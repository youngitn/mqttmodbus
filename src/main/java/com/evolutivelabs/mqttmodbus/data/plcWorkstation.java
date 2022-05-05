package com.evolutivelabs.mqttmodbus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * for PlcInfo,every plc contain 1~4 PlcWorkstation.
 * @author user
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class plcWorkstation {

	private String id;
	private String yellowbox;
	private String function;
	private String num;
	private String type;
	private String note;
}
