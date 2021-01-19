package gotclient.gamemanager;

import gotclient.message.ClientRequestHandler;
import gotclient.message.ServerResponseHandler;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Controller class for the game mechanics on the client side of the Game Of Three.
 * The purpose of this class is to monitor the state of the player variables which
 * are updated after every game move.
 * Input from the server is delegated to the @link{ClientRequestHandler}
 */
@Getter
public class GameManager {

  // Player bound
  private int playerId;
  private int currentGameValue;
  private int turnCount;
  private int winnerId;
  private boolean isGameFinished;
  private boolean isThisPlayerTurn;


  // Request/Response handling
  private final ClientRequestHandler requestHandler;
  private final ServerResponseHandler responseHandler;

  // I/O handling
  private final BufferedReader serverReader;
  private final BufferedReader userReader;
  private final PrintWriter serverWriter;


  public GameManager(BufferedReader serverReader,
                     PrintWriter serverWriter,
                     BufferedReader userReader) {
    this.serverReader = serverReader;
    this.serverWriter = serverWriter;
    this.userReader = userReader;
    this.requestHandler = new ClientRequestHandler(userReader);
    this.responseHandler = new ServerResponseHandler();
  }

  public void initPlayerVariables() throws IOException {
    responseHandler.updateResponse(serverReader.readLine());
    System.out.println(responseHandler.getResponseString()); // Greeting from the server

    this.playerId = responseHandler.getPlayerId();
    this.isThisPlayerTurn = playerId == 1;
    this.turnCount = isThisPlayerTurn ? 0 : 1;
    this.currentGameValue = 0;
    this.isGameFinished = false;
  }

  public void updateTurn() {
    this.isThisPlayerTurn = !this.isThisPlayerTurn;
    this.turnCount++;
  }

  public int getNumberFromPlayer() {
    return requestHandler.getValidNumber(turnCount == 0);
  }

  public void sendMove(int userNumber) {
    this.currentGameValue += userNumber;
    String response = "1," + currentGameValue;
    this.serverWriter.println(response);
  }

  public void sendGameEnded() {
    String response = "2," + responseHandler.getWinnerId();
    this.serverWriter.println(response);
  }

  public void processResponse(String serverInput) {
    responseHandler.updateResponse(serverInput);
    System.out.println("Received: " + responseHandler.getResponseString());
    this.isGameFinished = responseHandler.isGameFinished();
    this.currentGameValue = responseHandler.getDivisionResult();
    if (isGameFinished) {
      winnerId = responseHandler.getWinnerId();
    }
  }
}
