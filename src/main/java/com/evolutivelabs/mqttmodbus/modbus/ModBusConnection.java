package com.evolutivelabs.mqttmodbus.modbus;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;

@Component
public class ModBusConnection {
	public TcpParameters tcpParameters = new TcpParameters();
	public ModbusMaster master;

	/**
	 * get ModbusMaster
	 * 
	 */
	public ModbusMaster getModbusMaster() {
		return master;
	}

	/**
	 * get ModbusMaster
	 * 
	 * @throws UnknownHostException
	 * @throws ModbusIOException
	 * 
	 */

	public ModbusMaster getModbusMaster(String ip, int port) throws UnknownHostException, ModbusIOException {
		
		InetAddress adress = InetAddress.getByName(ip);
		// TCP set ip
		tcpParameters.setHost(adress);
		// TCP long alive
		tcpParameters.setKeepAlive(true);

		tcpParameters.setPort(port);
		
		
		FrameEventListener listener = new FrameEventListener() {
			@Override
			public void frameSentEvent(FrameEvent event) {
				System.out.println("frame sent " + DataUtils.toAscii(event.getBytes()));
			}

			@Override
			public void frameReceivedEvent(FrameEvent event) {
				System.out.println("frame recv " + DataUtils.toAscii(event.getBytes()));
			}
		};
		// create master
		
		ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
		
		master.addListener(listener);
		master.setResponseTimeout(300);
		
		Modbus.setAutoIncrementTransactionId(true);
		// master.connect();
		return master;
	}

	/**
	 * close ModbusMaster
	 */

	public void destoryModbusMaster() {
		if (master.isConnected()) {
			try {
				master.disconnect();

			} catch (ModbusIOException e) {
				e.printStackTrace();
			}
		}
	}
}
