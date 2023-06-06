import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Weifan {
    static ClientSockets communicate;
    static String client_id;
    static String session_id;
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
            // ??? what is this
        } else if (parts[0].equals("t3tcp")) {
            Socket socket = new Socket("localhost", 3116);
            communicate = new ClientSockets(socket);
            // handle other ports
        } else {
            throw new Exception("The protocol that is given is not legal...");
        }
        session();
        System.out
                .print("You want to join a game(join), create a new game(create) or check the stat of a game(stat): ");
        String action = scanner.next();
        if (action.equals("create")) {
            create();
        } else if (action.equals("join")) {
            list();
        } else {
            // stat();
        }
        move();
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
        session_id = numbers[numbers.length - 1];
        System.out.println("This is your session ID: " + client_id);
    }

    public static void create() throws Exception {
        System.out.println("Starting to create a new game: ");
        communicate.send("CREA " + client_id);
        String cmd = "";
        String[] info = {};
        while (!cmd.equals("JOND")) {
            info = communicate.receive().split(" ");
            cmd = info[0];
        }
        game_id = info[2];
        System.out.print("Wait the other user...");
    }

    public static void list() throws Exception {
        communicate.send("LIST");
        System.out.print("This is all games that are available: ");
        String cmd = "";
        String[] info = {};
        while (!cmd.equals("GAMS")) {
            info = communicate.receive().split(" ");

            cmd = info[0];
            System.out.println(cmd);
        }
        for (int i = 1; i < info.length; i++) {
            System.out.println("Game:" + info[i]);
        }
        scanner = new Scanner(System.in);
        System.out.print("Select a game: ");
        String game = scanner.next();
        communicate.send("JOIN " + game.trim());
        String current = communicate.receive();
        System.out.println("Status: " + current);
    }

    public static void move() throws Exception {
        System.out.print("I am so exited, let's start the game!");
        while (true) {
            String board = communicate.receive();

            if (board.split(" ")[0].equals("BORD")) {
                System.out.println("This is the board: \n" + board.split(" ")[5].substring(0, 6) + "|\n"
                        + board.split(" ")[5].substring(6, 12) + "|\n" + board.split(" ")[5].substring(12, 19));
            } else if (board.split(" ")[0].equals("TERM")) {
                System.out.println("Player " + board.split(" ")[2] + "won!!!");
            }

            String[] message = board.split(" ");
            if (message[0].equals("VRMV") || (message[0].equals("BORD") && message[4].equals(client_id))) {
                if (message[2].equals(client_id)) {
                    String cmd = "";
                    String[] info = {};
                    while (!cmd.equals("BORD")) {
                        info = communicate.receive().split(" ");

                        cmd = info[0];
                    }
                    System.out.println("This is the board: \n" + info[5].substring(0, 6) + "|\n"
                            + info[5].substring(6, 12) + "|\n" + info[5].substring(12, 19));
                    System.out.println("Please make you move: ");
                    String move = scanner.next();
                    communicate.send("MOVE " + move);
                } else {

                    System.out.println("Please wait for the other play to make the move!");
                }
            }
        }
    }
}
