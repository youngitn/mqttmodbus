package com.evolutivelabs.mqttmodbus.scheduled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.evolutivelabs.mqttmodbus.data.Status;
import com.evolutivelabs.mqttmodbus.data.WorkStationInfo;
import com.evolutivelabs.mqttmodbus.mqtt.CallBackClient;
import com.evolutivelabs.mqttmodbus.service.ModbusService;
import com.evolutivelabs.mqttmodbus.service.PlcInfoService;
import com.google.gson.Gson;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;


/**
 * 作為長期執行服務
 * 
 * @author user
 *
 */

@Service
public class RecordChecker {
	@Autowired
	PlcInfoService plcService;

	@Autowired
	ModbusService ms;

	private static final Logger logger = LoggerFactory.getLogger(RecordChecker.class);

	@SuppressWarnings("unchecked")
	@Scheduled(fixedDelay = 1000)
	public void checkRecords()
			throws FileNotFoundException, IOException, ParseException, InterruptedException, ExecutionException {
		Gson gson = new Gson();
		// 取得所有工作站定義檔 記錄在workStationMap.json
		JSONArray plcinfoList = plcService.getPlcInfoList();

		// 最後會轉乘json送給mqtt的list
		List<WorkStationInfo> allWs = new ArrayList<>();

		//ExecutorService pool = Executors.newFixedThreadPool(5);
		//CompletableFuture<?> aComFuture = CompletableFuture.supplyAsync(() -> "", pool);

		plcinfoList.forEach(plcinfo -> {
			try {

				allWs.addAll(processList((JSONObject) plcinfo));

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
			// return str;
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

	
		// aComFuture.join();
		if ( allWs.size() == 40) {
			String json = gson.toJson(allWs);
			//logger.info("json--->" + json);
			CallBackClient.push("pad/ws_all", json);
			json = null;
			ms.getMbc().destoryModbusMaster();
			
			
		}
		
		plcinfoList = null;
	
		System.gc();
		
		
	}

	
	public List<WorkStationInfo> processList(JSONObject plcinfo) throws InterruptedException,
			ExecutionException, ModbusIOException, ModbusProtocolException, ModbusNumberException, IOException {

		String ip = plcinfo.get("ip").toString();

		String mret = "";

		if (InetAddress.getByName(ip).isReachable(500)) {
			mret = ms.getMbValueByIp(ip);
		}

		JSONArray workstations = (JSONArray) plcinfo.get("workstations");

		List<WorkStationInfo> result = new ArrayList<WorkStationInfo>();

		for (Object workstation : workstations) {
			JSONObject inwk = ((JSONObject) workstation);
			// 取得各站id
			String id = inwk.get("id").toString();
			// 取得各站plc針腳位址
			String yellowbox = inwk.get("yellowbox").toString();
			String note =  inwk.get("note")==null?"":inwk.get("note").toString();
			WorkStationInfo wsi = new WorkStationInfo();
			Status stu = new Status();
			wsi.setId(id);
			wsi.setNote(note);
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
			//inwk = null;
			wsi.setStatus(stu);
			result.add(wsi);
		}
		mret= null;
		
		return result;
	}

	// 比對plc針腳與工作站所定義的位址做比較
	public int checkYellowboxStatus(String yellowbox, String fromModbus) {

		// 工作站定義位址解析成陣列
		String[] boxAddr = yellowbox.split("[,]", 0);

		// 燈號分數 1到3分
		int total = 0;

		for (String addr : boxAddr) {
			int index = Integer.parseInt(addr);
			// 因為plc腳位和定義位址大於10的部分會差2 從1~7跳到10~..
			index = (index >= 10) ? index - 2 : index;
			// 所屬位址 == 1 燈號分數就增加
			if ('1' == fromModbus.charAt(index)) {

				total++;
			}
		}

		return total;
	}

}