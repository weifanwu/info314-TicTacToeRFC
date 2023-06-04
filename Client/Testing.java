import java.io.*;
import java.net.*;
import java.util.*;

public class Testing {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3116);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("This is the message from TCP!");

        DatagramSocket udpSocket = new DatagramSocket();
        String message = "This is the message from UDP!";
        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 3116;
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        udpSocket.send(sendPacket);
        System.out.println("Message sent to the server.");

    }
}
