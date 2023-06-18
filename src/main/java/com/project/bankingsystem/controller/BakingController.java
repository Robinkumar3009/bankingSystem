package com.project.bankingsystem.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bankingsystem.models.Account;
import com.project.bankingsystem.models.AccountDetails;
import com.project.bankingsystem.models.Customer;
import com.project.bankingsystem.service.BankingService;

@RestController
public class BakingController {

	@Autowired
	BankingService service;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/registerCustmer")
	public ResponseEntity<?> registerCustmer(@RequestBody AccountDetails accountDetail) {
		try {
			Customer customer = new Customer();
			Account account = new Account();

			String password = service.generateRandomPassword();
			Date date = new Date();
			customer.setName(accountDetail.getName());
			customer.setDateOfBirth(accountDetail.getDateOfBirth());
			customer.setContact(accountDetail.getContact());
			customer.setAdharNo(accountDetail.getAdharNo());
			customer.setCreatedOn(date.toString());
			customer.setAddress(accountDetail.getAddress());
			UUID uuid = UUID.randomUUID();
			long numericValue = Math.abs(uuid.getMostSignificantBits());
			String accountNumber = String.format("%016d", numericValue);
			customer.setAccountNo(accountNumber);
			account.setAccountNumber(accountNumber);
			account.setAccountType(accountDetail.getAccountType());
			account.setBalance(accountDetail.getBalance());
			account.setPassword(passwordEncoder.encode(password));
			customer.setAccount(account);
			account.setCustomer(customer);
			Customer save = service.saveCustomer(customer);
			if (save != null) {
				int sendmessage = service.sendSms(accountDetail.getContact(),
						"your account no:-" + accountNumber + " and account password:" + password);
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "account created successfully..");
				response.put("status", 200);
				return ResponseEntity.status(HttpStatus.OK).body(response);

			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "error occur during account creation..");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return ResponseEntity.status(HttpStatus.OK).body("CustomerRegister Successfully....");

	}

	@PutMapping("/updateAccountPassword")
	public ResponseEntity<?> updateAccountPassword(@RequestParam(required = true) String accountNumber,
			@RequestParam(required = true) String password, @RequestParam(required = true) String newPassword) {
		Map<String, Object> accountDetail = service.getAccounDetailByAccounNumber(accountNumber);

		if (accountDetail != null) {
			String hashPassword = (String) accountDetail.get("password").toString();
			boolean isPasswordMatched = passwordEncoder.matches(password, hashPassword);

			if (isPasswordMatched) {
				int updatePasswordOfAccountHolder = service.updatePassword(accountNumber,
						passwordEncoder.encode(newPassword));

				if (updatePasswordOfAccountHolder > 0) {
					Map<String, Object> response = new HashMap<>();
					response.put("msg", "password updated successfully..");
					response.put("status", 200);
					return ResponseEntity.status(HttpStatus.OK).body(response);
				} else {
					Map<String, Object> response = new HashMap<>();
					response.put("msg", "error occur while updating account password");
					response.put("status", 200);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

				}
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "incorrect password..");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

			}

		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("msg", "not data found please check accountnumber");
			response.put("status", 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

	}

	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestParam double amount, @RequestParam String accountNumber,
			@RequestParam String password) {

		int updateDataOfAccountHoder;
		Map<String, Object> accountDetail = service.getAccounDetailByAccounNumber(accountNumber);

		if (accountDetail != null) {

			String hashPassword = (String) accountDetail.get("password").toString();
			boolean isPasswordMatched = passwordEncoder.matches(password, hashPassword);
			if (isPasswordMatched) {
				Long totalBalance = (long) (Double.parseDouble(accountDetail.get("balance").toString()) - amount);
				updateDataOfAccountHoder = service.updateAccountBalance(accountNumber, totalBalance);

				if (updateDataOfAccountHoder > 0) {
					int sendmessage = service.sendSms(accountDetail.get("contact").toString(), "Rs. " + amount
							+ " deducted from your  account no:-" + accountNumber + " avlbl amount :" + totalBalance);

					Map<String, Object> response = new HashMap<>();
					response.put("msg", "balance updated successfully..");
					response.put("status", 200);
					return ResponseEntity.status(HttpStatus.OK).body(response);
				} else {
					Map<String, Object> response = new HashMap<>();
					response.put("msg", "error occur while updating account balance");
					response.put("status", 200);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

				}
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "incorrect password..");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

			}
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("msg", "not data found please check accountnumber");
			response.put("status", 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		}

	}

