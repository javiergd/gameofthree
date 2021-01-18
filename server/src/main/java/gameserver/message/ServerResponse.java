package gameserver.message;

import lombok.Getter;

@Getter
public class ServerResponse {

  private int playerId;
  private int playerResponse;
  private int divisionResult;
  private String gameState;

  public ServerResponse(final int playerId, final String message, final boolean isFirstTurn) {
    this.playerId = playerId;
    this.playerResponse = Integer.parseInt(message);
    this.divisionResult = isFirstTurn ? playerResponse : playerResponse / 3;
    this.gameState = divisionResult == 1 ? "finished" : "ongoing";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("PlayerId: ").append(playerId).append(", ")
      .append("Player response: ").append(playerResponse).append(", ")
      .append("Division result: ").append(divisionResult).append(", ")
      .append("Game state: ").append(gameState);
    return sb.toString();
  }
}
