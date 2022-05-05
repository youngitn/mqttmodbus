package com.evolutivelabs.mqttmodbus.scheduled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.evolutivelabs.mqttmodbus.mqtt.CallBackClient;
import com.evolutivelabs.mqttmodbus.service.ModbusService;
import com.evolutivelabs.mqttmodbus.service.PlcInfoService;
import com.google.gson.Gson;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;

/**
 * 測試用
 * 
 * @author user
 *
 */

@Service
public class Plc2 {
	@Autowired
	PlcInfoService plcService;
	
	static Map<String, ModbusMaster> mms;

	final static Gson gson = new Gson();
	static List<String> aliveIps = new ArrayList<>();

	// Executes each 500 ms
	@SuppressWarnings("unchecked")
	//@Scheduled(fixedRate = 1000)
	@Async("taskExecutor")
	public void checkRecords()
			throws FileNotFoundException, IOException, ParseException, InterruptedException, ExecutionException, ModbusIOException, ModbusProtocolException, ModbusNumberException {
		ModbusService modbusService = new ModbusService();
		JSONArray plcinfoList = plcService.getPlcInfoList();
		List<String> allWs = new ArrayList<>();
		String ip = "10.63.1.32";
		String mret = modbusService.getMbValueByIp(ip);
		

		plcinfoList.forEach(plcinfo -> {
			try {
				JSONObject plcinfoJo = (JSONObject) plcinfo;
			
				if (ip.equals(plcinfoJo.get("ip"))) {
					allWs.addAll(modbusService.processList(plcinfoJo,mret).get());
					CallBackClient.pushByList("test/ws",allWs );
					modbusService.getMbc().destoryModbusMaster();
				}
					
				// }
				// str.append(processList((JSONObject) plcinfo));

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (ExecutionException e) {

				e.printStackTrace();
			} catch (ModbusIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return str;

		});

		

	}

	

}