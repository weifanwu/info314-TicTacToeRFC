import java.io.*;
import java.io.BufferedReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class TTTServer {

  static ServerSocket TCPserverSocket;
  static DatagramSocket UDPSocket;

  static ExecutorService exec = Executors.newCachedThreadPool();

  static List<Board> games;
  static int clientNumber;
  static int gameNubmer;

  // private static Map<String, ClientConnection> clientConnections = new
  // HashMap<>(); // maps clientIds to clientConnections

  public static void main(String[] args) {
    try {
      games = new ArrayList<>();
      clientNumber = 1;
      gameNubmer = 1;
      TCPserverSocket = new ServerSocket(3116);
      UDPSocket = new DatagramSocket(3116);
      exec = Executors.newFixedThreadPool(20);
      exec.execute(TTTServer::TCPService);
      exec.execute(TTTServer::UDPService);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void TCPService() {

    while (true) {
      try {
        Socket clientSocket = TCPserverSocket.accept(); 
        exec.execute(() -> {
          try {
            while (true) {
              CombinedSocket connection = new CombinedSocket(clientSocket);

              // String request =
              String inputLine = connection.in.readLine();
              System.out.println("client in");
              handleRequest(connection, inputLine);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // private static void UDPService() {    
  // while (true) {
  // try {
  // byte[] buffer = new byte[1024];
  // DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
  // UDPSocket.receive(packet);

  // System.out.println("client in");

  // String request = new String(packet.getData(), 0, packet.getLength());
  // InetAddress IPAddress = packet.getAddress();
  // int port = packet.getPort();
  // DatagramSocket UDPSocket;

  // exec.execute(() -> handleClientRequest(new CombinedSocket(UDPSocket,
  // IPAddress, port), request));
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }
  // }

  private void handleRequest(CombinedSocket connection, String inputLine) {
    String[] actions = inputLine.split(" ");
    String action = actions[0];
    
    if (action.equals("HELO")) {
      String result = handleHELORequest(actions, connection);
      
    } else if (action.equals("LIST")) {
      String result = handleLISTRequest(actions);

    } else if (action.equals("CREA")) {
      String result = handleCREARequest(actions, connection);

    } else if (action.equals("JOIN")) {
      String result = handleJOINRequest(actions, connection);

    } else if (action.equals("STAT")) {
      String result = handleSTATRequest(actions, connection);

    } else if (action.equals("MOVE")) {
      String moveResponse = handleMOVERequest(actions, connection);

    } else if (action.equals("GDBY")) {
      String gdbyResponse = handleGDBYRequest(actions, connection);

    } else if (action.equals("QUIT")) {
      boolean quitSuccess = handleQUITRequest(actions, connection);
      
    } else {
      
    }
  }

  private String handleHELORequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleLISTRequest(String[] actions) {
  
  }

  private String handleCREARequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleJOINRequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleSTATRequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleMOVERequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleGDBYRequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleQUITRequest(String[] actions, CombinedSocket connection) {
  
  }

  private String handleYRMVRequest(String[] actions, CombinedSocket connection) {
  
  }

}
