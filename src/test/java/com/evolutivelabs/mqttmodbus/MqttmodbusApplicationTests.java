package com.evolutivelabs.mqttmodbus;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.evolutivelabs.mqttmodbus.modbus.ModBusUtil;
import com.evolutivelabs.mqttmodbus.service.ModbusService;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;

@SpringBootTest
class MqttmodbusApplicationTests {
	
	

	@Test
	void contextLoads() throws InterruptedException, ExecutionException, UnknownHostException, ModbusIOException,
			ModbusProtocolException, ModbusNumberException {

//		ModbusService ms = new ModbusService();
//		String mret = ms.getMbValueByIp("10.63.1.31");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.32");
//	
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.31");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.32");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.31");
//	
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.32");
//		
//		System.out.println(mret);
//		
//		mret = ms.getMbValueByIp("10.63.1.31");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.32");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.31");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.31");
//	
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.32");
//		
//		System.out.println(mret);
//		
//		mret = ms.getMbValueByIp("10.63.1.34");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.35");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.31");
//		
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.34");
//	
//		System.out.println(mret);
//		mret = ms.getMbValueByIp("10.63.1.35");
//		
//		System.out.println(mret);
		
	}

}
