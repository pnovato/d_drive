package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitManager
{

    private static final String EXCHANGE_NAME = "updates";
    private static final String HOST = "localhost";

    // Publica uma mensagem de atualização para um utilizador específico.
    public static void publishUpdate(String toUser, String message)
    {
        try
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel())
            {

                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
                String routingKey = "updates." + toUser;

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("[RabbitMQ] Enviou para " + toUser + ": " + message);
            }
        }
        catch (IOException | TimeoutException e)
        {
            e.printStackTrace();
        }
    }

    //Subscreve uma fila e processa mensagens com o callback fornecido.
    public static void subscribeToUserQueue(String username, DeliverCallback callback)
    {
        try
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);

            String queueName = "updates." + username;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, queueName);

            System.out.println("[RabbitMQ] Subscreveu numa fila: " + queueName);
            channel.basicConsume(queueName, true, callback, consumerTag -> {});

        }
        catch (IOException | TimeoutException e)
        {
            e.printStackTrace();
        }
    }
}

