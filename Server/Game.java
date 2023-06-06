import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// TODO: how can we know wether user uses TCP or UDP?
// NOTE THAT ALL BODY MESSAGE NEED TO END WITH A \n
public class Game {
    static List<Board> games;
    static int clientNumber;
    static int gameNubmer;
    static Thread tcpThread;
    static Thread udpThread;
    // clientID should reflect their user name

    public static void main(String[] args) throws IOException {
        games = new ArrayList<>();
        clientNumber = 1;
        gameNubmer = 1;
        tcpThread = new Thread(new TCPServerThread());
        udpThread = new Thread(new UDPServerThread());

        tcpThread.start();
        udpThread.start();

    }

    static class TCPServerThread implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket tcpServerSocket = new ServerSocket(3116);
                System.out.println("TCP Server listening...");

                while (true) {
                    System.out.println("Waiting for the TCP connection request");
                    Socket clientSocket = tcpServerSocket.accept();
                    // Handle TCP connection logic here
                    CombinedSocket communicate = new CombinedSocket(clientSocket);
                    Runnable serverRunnable = new Server(communicate);
                    Thread serverThread = new Thread(serverRunnable);
                    serverThread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class UDPServerThread implements Runnable {
        @Override
        public void run() {
            try {
                DatagramSocket udpSocket = new DatagramSocket(3116);
                System.out.println("UDP Server listening...");

                CombinedSocket communicate = new CombinedSocket(udpSocket, InetAddress.getByName("localhost"), 3116);
                System.out.println("Waiting for the UDP connection request");
                communicate.receive();
                Runnable serverRunnable = new Server(communicate);
                Thread serverThread = new Thread(serverRunnable);
                serverThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Server implements Runnable {
        private int playerID;
        private Board ttt_game;
        private CombinedSocket communicate;

        public Server(CombinedSocket communicate) {
            this.communicate = communicate;
        }

        public void run() {
            try {
                session();
                enterLobby();
                game();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void session() throws Exception {
            System.out.println("Session stage...");
            // Read data from the client and a response
            String[] cmd = new String[] { "1", "2", "1" };
            String action = "";
            while (!action.equals("HELO")) {
                cmd = communicate.receive().split(" ");
                action = cmd[0];
            }
            System.out.println("Received from client: " + cmd[2]);
            this.playerID = clientNumber;
            clientNumber++;
            communicate.send("SESS " + cmd[1] + " " + playerID + "\n");
            System.out.println("The end of the session stage...");
        }

        public void enterLobby() throws Exception {
            try {
                System.out.println("This is the Player: " + this.playerID);
                System.out.println("Waiting for client actions...");
                // Read data from the client and send a response according to the request

                String cmd = "";
                String[] info = {};
                while (!cmd.equals("HELO") && !cmd.equals("CREA") && !cmd.equals("LIST") && !cmd.equals("JOIN")
                        && !cmd.equals("STAT")
                        && !cmd.equals("GDBY")) {
                    info = communicate.receive().split(" ");
                    cmd = info[0];
                }
                System.out.println("Valid input received: " + cmd);

                System.out.println("info length " + info.length);

                if (cmd.equals("LIST")) {
                    System.out.println("Listing all available games...");
                    if (info.length > 1) {
                        list(info[1]);
                        join();
                    } else {
                        list();
                        join();
                    }
                } else if (cmd.equals("CREA")) {
                    System.out.println("now creating a new game");
                    create(info[1].trim());
                } else if (cmd.equals("JOIN")) {
                    join();
                } else if (cmd.equals("GDBY")) {
                    GDBY(playerID);
                } else {
                    communicate.send(STAT(info[1]));
                    // System.out.print("Unknown command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void create(String clientID) throws Exception {
            System.out.println("Creating new game...");
            Board game = new Board(Integer.parseInt(clientID), gameNubmer, communicate);
            gameNubmer++;
            this.ttt_game = game;
            games.add(game);
            String result = "JOND " + clientID + " " + ttt_game.gameID + "\n";

            communicate.send(result);
            System.out.println("Create msg sent: " + result);

        }

        public void list() throws Exception {
            String list_message = "GAMS";
            for (Board game : games) {
                if (!game.start) {
                    list_message = list_message + " " + game.getName();
                }
            }
            communicate.send(list_message + "\n");
        }

        public void list(String code) throws Exception {
            if (!code.equals("CURR")) {
                String list_message = "GAMS";
                for (Board game : games) {
                    if (game.winnerID != 0) {
                        list_message = list_message + " " + game.getName();
                    }
                }
                communicate.send(list_message + "\n");
            } else if (!code.equals("ALL")) {
                String list_message = "GAMS";
                for (Board game : games) {
                    list_message = list_message + " " + game.getName();
                }
                communicate.send(list_message + "\n");
            } else {
                list();
            }

        }

        public void join() throws Exception {
            // System.out.println("Waiting to make a selection...");
            System.out.println("join started");
            String cmd = "";
            String[] info = {};
            while (!cmd.equals("JOIN")) {
                info = communicate.receive().split(" ");
                cmd = info[0];
            }
            System.out.println("Name of the join_game: " + info[1]);
            String gameName = info[1];
            System.out.println("Name of the selected game: " + gameName);
            for (Board game : games) {
                if (game.getName().equals(gameName)) {
                    System.out.println("Board: " + game.getName());
                    this.ttt_game = game;
                    ttt_game.join(playerID, communicate);
                    break;
                }
            }
            String result = "JOND " + playerID + " " + ttt_game.gameID + "\n";
            communicate.send(result);
            System.out.println("successfully joined");
        }

        public void game() throws Exception {
            // need to handle wrong input
            while (!ttt_game.getStart()) {
                Thread.sleep(100);
            }

            if (playerID == ttt_game.playerTwoID) {
                String result = "JOND " + ttt_game.playerTwoID + " " + ttt_game.gameID + "\n";
                communicate.send(result);
            }
            YRMV();
            while (ttt_game.winnerID == 0) {
                // handle repeated spot
                if (ttt_game.getTurn() == playerID) {
                    communicate.send(ttt_game.toString());
                    String inputLine = communicate.receive();
                    String[] actions = inputLine.split(" ");
                    String action = actions[0];
                    if (action.equals("MOVE")) {
                        String location = actions[1];
                        System.out.println("player trying to move with data:" + location);
                        if (location.contains(",")) {
                            // WHEN USER INPUT X, X server crash
                            // ACEEPT INPUT IS X,X
                            System.out.println("player entered x,x");
                            String[] index = location.split(",");
                            int x = Integer.parseInt(index[0].trim());
                            int y = Integer.parseInt(index[1]);
                            System.out.println("player input:" + x + y);
                            // need guide user to re-enter a value
                            if (0 < x && x < 4 && 0 < y && y < 4) {
                                if (ttt_game.move(x, y, playerID)) {
                                    communicate.send(ttt_game.toString());
                                    YRMV();
                                } else {
                                    communicate.send(ttt_game.toString());
                                }
                            } else {
                                communicate.send(ttt_game.toString());
                            }
                        } else {
                            if (0 < Integer.parseInt(location) && Integer.parseInt(location) < 10) {
                                System.out.println("player entered a linear");
                                System.out.println("player input:" + location);
                                if (ttt_game.move(Integer.parseInt(location), playerID)) {
                                    communicate.send(ttt_game.toString());
                                    YRMV();
                                } else {
                                    communicate.send(ttt_game.toString());
                                }
                            } else {
                                communicate.send(ttt_game.toString());
                            }
                        }
                    } else if (action.equals("GDBY")) {
                        GDBY(playerID);
                    } else if (action.equals("STAT")) {
                        communicate.send(STAT(actions[1]));
                    } else if (action.equals("QUIT")) {
                        QUIT(actions[1], playerID);
                    }
                } else {
                    Thread.sleep(1000);
                    if (ttt_game.getTurn() == playerID) {
                        YRMV();
                    }
                }
            }

            TERM(ttt_game.gameID);
        }

        public String STAT(String gameID) {
            for (Board game : games) {
                if (game.getName().equals(gameID)) {
                    // System.out.println("Board: " + game.getName());
                    return game.toString();
                }
            }
            return "not found";
        }

        public void GDBY(int playerID) throws Exception {

            QUIT(String.valueOf(ttt_game.gameID), playerID);
            tcpThread.interrupt();
            udpThread.interrupt();
        }

        public void QUIT(String gameID, int senderID) throws Exception {
            for (Board game : games) {
                if (game.getName().equals(gameID)) {
                    // System.out.println("Board: " + game.getName());
                    if (game.playerOneID == senderID) {
                        game.winnerID = game.playerTwoID;
                        game.start = false;
                    } else {
                        game.winnerID = game.playerOneID;
                        game.start = false;
                    }
                    break;
                }

            }
            // HOW CAN USER JOIN OTHER GAME?
        }

        public void TERM(int gameID) throws Exception {
            String result = "TERM ";
            result += gameID + " ";
            if (ttt_game.playerTwoID != 0) {
                result += ttt_game.winnerID + " ";
            }
            ttt_game.playerOne.send(result + "KTHXBYE" + "\n");
            ttt_game.playerTwo.send(result + "KTHXBYE" + "\n");
        }

        public void YRMV() throws Exception {
            String result = "YRMV ";
            result += ttt_game.gameID + " " + ttt_game.getTurn() + "\n";
            communicate.send(result);
        }

    }
}