import java.io.*;
import java.net.*;
import java.util.*;



public class ClientSockets {
     Socket TCPSocket;
     BufferedReader in;
     PrintWriter out;

     DatagramSocket UDPSocket;
     InetAddress IPAddress;
     int port;
    
     int playerID;
     String protocol;
    public ClientSockets(Socket TCPSocket) throws Exception {
        this.TCPSocket = TCPSocket;
        in = new BufferedReader(new InputStreamReader(this.TCPSocket.getInputStream()));
        out = new PrintWriter(TCPSocket.getOutputStream(), true);
        protocol = "TCP";
    }

    public ClientSockets(DatagramSocket UDPSocket, InetAddress IPAddress, int port) {
        this.UDPSocket = UDPSocket;
        this.IPAddress = IPAddress;
        this.port = port;
        protocol = "UDP";
    }

    public void send(String message) throws Exception {
        if (protocol.equals("TCP")) {
            out.println(message);
        } else {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            UDPSocket.send(sendPacket);
        }
    }

    public String receive() throws Exception {
        if (protocol.equals("TCP")) {
            String message = in.readLine();
            return message;
        } else {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            UDPSocket.receive(receivePacket);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            return message;
        }
    }
}
