package com.project.bankingsystem.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.project.bankingsystem.config.SendChampConfig;
import com.project.bankingsystem.dao.BankingDao;
import com.project.bankingsystem.models.Customer;
import com.project.bankingsystem.models.SmsRequest;
import com.project.bankingsystem.repository.CustomerRepo;
import com.squareup.okhttp.OkHttpClient;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class BankingServiceImple implements BankingService {
	
	@Autowired
	Environment env;

	private final SendChampConfig sendChampConfig = new SendChampConfig();
	private final OkHttpClient client = new OkHttpClient();

//	@Value("${twilio.account.sid}")
//	private String twilioAccountSid;
//
//	@Value("${twilio.auth.token}")
//	private String twilioAuthToken;
//
//	@Value("${twilio.sender.number}")
//	private String twilioSenderNumber;
	
	

	@Autowired
	CustomerRepo repo;

	@Autowired
	BankingDao dao;

	@Override
	public Customer saveCustomer(Customer customer) {

		return repo.save(customer);

	}

	@Override
	public String generateRandomPassword() {
		String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
		String NUMERIC_CHARACTERS = "0123456789";
//		String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
		int PASSWORD_LENGTH = 4;
		String allCharacters = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + NUMERIC_CHARACTERS ;
		SecureRandom secureRandom = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int randomIndex = secureRandom.nextInt(allCharacters.length());
			char randomChar = allCharacters.charAt(randomIndex);
			password.append(randomChar);
		}

		return password.toString();
	}

	@Override
	public int sendSms(String recipientNumber, String message) {
		try {
			Twilio.init(env.getProperty("twilio.account.sid"), env.getProperty("twilio.auth.token"));
			 Message msg=Message.creator(new PhoneNumber(recipientNumber), new PhoneNumber(env.getProperty("twilio.sender.number")), message).create();
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return 0;
	}

	@Override
	public boolean sendOtp(String mobileNo) {
		// TODO Auto-generated method stub
		String otp = generateOtp();

		int saveOtp = dao.saveOtp(mobileNo, otp);

		// Store the OTP
//        otpStorage.put(customerId, otp);
//
//        // Send the OTP to the customer
//        messagingService.sendOtp(contactDetails, otp);
		return false;
	}

	private String generateOtp() {
		// Generate a random 6-digit OTP
		Random random = new Random();
		Integer randomNum = random.nextInt(899999) + 100000;
		return randomNum.toString();
	}

	@Override
	public Map<String, Object> getAccounDetailByAccounNumber(String accountNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> accountDetail = dao.accountDetail(accountNumber);
		return accountDetail;
	}

	@Override
	public int updateAccountBalance(String accountNumber, Long totalBalance) {
		// TODO Auto-generated method stub
		int updateBalance = dao.updateAccountBalance(accountNumber, totalBalance);
		return updateBalance;
	}

	@Override
	public HttpEntity<SmsRequest> sendSms(SmsRequest smsRequest) throws IOException {
		// TODO Auto-generated method stub

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(sendChampConfig.getAuthorization());
		return new HttpEntity<>(smsRequest, httpHeaders);

	}

	@Override
	public int updatePassword(String accountNumber, String newPassword) {
		// TODO Auto-generated method stub
		
		int updatePassword = dao.updateAccountPassword(accountNumber, newPassword);
		return updatePassword;
	}
	
	

	@Override
    public double convertCurrency(String sourceCurrency, String targetCurrency, double amount) {
       
    	Map<String, Double> exchangeRates=new HashMap<>(); 
        exchangeRates.put("RUP_EUR", 0.85);
        exchangeRates.put("Rup_yen", 0.72);
        exchangeRates.put("Rup_usd", 80.0);
    	
    	String currencyPair = sourceCurrency + "_" + targetCurrency;
        Double exchangeRate = exchangeRates.get(currencyPair);

        if (exchangeRate != null) {
            // Perform the currency conversion
            double convertedAmount = amount * exchangeRate;
            return convertedAmount;
        } else {
            throw new IllegalArgumentException("Exchange rate not found for the given currency pair");
        }
    }

}
