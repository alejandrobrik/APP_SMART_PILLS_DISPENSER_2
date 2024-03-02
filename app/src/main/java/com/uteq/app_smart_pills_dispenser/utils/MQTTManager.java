package com.uteq.app_smart_pills_dispenser.utils;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTManager {

    private static final String MQTT_SERVER_URI = "tcp://52.201.104.43:1883";
    private static final String MQTT_TOPIC = "control/dispensador";

    private Context context;
    private MqttClient mqttClient;
    private boolean huellaOkReceived; // Variable global para almacenar el estado

    public MQTTManager(Context context) {
        this.context = context;
    }

    public void connectAndSubscribe() {
        try {
            mqttClient = new MqttClient(MQTT_SERVER_URI, MqttClient.generateClientId(), null);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Manejar la pérdida de conexión
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Manejar los mensajes recibidos
                    String payload = new String(message.getPayload());
                    if (payload.equals("huellaOk")) {
                        // Mostrar mensaje de huella registrada
                        showToast("¡Huella registrada!");
                        huellaOkReceived = true; // Establecer la variable en true cuando se recibe el mensaje
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Este método se llama cuando un mensaje se entrega con éxito
                }
            });

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String message) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(message.getBytes());
                mqttClient.publish(MQTT_TOPIC, mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void showToast(final String message) {
        // Mostrar el mensaje en el hilo principal
        ((Activity) context).runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}