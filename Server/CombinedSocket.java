import java.io.*;
import java.net.*;
import java.util.*;



public class CombinedSocket {
     Socket TCPSocket;
     BufferedReader in;
     PrintWriter out;

     DatagramSocket UDPSocket;
     InetAddress IPAddress;
     int port;
    
     int playerID;
    
    public CombinedSocket(Socket TCPSocket) throws IOException {
        this.TCPSocket = TCPSocket;
        in = new BufferedReader(new InputStreamReader(this.TCPSocket.getInputStream()));
        out = new PrintWriter(TCPSocket.getOutputStream(), true);

    }

    public CombinedSocket(DatagramSocket UDPSocket, InetAddress IPAddress, int port) {
        this.UDPSocket = UDPSocket;
        this.IPAddress = IPAddress;
        this.port = port;
    }

}
