package com.project.bankingsystem.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfiguration {
	
	@Autowired
	Environment env;

	@Bean
	public DataSource source() throws Exception {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
		dataSourceBuilder.url(env.getRequiredProperty("spring.datasource.url"));
		dataSourceBuilder.username(env.getRequiredProperty("spring.datasource.username"));
		dataSourceBuilder.password(env.getRequiredProperty("spring.datasource.password"));
		return dataSourceBuilder.build();
	}

	@Bean
	@Autowired
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jd = new JdbcTemplate();
		jd.setDataSource(dataSource);
		return jd;
	}


}
