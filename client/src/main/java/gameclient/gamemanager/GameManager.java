package gameclient.gamemanager;

import gameclient.message.ServerResponseHandler;
import gameclient.message.input.UserInputHandler;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Controller class for the game mechanics on the client side of the Game Of Three.
 * The purpose of this class is to monitor the state of the player variables which
 * are updated after every game move.
 * Input from the client side is delegated to the {@link UserInputHandler}
 * Messages received from the server are managed by the {@link ServerResponseHandler}
 */
@Getter
public class GameManager {

  private static final Logger logger = Logger.getLogger(GameManager.class);

  // Game mode
  public static final int MANUAL = 0;
  public static final int AUTO = 1;

  // Game state
  private static final int GAME_NOT_STARTED = 0;
  private static final int GAME_ONGOING = 1;
  private static final int GAME_FINISHED = 2;

  // Player fields
  private int playerId;
  private int currentGameNumber;
  private int turnCount;
  private int winnerId;
  private int gameMode;
  private boolean isGameFinished;
  private boolean isThisPlayerTurn;

  // Request/Response handling
  private final UserInputHandler inputHandler;
  private final ServerResponseHandler responseHandler;

  // I/O handling
  private final BufferedReader serverReader;
  private final BufferedReader userReader;
  private final PrintWriter serverWriter;

  public GameManager(
    BufferedReader serverReader,
    PrintWriter serverWriter,
    BufferedReader userReader) {
    this.serverReader = serverReader;
    this.serverWriter = serverWriter;
    this.userReader = userReader;
    this.inputHandler = new UserInputHandler(userReader);
    this.responseHandler = new ServerResponseHandler();
  }

  public void processResponse() throws IOException {
    responseHandler.updateResponse(serverReader.readLine());

    switch (responseHandler.getGameState()) {
      case GAME_NOT_STARTED:
        initPlayerVariables();
        break;
      case GAME_ONGOING:
        System.out.println("Received: " + responseHandler.getResponseString());
        this.currentGameNumber = responseHandler.getDivisionResult();
        break;
      case GAME_FINISHED:
        System.out.println("Received finishing message: " + responseHandler.getResponseString());
        this.isGameFinished = true;
        this.currentGameNumber = responseHandler.getDivisionResult();
        winnerId = responseHandler.getWinnerId();
        break;
      default:
        logger.error("Unrecognized game state: " + responseHandler.getGameState());
    }
  }


  public int getNumberFromPlayer() {
    return inputHandler.getValidNumber(turnCount == 0, currentGameNumber, gameMode);
  }

  public void selectMode() {
    this.gameMode = inputHandler.selectInputMode();
  }

  public void sendMove(int userNumber) {
    this.currentGameNumber += userNumber;
    String response = GAME_ONGOING + "," + userNumber + "," + currentGameNumber;
    this.serverWriter.println(response);
  }

  public void sendGameEnded() {
    String response = GAME_FINISHED + "," + responseHandler.getWinnerId() + "," + currentGameNumber;
    this.serverWriter.println(response);
  }

  public void updateTurn() {
    this.isThisPlayerTurn = !this.isThisPlayerTurn;
    this.turnCount++;
  }

  private void initPlayerVariables() {
    this.playerId = responseHandler.getPlayerId();
    this.isThisPlayerTurn = playerId == 1;
    this.turnCount = isThisPlayerTurn ? 0 : 1;
    this.currentGameNumber = 0;
    this.isGameFinished = false;
  }
}
