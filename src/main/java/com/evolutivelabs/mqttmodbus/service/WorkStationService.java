package com.evolutivelabs.mqttmodbus.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

/**
 * 未來workStationMap.JSON會棄用 改為DB儲存 該服務針對從DB或API來的工作站資訊作重整以符合workStationMap的格式,
 * 程式端預計不做更動.
 * 
 * @author user
 *
 */
@Service
public class WorkStationService {
	
	//回傳結果(這邊的回傳值JsonObject是用GSON的)
	public JsonObject getWorkStationMapJson() {
		getWorkStationDefindData();
		processDataStructure();
		return null;
	}
	

	//從DB或API取得DATA
	public ArrayList getWorkStationDefindData() {
		return null;
	}
	
	//處理結構
	public ArrayList processDataStructure() {
		//給屬性
		
		//結構調整(根據原JSON建立物件來做)
		return null;
	}

}
