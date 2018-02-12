package rabbitmq.queues.normal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RMQ Demo App
 */

public class RMQMainSend {

    private final static String QUEUE_NAME = "hello";

    private final static String TASK_QUEUE_NAME = "hello_task";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        //connecting to the broker
        Connection conn = factory.newConnection();

        //channel APIs are most ustilised
        Channel ch = conn.createChannel();

        //Declare the Queue
        ch.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Publisher sending a message saying HW";

        ch.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("[X] Sent:" + message);

        ch.close();
        conn.close();
    }
}
