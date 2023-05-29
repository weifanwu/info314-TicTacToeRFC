import java.io.*;
import java.net.*;
import java.util.*;

public class Game {
    public static void main(String[] args) throws IOException {
        // Create a server socket on port 12345
        ServerSocket serverSocket = new ServerSocket(3116);
        Map<String, Board> recording = new HashMap<>();
        while(true) {
            System.out.println("Server started. Waiting for SESSION communication...");
            session(serverSocket);
            enterLobby(serverSocket);
            serverSocket.close();
        }
    }
    public static void session(ServerSocket serverSocket) throws IOException {
        // Accept client connection
        System.out.println("Server started. Waiting for client connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected.");

        // Create input and output streams for the client socket
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Read data from the client and send a response
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received from client: " + inputLine);
            out.println("SESS 1 1");
        }
        // Close the connection
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void enterLobby(ServerSocket serverSocket) throws IOException {
        System.out.println("Waiting for actions of clients...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected.");

        // Create input and output streams for the client socket
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Read data from the client and send a response
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received from client: " + inputLine);
        }
        String[] actions = inputLine.split(" ");
        String action = actions[0];
        if (action.equals("LIST")) {
            System.out.println("Starting the list all the available games");
        } else if (action.equals("CREA")) {
            System.out.println("Create new games");
            
        } else {
            System.out.print("Command doesn't exist");
        }
        out.println("SESS 1 1");

    }
}