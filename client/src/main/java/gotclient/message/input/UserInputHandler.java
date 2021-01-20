package gotclient.message.input;

import gotclient.gamemanager.GameManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for providing the appropriate user input for the Game of Three
 * This input is always a number, which can represent either the game mode (automatic or manual) or
 * an move in the game (i.e. an integer divisible by 3).
 */
public class UserInputHandler {

  private final BufferedReader inputReader;
  private final Set<Integer> validGameNumbers = new HashSet<>();

  public UserInputHandler(BufferedReader inputReader) {
    this.inputReader = inputReader;

    validGameNumbers.add(-1);
    validGameNumbers.add(0);
    validGameNumbers.add(1);
  }

  public int getValidNumber(boolean isFirstTurn, int currentGameNumber, int gameMode) {
    if (isFirstTurn) {
      return getStartingNumber();
    }
    return gameMode == GameManager.MANUAL ?
      getUserInputNumberFromSet(validGameNumbers, currentGameNumber) :
      getAutomaticInputNumberFromSet(validGameNumbers, currentGameNumber);
  }

  public int selectInputMode() {
    int selection = -1;
    while (!(selection == 0 || selection == 1)) {
      System.out.print("Enter a number to select the game mode (0 - manual, 1 - auto): ");
      try {
        selection = Integer.parseInt(inputReader.readLine());
        if (!(selection == 0 || selection == 1)) {
          System.out.println("Please enter only 0 or 1 to select the game mode!");
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
      } catch (IOException e) {
        System.out.println("Error processing input!");
      }
    }
    return selection;
  }


  private int getStartingNumber() {
    int userInputNumber = Integer.MIN_VALUE;
    boolean validNumberEntered = false;
    while (!validNumberEntered) {
      System.out.print("Enter a positive number bigger than 3: ");
      try {
        userInputNumber = Integer.parseInt(inputReader.readLine());
        if (userInputNumber <= 3) {
          System.out.println("Only positive numbers bigger than 3 are allowed!");
        } else {
          validNumberEntered = true;
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
      } catch (IOException e) {
        System.out.println("Error processing input!");
      }
    }
    return userInputNumber;
  }

  private int getUserInputNumberFromSet(Set<Integer> validNumbers, int currentGameNumber) {
    int userInputNumber = Integer.MIN_VALUE;
    boolean isValidNumber = false;
    while (!isValidNumber) {
      System.out.print("Enter a number from " + validNumbers + ": ");
      try {
        userInputNumber = Integer.parseInt(inputReader.readLine());
        isValidNumber = true;
        if (!validNumbers.contains(userInputNumber)) {
          System.out.println("Only numbers in " + validNumbers + " are allowed!");
          isValidNumber = false;
        }
        if ((currentGameNumber + userInputNumber) % 3 != 0) {
          System.out.println(currentGameNumber + " + " + userInputNumber + " is not divisible by 3!");
          System.out.println("Please try again");
          isValidNumber = false;
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
      } catch (IOException e) {
        System.out.println("Error processing input!");
      }
    }
    return userInputNumber;
  }

  private int getAutomaticInputNumberFromSet(Set<Integer> validNumbers, int currentGameNumber) {
    int validNumber = 0;
    for (Integer candidateNum : validNumbers) {
      if (((currentGameNumber + candidateNum) % 3) == 0) {
         validNumber = candidateNum;
         break;
      }
    }
    return validNumber;
  }
}
