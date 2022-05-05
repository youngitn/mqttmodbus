package com.evolutivelabs.mqttmodbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.evolutivelabs.mqttmodbus.mqtt.CallBackClient;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class MqttmodbusApplication {

//	@Autowired
//	Pub pub;
//	@Autowired
//	Sub sub;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MqttmodbusApplication.class, args);
		
		
		//Sub.init();
		CallBackClient.init("1");
		
		
		
		
		
		
	}

}
