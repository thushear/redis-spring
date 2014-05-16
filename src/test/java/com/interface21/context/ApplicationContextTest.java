package com.interface21.context;

import com.interface21.context.support.FileSystemXmlApplicationContext;
import net.shopin.util.HttpClientUtil;

import java.io.IOException;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 下午6:12
 */
public class ApplicationContextTest {


    public static void main(String[] args) {

        try {
//            ApplicationContext context = new ClassPathXmlApplicationContext("D:\\clubworkspace\\redis-shopin\\target\\redis-0.0.1-SNAPSHOT\\WEB-INF\\classes\\spring\\application-config.xml");

        ApplicationContext context = new FileSystemXmlApplicationContext("D:\\clubworkspace\\redis-shopin\\src\\main\\resources\\spring\\application-config.xml");

         HttpClientUtil httpClientUtil = (HttpClientUtil) context.getBean("testC");
         System.out.println(httpClientUtil);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
