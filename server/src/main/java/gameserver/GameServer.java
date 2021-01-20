package gameserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import gameserver.message.ClientResponseHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {

  private static final Logger logger = Logger.getLogger(GameServer.class);

  private static final int MAX_CONNECTIONS = 2;
  private static final int DEFAULT_PORT = 54321;
  private static final int BUFFER_CAPACITY = 1;

  private final int port;
  private final Map<Integer, GameServerThread> playerMap;
  private final List<String> messageBuffer;
  private int turnsPlayed;

  public GameServer(String[] args) {
    this.playerMap = new HashMap<>();
    this.messageBuffer = new ArrayList<>(BUFFER_CAPACITY);
    this.turnsPlayed = 0;
    this.port = getPortFromArgsOrDefault(args);
  }

  public static void main(String[] args) {
    GameServer server = new GameServer(args);
    server.acceptConnections();
  }

  protected void forwardMessage(final String message, final int fromId, final int toId)
    throws JsonProcessingException {
    GameServerThread playerToSend = playerMap.get(toId);
    logger.info("Sending message to player: " + toId);
    ClientResponseHandler response = new ClientResponseHandler(fromId,
      message, turnsPlayed == 0);

    String responseString = response.buildResponseString();
    if (playerToSend == null) {
      messageBuffer.add(responseString);
    } else {
      playerToSend.sendMessage(responseString);
    }
    turnsPlayed++;
  }

  protected void forwardBufferedMessage(final int toId) {
    GameServerThread playerToSend = playerMap.get(toId);
    playerToSend.sendMessage(messageBuffer.get(0));
    messageBuffer.clear();
  }

  protected boolean hasBufferedMessages() {
    return !messageBuffer.isEmpty();
  }

  private void acceptConnections() {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      logger.info("Waiting for connections...");
      int connectionCount = 0;
      while (connectionCount < MAX_CONNECTIONS) {
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



  private int getPortFromArgsOrDefault(String[] args) {
    if (args.length == 1) {
      try {
        return Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.out
          .println("Invalid port number supplied. Using port " + DEFAULT_PORT + " for connections");
        return DEFAULT_PORT;
      }
    } else {
      System.out.println("No port supplied. Using port " + DEFAULT_PORT + " for connections");
      return DEFAULT_PORT;
    }
  }



}
