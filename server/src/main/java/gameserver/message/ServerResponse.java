package gameserver.message;

import lombok.Getter;

@Getter
public class ServerResponse {

  public static final String FINISHED_GAME_STATE = "finished";
  public static final String ONGOING_GAME_STATE = "ongoing";
  private final int fromId;
  private String gameState;
  private int playerResponse;
  private int divisionResult;

  public static ServerResponse buildResponse(final int fromId, final String message, final boolean isFirstTurn) {
    if (message.contains(FINISHED_GAME_STATE)) {
      return new ServerResponse(fromId, message);
    } else {
      return new ServerResponse(fromId, message, isFirstTurn);
    }
  }

  private ServerResponse(final int fromId, final String message, final boolean isFirstTurn) {
    this.fromId = fromId;
    this.playerResponse = Integer.parseInt(message);
    this.divisionResult = isFirstTurn ? playerResponse : playerResponse / 3;
    this.gameState = divisionResult == 1 ? FINISHED_GAME_STATE : ONGOING_GAME_STATE;
    if (divisionResult == 1) {
      this.gameState += " - Player " + fromId  + " wins the game!";
    }
  }

  private ServerResponse(final int fromId, final String message) {
    this.fromId = fromId;
    this.gameState = message;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("PlayerId: ").append(fromId).append(", ")
      .append("Player response: ").append(playerResponse).append(", ")
      .append("Division result: ").append(divisionResult).append(", ")
      .append("Game state: ").append(gameState);

    return sb.toString();
  }
}
