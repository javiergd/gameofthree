package gameserver;

import gameserver.message.ClientResponseHandler;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameServerThread extends Thread {

  private static final Logger logger = Logger.getLogger(GameServerThread.class);

  private final int playerId;
  private final int opponentId;
  private final Socket socket;
  private final GameServer server;

  private PrintWriter out;

  public GameServerThread(Socket socket, GameServer server, final int playerId) {
    super("GameServerThread - " + playerId);
    this.socket = socket;
    this.server = server;
    this.playerId = playerId;
    this.opponentId = playerId == 1 ? 2 : 1;
    try {
      this.out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      logger.error("Could not get the print writer for the socket");
    }
  }

  @Override
  public void run() {
    try (
      BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    ) {
      // Pre-game action - send welcome message
      ClientResponseHandler responseHandler = new ClientResponseHandler(0, playerId);
      sendMessage(responseHandler.buildResponseString());

      // In-game: turn-based message exchange
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
          server.forwardMessage(inputLine, playerId, opponentId);
      }
    } catch (IOException e) {
      logger.error("Exception caught: " + e);
    } finally {
      // Post-game: cleanup
      out.close();
      try {
        socket.close();
      } catch (IOException e) {
        logger.error("Exception trying to close GameServerThread socket: " + e);
      }
    }
  }

  protected void sendMessage(String message) {
    out.println(message);
  }
}
