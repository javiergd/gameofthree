package gotclient;

import gotclient.gamemanager.GameManager;
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
      gameManager.initPlayerVariables();

      // In-game actions
      while(!gameManager.isGameFinished()) {
        if (gameManager.isThisPlayerTurn()) {
          int userNumber = gameManager.getNumberFromPlayer();
          gameManager.sendMove(userNumber);
        } else {
          System.out.println("Waiting for server input...");
          gameManager.processResponse(reader.readLine());
        }
        gameManager.updateTurn();
      }

      // Post game completion actions
      gameManager.sendGameEnded();
      System.out.println("Game ended! - Winner is player: " + gameManager.getWinnerId());

    }  catch (IOException e) {
      logger.error("Couldn't get I/O for the connection to localhost");
      System.exit(1);
    }
  }

}
