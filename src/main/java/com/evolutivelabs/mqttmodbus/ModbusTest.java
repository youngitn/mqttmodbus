package com.evolutivelabs.mqttmodbus;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;

import com.evolutivelabs.mqttmodbus.service.ModbusService;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;

public class ModbusTest {
 public static void main(String arg[]) throws UnknownHostException, ModbusIOException, InterruptedException, ExecutionException, ModbusProtocolException, ModbusNumberException {
	 ModbusService modbusService = new ModbusService();
		
		
		String ip = "10.63.1.31";
		String mret = modbusService.getMbValueByIp(ip);
 }

}
