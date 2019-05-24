package app.controller;

import app.StudentCalc;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javax.swing.text.TableView.TableCell;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import app.controller.helper.*;

public class LoanCalcViewController implements Initializable   {

	private StudentCalc SC = null;
	
	@FXML
	private TextField LoanAmount;

	@FXML
	private Label lblTotalPayemnts;
	
	@FXML
	private DatePicker PaymentStartDate;
	
	@FXML
	private TextField NbrOfYears;
	
	@FXML
	private TextField InterestRate;
	
	@FXML
	private Label lblTotalInterest;
	
	@FXML
	private TextField ExtraPayment;
	
	@FXML
	private TableView<Payments> tableView;
	
	@FXML
	private TableColumn numCol;
	
	@FXML
	private TableColumn dueDateCol;
	
	@FXML
	private TableColumn paymentCol;
	
	@FXML
	private TableColumn adPaymentCol;
	
	@FXML
	private TableColumn interestCol;
	
	@FXML
	private TableColumn principalCol;
	
	@FXML
	private TableColumn balanceCol;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		numCol.setCellValueFactory(new PropertyValueFactory<Payments, Integer>("num"));
		dueDateCol.setCellValueFactory(new PropertyValueFactory<Payments, LocalDate>("due"));
		paymentCol.setCellValueFactory(new PropertyValueFactory<Payments, Double>("pay"));
		adPaymentCol.setCellValueFactory(new PropertyValueFactory<Payments, Double>("adPay"));
		interestCol.setCellValueFactory(new PropertyValueFactory<Payments, Double>("interest"));
		principalCol.setCellValueFactory(new PropertyValueFactory<Payments, Double>("prin"));
		balanceCol.setCellValueFactory(new PropertyValueFactory<Payments, Double>("balance"));
		 
		  
	}

	public void setMainApp(StudentCalc sc) {
		this.SC = sc;
	}
	
	/**
	 * btnCalcLoan - Fire this event when the button clicks
	 * 
	 * @version 1.0
	 * @param event
	 */
	
	@FXML
	private void btnCalcLoan(ActionEvent event) {

		double loanAmount = Double.parseDouble(LoanAmount.getText());
		int years = Integer.parseInt(NbrOfYears.getText());
		double intRate = Double.parseDouble(InterestRate.getText());
		
		lblTotalPayemnts.setText(String.valueOf(getTotalPayments(loanAmount,years,intRate)));
		lblTotalInterest.setText(String.valueOf(getTotalPayments(loanAmount,years,intRate)-loanAmount));
		
		
		
		tableView.setItems(getPayments());
		
	}
	
	
	public ObservableList<Payments> getPayments(){
		 
		ObservableList<Payments> payments = FXCollections.observableArrayList();
		
		Double bal = Double.parseDouble(LoanAmount.getText());
		double adPayment = Double.parseDouble(ExtraPayment.getText());
		bal=bal-adPayment;
		int years = Integer.parseInt(NbrOfYears.getText());
		int months = years*12;
		double intRate = Double.parseDouble(InterestRate.getText());
		double totalPay = getTotalPayments(bal,years,intRate);
	
		LocalDate date = PaymentStartDate.getValue();
		
		double payment = totalPay/months;
		double interest;
		double principal;
		
		payments.add(new Payments(0,null,0,adPayment,0,0,bal));
		
		for(int i=1; i<=months;i++ ) {
			interest=bal*intRate/1200;
			principal=payment-interest;
			bal=bal-principal;
			date=date.plusMonths(1);
			
			if(bal<0) {
				bal=0.0;
			}
	
			payments.add(new Payments(i,date,Math.round(payment*100)/100,0,Math.round(interest*100)/100,Math.round(principal*100)/100,Math.round(bal*100)/100));
			
		}
		
		
		return payments;
	}
	
	public double getTotalPayments(double loanAmount, int terms, double intRate) {
		intRate= intRate/1200;
		int year = terms*12;
		double numerator = intRate*loanAmount*year;
		double denominator = 1-(Math.pow(1+intRate, -1*year));
		double totalPayment = numerator/denominator;
		double total = Math.round(totalPayment*100)/100;
		
		
		return total;
	}
	
