package com.evolutivelabs.mqttmodbus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * for APP 的 OBJ
 * @author user
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkStationInfo {
	private String id;
	private String type;
	private String note;
	private Status status;
}
