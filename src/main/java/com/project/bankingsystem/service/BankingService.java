package com.project.bankingsystem.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpEntity;

import com.project.bankingsystem.models.Customer;
import com.project.bankingsystem.models.SmsRequest;

public interface BankingService {

	Customer saveCustomer(Customer customer);

	boolean sendOtp(String contact);

	Map<String, Object> getAccounDetailByAccounNumber(String accountNumber);

	int updateAccountBalance(String accountNumber, Long totalBalance);
	
	HttpEntity<SmsRequest> sendSms(SmsRequest smsRequest) throws IOException;
//    HttpEntity<SmsRequestForAllUsers> sendSmsToAllRegisteredUsers(SmsRequestForAllUsers request);
	
	public int sendSms(String recipientNumber, String message);
	public  String generateRandomPassword();

	int updatePassword(String accountNumber, String newPassword);
	
	public double convertCurrency(String sourceCurrency, String targetCurrency, double amount);

}
