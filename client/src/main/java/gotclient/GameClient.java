package gotclient;

import gotclient.message.ClientRequestHandler;
import gotclient.message.ServerResponse;
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
    GameClient client = new GameClient();
    client.handleConnection();
  }

  private void handleConnection() {
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
      String fromServer = in.readLine();
      System.out.println(fromServer); // Greeting from the server

      int playerId = Integer.parseInt(fromServer.split(":")[1]);
      System.out.println("I am player: " + playerId);

      boolean myTurn = playerId == 1;
      int turnCounter = myTurn ? 0 : 1;
      ClientRequestHandler clientRequestHandler = new ClientRequestHandler(stdIn);
      int currentNumber = 0;

      while(true) {
        if (myTurn) {
          int userNumber = clientRequestHandler.getValidNumber(turnCounter == 0);
          out.println(currentNumber + userNumber);
          turnCounter++;
          myTurn = false;
        } else {
          System.out.println("Waiting for server input...");

          ServerResponse serverResponse = ServerResponse.fromServerInput(in.readLine());

          if (serverResponse.getGameState().equals("finished")) {
            // TODO notify other player of the result
            break;
          }

          System.out.println("From: " + serverResponse);
          myTurn = true;
          currentNumber = serverResponse.getDivisionResult();
        }
      }
    }  catch (IOException e) {
      logger.error("Couldn't get I/O for the connection to localhost");
      System.exit(1);
    }
  }

}
