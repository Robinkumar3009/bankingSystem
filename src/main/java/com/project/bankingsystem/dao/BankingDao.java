
package com.project.bankingsystem.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BankingDao{
	
	@Autowired
	JdbcTemplate jdbc;

	public int saveOtp(String mobileNo, String otp) {
		// TODO Auto-generated method stub
		
		String query = "insert into otp_save_table(otp_number,mobile_no) values(?,?)";
		int insertOpt = jdbc.update(query,mobileNo ,otp  );
		return insertOpt;
	}

	public Map<String, Object> accountDetail(String accountNumber) {
		// TODO Auto-generated method stub
		String query="SELECT * FROM customer " +
                "JOIN account ON customer.account_no=account.account_number where account.account_number='"+accountNumber+"'";

		Map<String,Object> details=jdbc.queryForMap(query);
		return details;
	}

	public int updateAccountBalance(String accountNumber, Long totalBalance) {
		// TODO Auto-generated method stub
		
//		UPDATE account
//		set password = '$2a$10$TUULUvQpsiMTeQAm2yrzE.kjyLf8M3w4sBY9ukcbPFvWgpEcDo4Te' 
//		from customer
//		where customer.account_no = account.account_number
//		and account.account_number= '6112304962368129223'
		
		String query="UPDATE account"
				+ " set balance = '"+totalBalance+"'"
				+ " from customer"
				+ " where customer.account_no = account.account_number"
				+ " and account.account_number= '"+accountNumber+"'";
		int update=jdbc.update(query);
		
		
		return update;
	}

	public int updateAccountPassword(String accountNumber, String newPassword) {
		// TODO Auto-generated method stub
		
//		UPDATE account
//		SET password = '$2a$10$dKjELwjOZg4.qmyuhBo/NedYyYkvVDeuRAHK/TbsZ4EojItEnaC/6'
//		FROM customer
//		WHERE customer.account_no = account.account_number
//		  AND account.account_number = '6112304962368129223';
		
		String query="UPDATE account"
				+ " set password = '"+newPassword+"'"
				+ " from customer"
				+ " where customer.account_no = account.account_number"
				+ " and account.account_number= '"+accountNumber+"'";
		int update=jdbc.update(query);
		return update;
	}
	
//	public void saveCustomerAndAccount(Customer customer, Account account) {
//        String customerInsertSql = "INSERT INTO customer (name, address, contact) VALUES (?, ?, ?)";
//        String accountInsertSql = "INSERT INTO account (accountNumber, accountType, balance, customerId) VALUES (?, ?, ?, ?)";
//
//        // Save customer
//        jdbc.update(customerInsertSql, customer.getName(), customer.getAddress(), customer.getContact());
//
//        // Retrieve the generated customer ID
//        Long customerId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
//
//        // Save account
//        jdbcTemplate.update(accountInsertSql, account.getAccountNumber(), account.getAccountType(),
//                account.getBalance(), customerId);
//    }

}
