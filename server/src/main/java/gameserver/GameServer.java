package gameserver;

import gameserver.message.ClientResponseHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GameServer {
  private static final Logger logger = Logger.getLogger(GameServer.class);

  public static final int MAX_CONNECTIONS = 2;
  public static final int DEFAULT_PORT = 54321;

  private Map<Integer, GameServerThread> playerMap;
  private int turnsPlayed;

  public GameServer() {
    this.playerMap = new HashMap<>();
    this.turnsPlayed = 0;
  }

  public static void main(String[] args) {
    GameServer server = new GameServer();
    server.acceptConnections();
  }

  private void acceptConnections() {
    try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
      logger.info("Waiting for connections...");
      int connectionCount = 0;
      while(connectionCount < MAX_CONNECTIONS) {
        Socket clientSocket = serverSocket.accept();
        connectionCount++;

        logger.info("A player joined. Connected players:  " + connectionCount);
        GameServerThread gotServerThread = new GameServerThread(clientSocket,
          this, connectionCount);
        playerMap.put(connectionCount, gotServerThread);

        gotServerThread.start();
      }
    } catch (IOException e) {
      logger.error("Exception caught when trying to listen on port "
        + DEFAULT_PORT + " or listening for a connection");
      logger.error(e.getMessage());
    }
  }

  protected void forwardMessage(final String message, final int fromId, final int toId) {
    GameServerThread playerToSend = playerMap.get(toId);
    logger.info("Sending message to player: " + toId);
    ClientResponseHandler inGameResponse = new ClientResponseHandler(fromId,
      message, turnsPlayed == 0);

    playerToSend.sendMessage(inGameResponse.buildResponseString());
    turnsPlayed++;
  }

}