	@PostMapping("/deposite")
	public ResponseEntity<?> deposite(@RequestParam(required = true) String accountNumber,
			@RequestParam(required = true) double amount) {
		
		int updateDataOfAccountHoder;
		Map<String, Object> accountDetail = service.getAccounDetailByAccounNumber(accountNumber);

		if (accountDetail != null) {

			Long totalBalance = (long) (Double.parseDouble(accountDetail.get("balance").toString()) + amount);
			updateDataOfAccountHoder = service.updateAccountBalance(accountNumber, totalBalance);

			if (updateDataOfAccountHoder > 0) {
				int sendmessage = service.sendSms(accountDetail.get("contact").toString(), "Rs." + amount
						+ " credited in your account no:-" + accountNumber + " avlbl amount :" + totalBalance);

				Map<String, Object> response = new HashMap<>();
				response.put("msg", "balance updated successfully..");
				response.put("status", 200);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "error occur while updating account balance");
				response.put("status", 200);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

			}

		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("msg", "not data found please check accountnumber");
			response.put("status", 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		}

	}

	@PostMapping("/transfer")
	public ResponseEntity<?> transfer(@RequestParam(required = true) String accountNoHolder,
			@RequestParam(required = true) String accountNoTransferTo,
//			@RequestParam(required = true) String TransferFrom, @RequestParam(required = true) String TransferTo,
			@RequestParam(required = true) String convertFrom, @RequestParam(required = true) String convertTo,
			@RequestParam(required = true) double amount) {
		
		System.out.println("hello");
		
		Map<String, Object> accountHolderDetail = service.getAccounDetailByAccounNumber(accountNoHolder);
		Map<String, Object> accountTransferDetail = service.getAccounDetailByAccounNumber(accountNoTransferTo);
		if((accountHolderDetail.size()>0 || accountHolderDetail==null) && (accountTransferDetail.size()>0 || accountTransferDetail==null) )
		{
			double Convertedamount = service.convertCurrency(convertFrom, convertTo, amount);
			
			Long StartingHoldertotalBalance = (long) (Double.parseDouble(accountHolderDetail.get("balance").toString()));
			
			
			
			Long HoldertotalBalance = (long) (Double.parseDouble(accountHolderDetail.get("balance").toString()) - Convertedamount);
			if(HoldertotalBalance <1000)
			{
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "account total account balance is less then 1000 ");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			int updateDataOfAccountHoder = service.updateAccountBalance(accountNoHolder, HoldertotalBalance);
			
			if(updateDataOfAccountHoder>0)
			{
			
			Long transfertotalBalance = (long) (Double.parseDouble(accountTransferDetail.get("balance").toString()) + Convertedamount);
			
			int updateDataOfAccountTransfer = service.updateAccountBalance(accountNoTransferTo, transfertotalBalance);
			if(updateDataOfAccountTransfer<=0)
			{
				updateDataOfAccountHoder = service.updateAccountBalance(accountNoHolder, StartingHoldertotalBalance);

				Map<String, Object> response = new HashMap<>();
				response.put("msg", "failed in updating of account balance of transfer");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
				
			}
			else
			{
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "transfer balance updated successfully..");
				response.put("status", 200);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			}
			else
			{
				Map<String, Object> response = new HashMap<>();
				response.put("msg", "failed in updating of account balance of holder");
				response.put("status", 400);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
		}else
		{
			Map<String, Object> response = new HashMap<>();
			response.put("msg", "not data found please check account_number of holder  or account_number of transfer to ");
			response.put("status", 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			
		}
		

	}
}
