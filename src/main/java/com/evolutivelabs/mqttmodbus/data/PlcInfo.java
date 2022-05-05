package com.evolutivelabs.mqttmodbus.data;

import java.util.List;

import lombok.Data;
/**
 * 
 * @author user
 *
 */
@Data
public class PlcInfo {
private String ip;
private List<plcWorkstation> plcWorkstations;
}
