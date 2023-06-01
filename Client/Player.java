import java.io.*;
import java.net.*;
import java.util.*;

public class Player {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3116);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your User Name: ");
        String client = scanner.next();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String message = "HELO " + 1 + " " + client;
        System.out.println("Sending the Greeting messages to the server: " + message);
        out.println(message);
        String serverResponse = in.readLine();
        String[] numbers = serverResponse.split(" ");
        String client_id = numbers[numbers.length - 1];
        System.out.println("This is your client ID: " + client_id);
        System.out.print("You want to join a game or create a new game: ");
        String action = scanner.next();
        if (action.equals("create")) {
            System.out.print("Starting to create a new game: ");
            out.println("CREA " + client_id);
        } else {
            System.out.print("This is all games that are available: ");
            out.println("LIST");
        }
        in.close();
        out.close();
        socket.close();
    }
}