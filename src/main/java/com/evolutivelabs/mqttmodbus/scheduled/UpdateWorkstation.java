package com.evolutivelabs.mqttmodbus.scheduled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.evolutivelabs.mqttmodbus.data.Status;
import com.evolutivelabs.mqttmodbus.data.WorkStationInfo;
import com.evolutivelabs.mqttmodbus.modbus.ModBusUtil;
import com.evolutivelabs.mqttmodbus.mqtt.CallBackClient;
import com.evolutivelabs.mqttmodbus.service.PlcInfoService;
import com.google.gson.Gson;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;

/**
 * 作為長期執行服務
 * 測試版本
 * 
 * @author user
 *
 */

@Service
public class UpdateWorkstation {
	@Autowired
	PlcInfoService plcService;

	@Autowired
	ModBusUtil mbu;

	static Map<String, ModbusMaster> mms;

	final static Gson gson = new Gson();
	static List<String> aliveIps = new ArrayList<>();

	// Executes each 500 ms
	@SuppressWarnings("unchecked")
	//@Scheduled(fixedRate = 1000)
	public void checkRecords()
			throws FileNotFoundException, IOException, ParseException, InterruptedException, ExecutionException {

		JSONArray plcinfoList = plcService.getPlcInfoList();
		List<String> allWs = new ArrayList<>();

		if (mms == null) {
			mms = new HashMap<>();
			plcinfoList.stream().forEach((plcinfo) -> {
				String ip = ((JSONObject) plcinfo).get("ip").toString();
				try {

					// ModbusMaster m = mbu.createModbusMaster(ip);

					if (InetAddress.getByName(ip).isReachable(500)) {
						// aliveIps.add(ip);
						mms.put(ip, mbu.createModbusMaster(ip));
					}
				} catch (IOException | ModbusIOException e) {
					e.printStackTrace();
				}
			});
		}


		plcinfoList.forEach(plcinfo -> {
			try {
				
				//allWs.addAll(processList((JSONObject) plcinfo));
				CallBackClient.pushByList("test/ws", processList((JSONObject) plcinfo));

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
		
//		aComFuture.join();
//
//		aComFuture.get();
		// aComFuture.join();
//		if (aComFuture.isDone() && allWs.size() == 40) {
//			System.out.println("allWs--->" + allWs.size());
//			System.out.println("json--->" +gson.toJson(allWs));
//			
//
//		}
		mms = null;

	}

	@Async("taskExecutor")
	public List<String> processList(JSONObject plcinfo)
			throws UnknownHostException, InterruptedException, ExecutionException, ModbusIOException,
			ModbusProtocolException, ModbusNumberException {

		String ip = plcinfo.get("ip").toString();
		List<String> strList = new ArrayList<>();
		String mret = "";
		if (mms.get(ip) != null) {

			//mret = mbu.getModbusValue(mms.get(ip), ip, 1, 0, 16).get();
		}
		System.out.println(ip+"*******"+mret);
		JSONArray workstations = (JSONArray) plcinfo.get("workstations");
		
		
		for (Object workstation : workstations) {
			JSONObject inwk = ((JSONObject) workstation);
			// 取得各站id
			String id = inwk.get("id").toString();
			// 取得各站plc針腳位址
			String yellowbox = inwk.get("yellowbox").toString();

			WorkStationInfo wsi = new WorkStationInfo();
			Status stu = new Status();
			wsi.setId(id);

			// 沒有這台plc的連接資訊 給予紀錄
			if ("".equals(mret) || "timeout".equals(mret)) {
				wsi.setType("timeout");
				stu.setYellowBox("0");
			} else {
				String type = inwk.get("type").toString();
				if ("".equals(type)) {
					wsi.setType(null);
				} else {
					wsi.setType(type);
				}

				stu.setYellowBox(String.valueOf(checkYellowboxStatus(yellowbox, mret)));
				
			}
			
			wsi.setStatus(stu);
			System.out.println();
			strList.add(gson.toJson(wsi));
			
		}
		return strList;


	}

	public int checkYellowboxStatus(String yellowbox, String fromModbus) {

		String[] res = yellowbox.split("[,]", 0);
		System.out.println(fromModbus);
		int total = 0;
		for (String i : res) {
			int index = Integer.parseInt(i);
			index = (index >= 10) ? index - 2 : index;

			if ('1' == fromModbus.charAt(index)) {

				total++;
			}
		}

		return total;
	}

}