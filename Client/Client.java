import java.io.*;
import java.net.*;
import java.util.*;


public class Client {
    static ClientSockets communicate;
    static String client_id;
    static String game_id;
    static Scanner scanner;
    public static void main(String[] args) throws Exception {
        
        scanner = new Scanner(System.in);
        String input = args[0];
        String[] parts = input.split("://|:");

        
        if (parts[0].equals("t3udp")) {
            DatagramSocket socket = new DatagramSocket();
            communicate = new ClientSockets(socket, InetAddress.getByName("localhost"), 3116);
            communicate.send("Connection...");
        } else if (parts[0].equals("t3tcp")) {
            Socket socket = new Socket("localhost", 3116);
            communicate = new ClientSockets(socket);
        } else {
            throw new Exception("The protocol that is given is not legal...");
        }
        session();
        System.out.print("You want to join a game or create a new game: ");
        String action = scanner.next();
        if (action.equals("create")) {
            create();
        } else {
            list();
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
        System.out.println("This is your client ID: " + client_id);
    }

    public static void create() throws Exception {
        System.out.println("Starting to create a new game: ");
        communicate.send("CREA " + client_id);
        String current = communicate.receive();
        System.out.println("This is the current " + current);
        String[] game = current.split(" ");
        game_id = game[2];
        System.out.println("Status: " + current);
        System.out.print("Wait the other user...");
    }

    public static void list() throws Exception {
        System.out.print("This is all games that are available: ");
        communicate.send("LIST");
        String games = communicate.receive();
        int length = games.length();
        System.out.println(games.substring(5, length));
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
            String[] message = board.split(" ");
            if (message[0].equals("VRMV")) {
                if (message[2].equals(client_id)) {
                    String board_message = communicate.receive();
                    System.out.println("This is the board: " + board_message);
                    System.out.println("Please make you move: ");
                    String move = scanner.nextLine();
                    communicate.send("MOVE " + game_id + " " + move);
                } else {
                    System.out.println("Please wait for the other play to make the move!");
                }
            } else if (message[0].equals("TERM")) {
                System.out.println("This is the winnner: " + message[2]);
                System.out.println("The end of the game...");
            }
        }
    }
}