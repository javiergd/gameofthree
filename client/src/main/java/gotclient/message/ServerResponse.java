package gotclient.message;

import lombok.Getter;

@Getter
public class ServerResponse {

  public static final String RESPONSE_FIELD_SEPARATOR = ",";
  public static final String KEY_VALUE_SEPARATOR = ":";

  private int playerId;
  private int playerResponse;
  private int divisionResult;
  private String gameState;

  private ServerResponse() {}

  public static ServerResponse fromServerInput(final String serverInput) {
    String[] responseFields = serverInput.split(RESPONSE_FIELD_SEPARATOR);

    String[] playerIdKeyValue = responseFields[0].split(KEY_VALUE_SEPARATOR);
    String[] playerResponseKeyValue = responseFields[1].split(KEY_VALUE_SEPARATOR);
    String[] divisionResultKeyValue = responseFields[2].split(KEY_VALUE_SEPARATOR);
    String[] gameStateKeyValue = responseFields[3].split(KEY_VALUE_SEPARATOR);

    ServerResponse serverResponse = new ServerResponse();
    serverResponse.playerId = Integer.parseInt(playerIdKeyValue[1].trim());
    serverResponse.playerResponse = Integer.parseInt(playerResponseKeyValue[1].trim());
    serverResponse.divisionResult = Integer.parseInt(divisionResultKeyValue[1].trim());
    serverResponse.gameState = gameStateKeyValue[1].trim();

    return serverResponse;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[")
      .append("Player Id: ").append(playerId).append(" || ")
      .append("Player response: ").append(playerResponse).append(" || ")
      .append("Division result: ").append(divisionResult).append(" || ")
      .append("Game state: ").append(gameState)
    .append("]");

    return sb.toString();
  }

}
