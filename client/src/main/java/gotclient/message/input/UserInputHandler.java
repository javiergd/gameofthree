package gotclient.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ClientRequestHandler {

  private final BufferedReader inputReader;
  private final Set<Integer> validGameNumbers = new HashSet<>();

  public ClientRequestHandler(BufferedReader inputReader) {
    this.inputReader = inputReader;

    validGameNumbers.add(-1);
    validGameNumbers.add(0);
    validGameNumbers.add(1);
  }

  public int getValidNumber(boolean isFirstTurn) {
    return isFirstTurn ? getStartingNumber() : getInputNumberFromSet(validGameNumbers);
  }

  private int getStartingNumber() {
    int userInputNumber = Integer.MIN_VALUE;
    boolean validNumberEntered = false;
    while (!validNumberEntered) {
      System.out.print("Enter a positive number: ");
      try {
        userInputNumber = Integer.parseInt(inputReader.readLine());
        if (userInputNumber <= 0) {
          System.out.println("Only positive numbers are allowed!");
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

  private int getInputNumberFromSet(Set<Integer> validNumbers) {
    int userInputNumber = Integer.MIN_VALUE;
    while (!validNumbers.contains(userInputNumber)) {
      System.out.print("Enter a number from " + validNumbers + ": ");
      try {
        userInputNumber = Integer.parseInt(inputReader.readLine());
        if (!validNumbers.contains(userInputNumber)) {
          System.out.println("Only numbers in " + validNumbers + " are allowed!");
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number!");
      } catch (IOException e) {
        System.out.println("Error processing input!");
      }
    }
    return userInputNumber;
  }

  public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    ClientRequestHandler req = new ClientRequestHandler(br);

    System.out.println("Entered number: " + req.getValidNumber(true));
  }
}
