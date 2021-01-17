package gameserver;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameServerThread extends Thread {

  private static final Logger logger = Logger.getLogger(GameServerThread.class);
  private final int playerId;
  private final Socket socket;
  private final GameServer server;

  private PrintWriter out;

  public GameServerThread(Socket socket, GameServer server, final int playerId) {
    super("GameServerThread");
    this.socket = socket;
    this.server = server;
    this.playerId = playerId;
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
      String inputLine;

      out.println("Player id:" + playerId);

      while ((inputLine = in.readLine()) != null) {
        if (playerId == 1)
          server.forwardMessage(inputLine, 2);
        else {
          server.forwardMessage(inputLine, 1);
        }
      }

      out.close();
      socket.close();
    } catch (IOException e) {
      logger.error("Exception caught: " + e.getMessage());
    }
  }

  public void sendMessage(String message) {
    out.println(message);
  }
}
