package com.evolutivelabs.mqttmodbus.mqtt;

import java.net.URISyntaxException;
import java.util.List;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CallBackClient {

	final static String TOPIC_NAME = "text/javasub";
	final static String IP = "10.60.1.210";
	final static int PORT = 1883;
	final static String username = "pad";
	final static String password = "pad";
	final private static MQTT mqtt = getMqttWithProperty();
	public final static CallbackConnection connection = mqtt.callbackConnection();
	private static final Logger logger = LoggerFactory.getLogger(CallBackClient.class);

	private CallBackClient() {
	}

	public static MQTT getMqttWithProperty() {
		if (mqtt == null) {
			MQTT mqtt = new MQTT();
			try {
				mqtt.setHost(IP, PORT);
			} catch (URISyntaxException e) {

				e.printStackTrace();
			} // 設定ip和port
			mqtt.setUserName(username);
			mqtt.setPassword(password);
			mqtt.setClientId("java_sub");
			mqtt.setWillTopic("willtopic/java_sub");
			return mqtt;

		}

		return mqtt;

	}

	public static void push(String tiopic, String payload) {

		// Send a message to a topic
		connection.publish(tiopic, payload.getBytes(), QoS.EXACTLY_ONCE, false, new Callback<Void>() {

			@Override
			public void onSuccess(Void v) {
				logger.info("push success!");
			}

			@Override
			public void onFailure(Throwable value) {
				logger.error(connection.failure().getMessage());
			}
		});

	}

	public static void init(String payload) throws Exception {

		connection.listener(new Listener() {
			@Override
			public void onDisconnected() {
				logger.info("onDisconnected!!!");
			}

			@Override
			public void onConnected() {
				logger.info("onConnected!!!");
			}

			@Override
			public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
				// You can now process a received message from a topic.
				// Once process execute the ack runnable.
				switch (topic.toString()) {
				case "foo":
					System.out.println(topic + "   " + payload.utf8());
					break;
				}

				ack.run();
			}

			@Override
			public void onFailure(Throwable value) {
				// a connection failure occured.
				System.out.println("onFailure!!!");
				logger.error(value.getMessage());
			}

		});

		connection.connect(new Callback<Void>() {

			@Override
			public void onFailure(Throwable value) {
				logger.info("onFailure");
				logger.error(value.getMessage());// If we could not connect to the server.
			}

			// Once we connect..
			@Override
			public void onSuccess(Void v) {

				logger.info("mqtt broker connection Success!!");
				// CallBackClient.push("test/lolx", "GO");
				// Subscribe to a topic
				Topic[] topics = { new Topic("foo", QoS.AT_LEAST_ONCE) };
				connection.subscribe(topics, new Callback<byte[]>() {
					@Override
					public void onSuccess(byte[] qoses) {
						// The result of the subcribe request.
						System.out.println("---------------------------------->" + qoses.toString());
					}

					@Override
					public void onFailure(Throwable value) {
						System.out.println(connection.failure().getMessage());
					}
				});
//
//				// Send a message to a topic
//				connection.publish("foo", payload.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
//					@Override
//					public void onSuccess(Void v) {
//						System.out.println("push success!");
//					}
//
//					@Override
//					public void onFailure(Throwable value) {
//						System.out.println(connection.failure().getMessage());
//					}
//				});

//				// To disconnect..
//				connection.disconnect(new Callback<Void>() {
//					@Override
//					public void onSuccess(Void v) {
//						// called once the connection is disconnected.
//						System.out.println("disconnected!!");
//					}
//					@Override
//					public void onFailure(Throwable value) {
//						// Disconnects never fail.
//						System.out.println(connection.failure().getMessage());
//					}
//				});
			}

		});

//		while (true) {
//
//		}

	}

	public static void pushByList(String tiopic, List<String> allWs) {
		System.out.println("------------------------------");
		// System.out.println(payload);
		System.out.println("------------------------------");
		// Send a message to a topic
		for (String payload : allWs) {
			connection.publish(tiopic, payload.getBytes(), QoS.EXACTLY_ONCE, false, new Callback<Void>() {

				@Override
				public void onSuccess(Void v) {
					System.out.println(payload);
					System.out.println("push success!");
				}

				@Override
				public void onFailure(Throwable value) {
					System.out.println(connection.failure().getMessage());
				}
			});
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
