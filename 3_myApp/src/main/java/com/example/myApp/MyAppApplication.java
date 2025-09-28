package com.example.myApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MyAppApplication {

	public static void main(String[] args) {
		//so basically this jvm [here we need to take obj adn everything, but we don't want] inside ioc container[this has to be created obj]
		//so we are using this to create obj
		ApplicationContext context = SpringApplication.run(MyAppApplication.class, args);

		//this like spring creat a obj so we just fetch from it and from which class, it can create for all class, but we dont want , so which class we need we added annotation @component
		Dev obj = context.getBean(Dev.class);
		obj.build();
	}

}
