package com.example.demo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.demo.DemoService;

public class T {

	public static String a() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "a";
	}

	public static String t() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "t";
	}

	public static String ak() {
			ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
			reference.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
			reference.setRegistry(new RegistryConfig("zookeeper://192.168.0.167:2181"));
			reference.setInterface(DemoService.class);
			DemoService service = reference.get();
			return service.sayHello("测试");
	}
}
