package gotclient;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
  private static final Logger logger = Logger.getLogger(GameClient.class);

  public static final int DEFAULT_PORT = 54321;
  public static final String DEFAULT_HOST = "localhost";

  public static void main(String[] args) {
    handleConnection();
  }

  private static void handleConnection() {
    try (
      Socket clientSocket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
      PrintWriter out =
        new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in =
        new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      BufferedReader stdIn =
        new BufferedReader(
          new InputStreamReader(System.in))
    ) {
      String fromUser;
      String fromServer = in.readLine();
      System.out.println(fromServer); // Greeting from the server

      int playerId = Integer.parseInt(fromServer.split(":")[1]);
      System.out.println("I am player: " + playerId);
      boolean myTurn = playerId == 1;

      while(true) {
        if (myTurn) {
          System.out.print("Enter a message: ");
          fromUser = stdIn.readLine();

          if (fromUser.equals("exit")) {
            out.println("exit");
            break;
          }

          out.println(fromUser);
          myTurn = false;
        } else {
          System.out.println("Waiting for server input");
          fromServer = in.readLine();
          if (fromServer.equals("exit")) break;

          System.out.println("Server says: " + fromServer);
          myTurn = true;
        }
      }
    }  catch (IOException e) {
      logger.error("Couldn't get I/O for the connection to localhost");
      System.exit(1);
    }
  }

}
