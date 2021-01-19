package gameserver.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;

public class ClientResponseHandler {

  private static final Logger logger = Logger.getLogger(ClientResponseHandler.class);
  public static final int GAME_FINISHED = 2;

  private final ClientResponseJson clientResponse;
  private final ObjectMapper objectMapper = new ObjectMapper();


  public ClientResponseHandler(final int messageType, final int playerId) {
    this.clientResponse = ClientResponseJson.builder()
      .gameState(messageType)
      .playerId(playerId)
      .build();
  }

  public ClientResponseHandler(
    int fromId,
    String message,
    boolean isFirstTurn) {

    String[] stateAndCurrentNumber = message.split(",");
    int gameState = Integer.parseInt(stateAndCurrentNumber[0]);
    int playerResponse = Integer.parseInt(stateAndCurrentNumber[1]);
    int currentGameNumber = Integer.parseInt(stateAndCurrentNumber[2]);

    ClientResponseJson response;
    if (gameState == GAME_FINISHED) {
      response = buildEndOfGameResponse(fromId, playerResponse);
    } else {
      response = buildInGameResponse(gameState, fromId, playerResponse, currentGameNumber, isFirstTurn);
    }
    this.clientResponse = response;
  }

  public String buildResponseString() {
    try {
      return objectMapper.writeValueAsString(clientResponse);
    } catch (JsonProcessingException e) {
      logger.error("Unable to write response as string: " + e);
    }
    return "";
  }

  private ClientResponseJson buildEndOfGameResponse(
    final int playerId,
    final int playerResponse) {

    return ClientResponseJson.builder()
      .gameState(GAME_FINISHED)
      .playerId(playerId)
      .divisionResult(1)
      .isGameFinished(true)
      .winnerId(playerResponse)
      .build();
  }

  private ClientResponseJson buildInGameResponse(
    final int receivedState,
    final int playerId,
    final int playerResponse,
    final int currentGameNumber,
    final boolean isFirstTurn) {

    int divisionResult = isFirstTurn ? currentGameNumber : currentGameNumber / 3;
    boolean isGameFinished = divisionResult == 1;
    int gameState = isGameFinished ? GAME_FINISHED : receivedState;
    int winnerId = isGameFinished ? playerId : -1;

    return ClientResponseJson.builder()
      .gameState(gameState)
      .playerId(playerId)
      .previousNumber(currentGameNumber)
      .playerResponse(playerResponse)
      .divisionResult(divisionResult)
      .isGameFinished(isGameFinished)
      .winnerId(divisionResult == 1 ? playerId : winnerId)
      .build();
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  private static class ClientResponseJson {

    private int gameState;
    private int playerId;
    private int previousNumber;
    private int playerResponse;
    private int divisionResult;
    private boolean isGameFinished;
    private int winnerId;
  }
}
