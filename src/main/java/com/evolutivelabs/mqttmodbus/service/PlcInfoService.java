package com.evolutivelabs.mqttmodbus.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@PropertySource(value = "file:./mqttmodbus.properties", encoding = "utf-8")
@Service
public class PlcInfoService {
	@Autowired
	Environment env;
	static JSONArray jsonArray;
	private static final Logger logger = LoggerFactory.getLogger(PlcInfoService.class);

	public JSONArray getPlcInfoList() throws FileNotFoundException, IOException, ParseException {
		if (jsonArray != null) {
			return jsonArray;
		}
		JSONParser parser = new JSONParser();
		String path = env.getProperty("plc.info.path");
		JSONObject jsonObject = (JSONObject) (parser.parse(new FileReader(path)));
		jsonArray = (JSONArray) jsonObject.get("infolist");
		logger.info("do getPlcInfoList");
		return jsonArray;

	}

}
