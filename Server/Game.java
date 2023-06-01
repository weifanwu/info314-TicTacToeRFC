import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    static List<Board> games;
    static int clientNumber;
    static int gameNubmer;
    public static void main(String[] args) throws IOException {
        games = new ArrayList<>();
        clientNumber = 1;
        gameNubmer = 1;
        ServerSocket serverSocket = new ServerSocket(3116);
        System.out.println("Server started on port 3116...");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();               
                System.out.println("");
                System.out.println("Server socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

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
        private String player;
        private Board ttt_game;
        public Server(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {
            try {
                initializeServer();
                session();
                enterLobby();
                game();
                close();
            } catch (Exception e) {
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
            this.player = "" + clientNumber;
            clientNumber++;
            out.println("SESS 1 " + player);
            System.out.println("The end of the session stage...");
        }

        public void enterLobby() throws IOException {
            try {
                System.out.println("This is the Player: " + this.player);
                System.out.println("Waiting for client actions...");
                // Read data from the client and send a response according to the request
                String inputLine = in.readLine();
                System.out.println("Client action: " + inputLine);
                String[] actions = inputLine.split(" ");
                String action = actions[0];
                if (action.equals("LIST")) {
                    System.out.println("Listing all available games...");
                    gameList();
                } else if (action.equals("CREA")) {
                    create();
                } else {
                    System.out.print("Unknown command");
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void create() {
            System.out.println("Creating new game...");
            Board game = new Board(this.player, "Game" + gameNubmer);
            gameNubmer++;
            ttt_game = game;
            games.add(game);
        }

        public void gameList() throws IOException {
            String list_message = "GAMS";
            for (Board game : games) {
                System.out.println("Name" + game.getName());
                list_message = list_message + " " + game.getName();
                System.out.println(list_message);
            }
            System.out.println("list_message" + list_message);
            out.println(list_message);
            System.out.println("Send the message: " + list_message);
            System.out.println("Waiting to make a selection...");
            String join_game = in.readLine();
            System.out.println("Name of the join_game: " + join_game);
            String gameName = join_game.split(" ")[1];
            System.out.println("Name of the selected game: " + gameName);
            for (Board game : games) {
                if (game.getName().equals(gameName)) {
                    System.out.println("Board: " + game.getName());
                    this.ttt_game = game;
                    ttt_game.join(player);
                    break;
                }
            }
        }

        public void game() throws Exception {
            while (!ttt_game.getStart()) {
                Thread.sleep(500);
            }
            String status = "It is " + ttt_game.getTurn() + " 's turn";
            out.println(status);
        }
    }
}