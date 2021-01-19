package gotclient.message.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserInputHandler {

  private final BufferedReader inputReader;
  private final Set<Integer> validGameNumbers = new HashSet<>();

  public UserInputHandler(BufferedReader inputReader) {
    this.inputReader = inputReader;

    validGameNumbers.add(-1);
    validGameNumbers.add(0);
    validGameNumbers.add(1);
  }

  public int getValidNumber(boolean isFirstTurn, int currentGameNumber) {
    return isFirstTurn ? getStartingNumber() :
      getInputNumberFromSet(validGameNumbers, currentGameNumber);
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

  private int getInputNumberFromSet(Set<Integer> validNumbers, int currentGameNumber) {
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
}
