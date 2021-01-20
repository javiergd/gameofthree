package gameclient;

import gameclient.gamemanager.GameManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Entry point for the client in Game of Three. This class handles the communication
 * with the game server and exchanges messages until the game ends.
 */
public class GameClient {
  public static final int DEFAULT_PORT = 54321;
  public static final String DEFAULT_HOST = "localhost";

  private static final Logger logger = Logger.getLogger(GameClient.class);

  private int port;

  public static void main(String[] args) {
    GameClient client = new GameClient(args);
    client.handleConnection();
  }

  private GameClient(String[] args) {
    this.port = getPortFromArgsOrDefault(args);
  }

  private void handleConnection() {
    try (
      Socket clientSocket = new Socket(DEFAULT_HOST, port);
      PrintWriter writer =
        new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader reader =
        new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      BufferedReader stdIn =
        new BufferedReader(
          new InputStreamReader(System.in))
    ) {
      // Pre-game actions
      GameManager gameManager = new GameManager(reader, writer, stdIn);
      gameManager.processResponse();
      gameManager.selectMode();

      // In-game actions
      while(!gameManager.isGameFinished()) {
        if (gameManager.isThisPlayerTurn()) {
          int userNumber = gameManager.getNumberFromPlayer();
          gameManager.sendMove(userNumber);
        } else {
          System.out.println("Waiting for server input...");
          gameManager.processResponse();
        }
        gameManager.updateTurn();
      }

      // Post game completion actions
      System.out.print("Game ended! - ");
      if (gameManager.getWinnerId() == gameManager.getPlayerId()) {
        System.out.println("You win! :)");
      } else {
        gameManager.sendGameEnded();
        System.out.println("You lose! :(");
      }

    }  catch (IOException e) {
      logger.error("Couldn't get I/O for the connection to " + DEFAULT_HOST);
      System.exit(1);
    }
  }

  private int getPortFromArgsOrDefault(String[] args) {
    if (args.length == 1) {
      try {
        return Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.out.println("Invalid port number supplied. Using port " + DEFAULT_PORT + " for connections");
        return DEFAULT_PORT;
      }
    }
    else {
      System.out.println("No port supplied. Using port " + DEFAULT_PORT + " for connections");
      return DEFAULT_PORT;
    }
  }

}
