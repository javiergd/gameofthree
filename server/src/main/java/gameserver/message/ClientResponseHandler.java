package gameserver.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ClientResponseHandler {

  private ClientResponseJson clientResponse;
  private final ObjectMapper objectMapper = new ObjectMapper();


  public ClientResponseHandler(final int messageType, final int playerId) {
    this.clientResponse = ClientResponseJson.builder()
      .messageType(messageType)
      .playerId(playerId).build();
  }

  public ClientResponseHandler(final int messageType, final int playerId, final int playerResponse) {
    this.clientResponse = ClientResponseJson.builder()
      .messageType(messageType)
      .playerId(playerId)
      .playerResponse(playerResponse)
      .divisionResult(playerResponse)
      .build();
  }

  public String buildResponseString() {
    try {
      return objectMapper.writeValueAsString(clientResponse);
    } catch (JsonProcessingException e) {
      // TODO add logger here
      System.out.println("Unable to write response as string");
      e.printStackTrace();
    }
    return ""; // TODO return here a message ended
  }

  public ClientResponseHandler(
    final int playerId,
    final String responseString,
    final boolean isFirstTurn) {

    String[] stateAndCurrentNumber = responseString.split(",");
    int messageType = Integer.parseInt(stateAndCurrentNumber[0]);
    int playerResponse = Integer.parseInt(stateAndCurrentNumber[1]);
    int divisionResult = isFirstTurn ? playerResponse : playerResponse / 3;
    boolean isGameFinished = divisionResult == 1 || messageType == 2;
    int winnerId = messageType == 2 ? playerResponse : -1;


    this.clientResponse = ClientResponseJson.builder()
      .messageType(messageType)
      .playerId(playerId)
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

    private int messageType;
    private int playerId;
    private int playerResponse;
    private int divisionResult;
    private boolean isGameFinished;
    private int winnerId;
  }
}
