package com.evolutivelabs.mqttmodbus.modbus;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.evolutivelabs.mqttmodbus.mqtt.CallBackClient;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;

@Component
public class ModBusUtil {

	@Autowired
	ModBusConnection mc;
	private static final Logger logger = LoggerFactory.getLogger(ModBusUtil.class);

	public ModbusMaster createModbusMaster(String ip) throws UnknownHostException, ModbusIOException {
		ModBusConnection mc = new ModBusConnection();

		return mc.getModbusMaster(ip, 502);

	}

	public ModBusConnection createModbusMasterConnection(String ip) throws UnknownHostException, ModbusIOException {

		return mc;

	}

	@Async("taskExecutor")
	public Future<String> getModbusValue(String ip, int port, int slaveId, int offset, int quantity)
			throws UnknownHostException, InterruptedException {

		String threadName = Thread.currentThread().getName();
		System.out.println("Go1-" + threadName);

		String requestString_x = "";
		try {
			ModbusMaster mm;
			mm = mc.getModbusMaster(ip, port);

			// is success
			if (!mm.isConnected()) {
				logger.info("ModBus is not Connected ip:" + ip + " will reconnect!");
				mm.connect();// 开启连接
			}
			logger.info("Connection OK");
			// read data，readInputRegisters讀取暫存器，function code = 04
			// int[] registerValues = mm.readInputRegisters(slaveId, offset, quantity);
			// boolean[] b = mm.readCoils(slaveId, offset, quantity);
			boolean[] x = mm.readDiscreteInputs(slaveId, offset, quantity);

			// output
			for (boolean value : x) {
				// System.out.println("Address: " + offset++ + ", Value: " + value);
				// 拼16進位
				// requestString += StringHex.byteToHex(value);
				if (value) {
					requestString_x += String.valueOf(1);
				} else {
					requestString_x += String.valueOf(0);
				}

			}

			// mm.disconnect();
			// Thread.sleep(1000);

		} catch (ModbusProtocolException e) {
			logger.error(e.getMessage());

		} catch (ModbusNumberException e) {
			e.printStackTrace();
		} catch (ModbusIOException e) {
			logger.error(ip + "   timeout");

			logger.error(e.getMessage());
			return new AsyncResult<String>("timeout");
		}

		return new AsyncResult<String>(requestString_x);
	}

	@Async("taskExecutor")
	public Future<String> getModbusValuen(ModbusMaster mm, String ip, int slaveId, int offset, int quantity)
			throws UnknownHostException, InterruptedException, ModbusIOException, ModbusProtocolException,
			ModbusNumberException {

		

		

		String requestString = "";
		try {
			Thread.currentThread().checkAccess();
			Thread.currentThread();

			mm.connect();

			logger.info("Connection OK " + ip);
			// read data，readInputRegisters读取的写寄存器，功能码04
			// int[] registerValues = mm.readInputRegisters(slaveId, offset, quantity);
			// boolean[] b = mm.readCoils(slaveId, offset, quantity);
			boolean[] readDiscreteInputs = mm.readDiscreteInputs(slaveId, offset, quantity);
			// Thread.sleep(200);
			logger.info("get value " + ip + readDiscreteInputs);

			// output
			for (boolean value : readDiscreteInputs) {
				// System.out.println("Address: " + offset++ + ", Value: " + value);
				// 拼16進位
				// requestString += StringHex.byteToHex(value);
				if (value) {
					requestString += String.valueOf(1);
				} else {
					requestString += String.valueOf(0);
				}

			}
			Thread.currentThread();

			Thread.currentThread().checkAccess();
			mm.disconnect();
			// Thread.sleep(1000);
			logger.info("get value done" + requestString);
			return new AsyncResult<String>(requestString);

		} catch (ModbusProtocolException e) {

			logger.error(e.getMessage());

			return new AsyncResult<String>("0000000000000001");
		} catch (ModbusNumberException e) {
			logger.error(e.getMessage());

			return new AsyncResult<String>("0000000000000001");
		} catch (ModbusIOException e) {
			logger.error(ip + " is  timeout");
			logger.error(e.getMessage());

			return new AsyncResult<String>("timeout");

		}
		// System.out.println("get value OKOKOKOKOKOKO!!!!"+ip);
		// Thread.currentThread().sleep(0);

		// return new AsyncResult<String>(requestString_x);
	}
}
