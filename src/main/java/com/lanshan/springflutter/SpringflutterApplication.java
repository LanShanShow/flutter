package com.lanshan.springflutter;

import com.lanshan.springflutter.server.DiscardServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringflutterApplication {

	public static void main(String[] args) {

			new Thread(() ->{
				try{
				DiscardServer.mains();
				}catch (Exception e){
				}
			}).start();

		SpringApplication.run(SpringflutterApplication.class, args);
	}
}
