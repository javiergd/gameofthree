package gameserver.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class handles the incoming messages for the client and builds an appropriate response
 * based on the message content. It also stores this response and provides a convenient way
 * for the server to forward it as a string.
 */
public class ClientResponseHandler {

  private static final int GAME_FINISHED = 2;

  private static final int GAME_STATE_FIELD = 0;
  private static final int PLAYER_RESPONSE_FIELD = 1;
  private static final int CURRENT_GAME_FIELD = 2;

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
    int gameState = Integer.parseInt(stateAndCurrentNumber[GAME_STATE_FIELD]);
    int playerResponse = Integer.parseInt(stateAndCurrentNumber[PLAYER_RESPONSE_FIELD]);
    int currentGameNumber = Integer.parseInt(stateAndCurrentNumber[CURRENT_GAME_FIELD]);

    ClientResponseJson response;
    if (gameState == GAME_FINISHED) {
      response = buildEndOfGameResponse(fromId, playerResponse);
    } else {
      response = buildInGameResponse(gameState, fromId, playerResponse, currentGameNumber,
        isFirstTurn);
    }
    this.clientResponse = response;
  }

  public String buildResponseString() throws JsonProcessingException {
    return objectMapper.writeValueAsString(clientResponse);
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
      .previousNumber(currentGameNumber - playerResponse)
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
