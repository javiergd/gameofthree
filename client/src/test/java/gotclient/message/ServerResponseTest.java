package gotclient.message;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServerResponseTest {

  @Test
  public void should_build_server_response_from_string() {
    // Prepare
    String serverInput = "PlayerId: 2, "
      + "Player response: 3, "
      + "Division result: 1, "
      + "Game state: finished";

    // Act
    ServerResponse serverResponse = ServerResponse.fromServerInput(serverInput);

    // Assert
    assertThat(serverResponse.getPlayerId()).isEqualTo(2);
    assertThat(serverResponse.getPlayerResponse()).isEqualTo(3);
    assertThat(serverResponse.getDivisionResult()).isEqualTo(1);
    assertThat(serverResponse.getGameState()).isEqualTo("finished");
  }
}