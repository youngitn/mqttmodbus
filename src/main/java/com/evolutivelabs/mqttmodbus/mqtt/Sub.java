package com.evolutivelabs.mqttmodbus.mqtt;

import java.util.concurrent.TimeUnit;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.springframework.stereotype.Service;

@Service
public class Sub {

	final static String TOPIC_NAME = "text/javasub";
	final static String IP = "10.60.1.210";
	final static int PORT = 1883;
	final static String username = "pad";
	final static String password = "pad";

	public static void init() throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(IP, PORT); // 設定ip和port
		mqtt.setUserName(username);
		mqtt.setPassword(password);
		mqtt.setClientId("java_sub");
		mqtt.setWillTopic("willtopic/java_sub");
		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect(); // 連接Broker
		System.out.println("Connected to Broker!");
		//設置Topic，傳送品質為EXACTLY_ONCE
		Topic[] topics = { new Topic(TOPIC_NAME, QoS.EXACTLY_ONCE) };
		connection.subscribe(topics);

		while (true) {
			//取得訊息
			Message message = connection.receive(10, TimeUnit.SECONDS);
			if (message != null) {
				System.out.println("Received messages. " + new String(message.getPayload()));
				message.ack(); // 返回ack，告知Broker收到訊息
			}
		}

	}

}
