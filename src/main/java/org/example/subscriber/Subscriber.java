package org.example.subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.example.broker.BrokerSocket;
import org.example.utility.TransportService;
import org.example.utility.Constants;
public class Subscriber {
    public static void main(String[] a) throws IOException {
        Socket socket;
        String name;
        String message;
        String command;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        socket = new Socket(Constants.HOSTNAME, Constants.PORT);

        System.out.println("Input your name: ");
        name = bufferedReader.readLine();
        BrokerSocket brokerSocket = new TransportService(socket);
        System.out.println("Input \"Connect\" command to be connected to broker");

        command = bufferedReader.readLine();
        while (true)
            if (command.equals("Connect")) {
                message = command + " " + name + "\n";
                brokerSocket.writeAsync(message);
                System.out.println("Connected to broker");
                break;
            } else
                System.out.println("No connection");

        Runnable r = new SubscriberReaderThread(brokerSocket);
        new Thread(r).start();
        System.out.println("Input \"Disconnect" + "\" command to be disconnected from broker");

        while (true) {
            command = bufferedReader.readLine();
            if (command.equals("Disconnect")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket = new Socket(Constants.HOSTNAME, Constants.PORT);
                brokerSocket = new TransportService(socket);
                brokerSocket.writeAsync(command + " " + name + "\n");
                break;
            }
        }
    }
}
