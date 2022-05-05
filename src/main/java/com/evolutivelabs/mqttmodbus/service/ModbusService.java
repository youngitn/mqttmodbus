package com.evolutivelabs.mqttmodbus.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.evolutivelabs.mqttmodbus.data.Status;
import com.evolutivelabs.mqttmodbus.data.WorkStationInfo;
import com.evolutivelabs.mqttmodbus.modbus.ModBusConnection;
import com.evolutivelabs.mqttmodbus.modbus.ModBusUtil;
import com.google.gson.Gson;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;

@Service
public class ModbusService {

	
	
	ModBusConnection mbc;
	public String getMbValueByIp(String ip) throws UnknownHostException, ModbusIOException, InterruptedException,
			ExecutionException, ModbusProtocolException, ModbusNumberException {
		ModBusUtil mbu = new ModBusUtil();
		ModBusConnection mbc = new ModBusConnection();

		ModbusMaster mm = mbc.getModbusMaster(ip, 502);
		mbc.master = mm;
		String mret = mbu.getModbusValuen(mm, ip, 1, 0, 16).get();
		this.mbc = mbc;
		//mbc.destoryModbusMaster();
		return mret;
	}
	
	public ModBusConnection getMbc() {
		return this.mbc;
	}

	@Async("taskExecutor")
	public Future<List<String>> processList(JSONObject plcinfo, String mret)
			throws UnknownHostException, InterruptedException, ExecutionException, ModbusIOException,
			ModbusProtocolException, ModbusNumberException {
		Gson gson = new Gson();
		JSONArray workstations = (JSONArray) plcinfo.get("workstations");
		List<String> result = new ArrayList<>();

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
			result.add(gson.toJson(wsi));
		}

//		System.out.println(result.size());
		return new AsyncResult<List<String>>(result);
	}

	public int checkYellowboxStatus(String yellowbox, String fromModbus) {

		String[] res = yellowbox.split("[,]", 0);

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
