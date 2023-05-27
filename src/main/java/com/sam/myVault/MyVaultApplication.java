package com.sam.myVault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyVaultApplication.class, args);
	}

}
