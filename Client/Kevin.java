import java.io.*;
import java.net.*;
import java.util.*;

public class Kevin {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3116);
        ClientSockets communicate = new ClientSockets(socket);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your User Name: ");
        String client = scanner.next();

        String message = "HELO " + 1 + " " + client;
        System.out.println("Sending the Greeting messages to the server: " + message);
        communicate.send(message);
        String serverResponse = communicate.receive();
        String[] numbers = serverResponse.split(" ");
        String client_id = numbers[numbers.length - 1];
        System.out.println("This is your client ID: " + client_id);
        System.out.print("You want to join a game or create a new game: ");
        String action = scanner.next();
        if (action.equals("create")) {
            System.out.println("Starting to create a new game: ");
            communicate.send("CREA " + client_id);
            System.out.print("Wait the other user...");
            String current = communicate.receive();
            System.out.println("Status: " + current);
        } else {
            System.out.print("This is all games that are available: ");
            communicate.send("LIST");

            String games = communicate.receive();
            int length = games.length();
            System.out.println(games.substring(5, length));
            System.out.print("Select a game: ");
            String game = scanner.next();
            communicate.send("JOIN " + game.trim());
            String current = communicate.receive();
            System.out.println(current);
        }
        socket.close();
        scanner.close();
    }
}