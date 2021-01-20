package gotclient.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServerResponseHandlerTest {

  @Test
  public void should_build_server_response_from_full_string() {
    String inputString = "{\"gameState\":1,"
      + "\"playerId\":1,"
      + "\"playerResponse\":3,"
      + "\"divisionResult\":1,"
      + "\"gameFinished\":true,"
      + "\"winnerId\":-1}";

    ServerResponseHandler responseHandler = new ServerResponseHandler();
    responseHandler.updateResponse(inputString);

    assertThat(responseHandler.getPlayerId()).isEqualTo(1);
    assertThat(responseHandler.getDivisionResult()).isEqualTo(1);
  }

  @Test
  public void should_build_server_response_from_partial_string() {
    String inputString = "{\"gameState\":1,"
      + "\"playerId\":1,"
      + "\"playerResponse\":3}";

    ServerResponseHandler responseHandler = new ServerResponseHandler();
    responseHandler.updateResponse(inputString);
    System.out.println(responseHandler.getResponseString());

    assertThat(responseHandler.getPlayerId()).isEqualTo(1);
  }

}