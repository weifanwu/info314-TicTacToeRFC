import java.io.*;
import java.net.*;
import java.util.*;

public class Game {
    static ServerSocket serverSocket;
    static BufferedReader in;
    static PrintWriter out;
    static Socket clientSocket;
    public static void main(String[] args) throws IOException {
        while(true) {
            initializeServer();        
            session();
            enterLobby();
            close();
        }
    }

    public static void initializeServer() throws IOException {
        System.out.println("Start to intialize the server...");
        // Create a server socket on port 3116
        serverSocket = new ServerSocket(3116);
    
        // Initialize the socket reader
        System.out.println("Listen to the port 3116...");
        clientSocket = serverSocket.accept();
        System.out.println("Server started on port 3116...");
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);    
    }

    public static void close() throws IOException {
        System.out.println("Closing the program...");
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void session() throws IOException {
        // Accept client connection
        System.out.println("Session stage...");
        // Read data from the client and send a response
        String inputLine = in.readLine();
        System.out.println("Received from client: " + inputLine);
        out.println("SESS 1 1");
        System.out.println("The end of the session stage...");
    }

    public static void enterLobby() throws IOException {
        System.out.println("Waiting for actions of clients...");
        // Read data from the client and send a response according to request
        String inputLine = in.readLine();
        System.out.println("This is the action of the client: " + inputLine);
        String[] actions = inputLine.split(" ");
        String action = actions[0];
        if (action.equals("LIST")) {
            System.out.println("Starting the list all the available games");
        } else if (action.equals("CREA")) {
            System.out.println("Create new games");
        } else {
            System.out.print("Command doesn't exist");
        }
    }
}