package gameserver.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServerResponseTest {

  @Test
  public void should_build_server_response_from_message_and_player_id() {
    String message = "3";
    int playerId = 2;
    boolean isFirstTurn = false;

    ServerResponse response = new ServerResponse(playerId, message, isFirstTurn);

    assertThat(response.getPlayerId()).isEqualTo(playerId);
    assertThat(response.getPlayerResponse()).isEqualTo(3);
    assertThat(response.getDivisionResult()).isEqualTo(1);
    assertThat(response.getGameState()).isEqualTo("finished");

    System.out.println(response);
  }

}