package com.evolutivelabs.mqttmodbus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {
	private String yellowBox;
	private String blueBox;

}
