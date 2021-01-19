package gotclient.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;

@NoArgsConstructor
public class ServerResponseHandler {
  private final Logger logger = Logger.getLogger(ServerResponseHandler.class);

  private ServerResponseJson serverResponse;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void updateResponse(String inputMessage) {
    try {
      this.serverResponse = objectMapper.readValue(inputMessage, ServerResponseJson.class);
    } catch (JsonProcessingException e) {
      logger.error("Unable to process request from server: " + e);
    }
  }

  public int getPlayerId() {
    return serverResponse.getPlayerId();
  }

  public int getDivisionResult() {return serverResponse.getDivisionResult();}

  public boolean isGameFinished() {
    return serverResponse.isGameFinished();
  }

  public int getWinnerId() {return serverResponse.getWinnerId();}

  public String getResponseString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[")
      .append("From player: ").append(serverResponse.getPlayerId()).append(" || ")
      .append("Response: ").append(serverResponse.getPlayerResponse()).append(" || ")
      .append("Current game number: ").append(serverResponse.getDivisionResult()).append(" || ")
      .append("Is game finished: ").append(serverResponse.isGameFinished())
      .append("]");

    return sb.toString();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  private static class ServerResponseJson {
    private int messageType;
    private int playerId;
    private int playerResponse;
    private int divisionResult;
    private boolean isGameFinished;
    private int winnerId;
  }

}
