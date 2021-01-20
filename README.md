# Game of Three

### Description: 
This repository contains the code for the 'Game of Three' coding challenge. 
Game of Three is a turn-based 2 player game in which the goal is to reach the number 1.

### Game mechanics:
Player 1 starts by providing a number and his opponent must add 1, subtract 1 or leave the number
unchanged so that the number is now divisible by three. The result from this division is then forwarded
back to player 1 and the loop continues until the result of the division is 1. The player who achieves this 
wins the game.

### How to run the project
There are two options to run the project:
1) Through the executable jars (simpler)
2) Using maven

By default, both the server and the client listen for connections on port 54321.

A different port can be supplied as a parameter to the programs upon execution.

**Option 1: Through the executable jars**

_Note: Every .jar file should be run in a separate console._

```
Navigate to the artifacts folder:
> cd /gameofthree/out/artifacts

run the server:
> java -jar server.jar <port_number>

run the clients:
> java -jar client.jar <port_number>
```
**Option 2: Using maven**

```
Note: arguments in {brackets} are optional.

Run the server:
Navigate to server directory
> cd /gameofthree/server
> mvn exec:java -Dexec.mainClass="gotclient.GameClient" {-Dexec.args="12345"}

Run the clients:
Navigate to the client directory
> cd /gameofthree/client
> mvn exec:java -Dexec.mainClass="gotclient.GameClient" {-Dexec.args="12345"}
```