import java.io.*;
import java.net.*;

public class Game {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3116);
        System.out.println("Server started on port 3116...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from client: " + clientSocket.getInetAddress());

            Runnable serverRunnable = new Server(clientSocket);
            Thread serverThread = new Thread(serverRunnable);
            serverThread.start();
        }
    }

    public static class Server implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public Server(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                initializeServer();
                session();
                enterLobby();
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void initializeServer() throws IOException {
            System.out.println("Initializing the server...");
            // Initialize the socket reader and writer
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        }

        public void close() throws IOException {
            System.out.println("Closing the server...");
            in.close();
            out.close();
            clientSocket.close();
        }

        public void session() throws IOException {
            System.out.println("Session stage...");
            // Read data from the client and send a response
            String inputLine = in.readLine();
            System.out.println("Received from client: " + inputLine);
            out.println("SESS 1 1");
            System.out.println("The end of the session stage...");
        }

        public void enterLobby() throws IOException {
            System.out.println("Waiting for client actions...");
            // Read data from the client and send a response according to the request
            String inputLine = in.readLine();
            System.out.println("Client action: " + inputLine);
            String[] actions = inputLine.split(" ");
            String action = actions[0];
            if (action.equals("LIST")) {
                System.out.println("Listing all available games...");
            } else if (action.equals("CREA")) {
                System.out.println("Creating new game...");
            } else {
                System.out.print("Unknown command");
            }
        }
    }
}