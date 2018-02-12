package rabbitmq.queues.normal;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RMQMainRecv {
    private final static String QUEUE_NAME = "hello";

    private static String getMessage(String[] str) {

        if (str.length < 1) {
            return "HW";
        } else {
            return joinStrings(str, " ");
        }
    }


    private static String joinStrings(String[] arr, String delimiter) {
        int length = arr.length;

        if (length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : arr) {
                sb.append(delimiter).append(s);
            }
            return sb.toString();
        }
    }

    private static void doWork(String str) throws InterruptedException {
        int timeToSleep = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                timeToSleep++;
            }
        }
        Thread.sleep(timeToSleep * 1000);
    }


    public static void main(String[] args) throws java.io.IOException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //declare the queue from where we want to consume from
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                try {
                    doWork(message);
                } catch (Exception c) {
                } finally {
                    System.out.println(" [x] Done");
                }
            }
        };

        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
