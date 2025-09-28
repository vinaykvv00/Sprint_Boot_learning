package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Dev obj = context.getBean(Dev.class);
       // obj.setAge(23);
       // System.out.println(obj.getAge());

        obj.build();
        System.out.println( "Hello World!" );
    }
}
