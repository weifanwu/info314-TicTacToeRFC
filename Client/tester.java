import java.net.*;
import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class tester {
    static ClientSockets communicate;
    static String client_id;
    static String game_id;
    static Scanner scanner;
    public static void main(String[] args) throws Exception {
        System.out.println("Enter server reference. ");
        scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parts = input.split("://|:");

        
        if (parts[0].equals("t3udp")) {
            DatagramSocket socket = new DatagramSocket();
            communicate = new ClientSockets(socket, InetAddress.getByName("localhost"), 3116);
            communicate.send("Connection...");
        } else if (parts[0].equals("t3tcp")) {
            Socket socket = new Socket("localhost", 3116);
            communicate = new ClientSockets(socket);
            // handle other ports
        } else {
            throw new Exception("The protocol that is given is not legal...");
        }
        session();
        communicate.send("LIST");
        System.out.println();
    }
    public static void session() throws Exception {
        System.out.print("Enter your User Name: ");
        String client = scanner.next();
        String message = "HELO " + 1 + " " + client;
        System.out.println("Sending the Greeting messages to the server: " + message);
        communicate.send(message);
        String serverResponse = communicate.receive();
        String[] numbers = serverResponse.split(" ");
        client_id = numbers[numbers.length - 1];
        System.out.println("This is your client ID: " + client_id);
    }
}