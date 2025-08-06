package com.dtzi.app;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Calculator {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Pattern OPERATIONS = Pattern.compile("[+\\-*/]$"); // AI says it shouldnt work but it works?
    InputReader IR = new InputReader(sc);
    double firstNumber = IR.input("Enter the first number: ");
    String operation = IR.input("Enter a mathematical operation <+-*/>: ", OPERATIONS);
    double secondNumber = IR.input("Enter the second number: ");
    double answer = switch (operation) {
      case "+"  -> firstNumber + secondNumber;
      case "-"  -> firstNumber - secondNumber;
      case "*"  -> firstNumber * secondNumber;
      case "/"  -> {
        if (secondNumber == 0) {
          System.out.println("Division by zero.");
          yield 0;
        }
        else {
          yield (double) firstNumber / secondNumber;
        }
      }
      default -> 0;
    };
    System.out.println(firstNumber + operation + secondNumber + "=" + answer);
    sc.close();
  }
}
class InputReader {
    Scanner myScanner;
    InputReader(Scanner sc){
      myScanner = sc;
    }

    String input(String question, Pattern operations) {
      System.out.print(question);
      String operation;
      
      while (true) {
        String input = myScanner.next();
        if (operations.matcher(input).matches()) {
          operation = input;
          break;
        }
        System.out.print("Invalid input. Valid input is <+-*/>: ");
      }
      return operation;
    }

    double input(String question) {
      System.out.print(question);
      int firstNumber;
      while (true) {
        if (myScanner.hasNextInt()) {
          firstNumber = myScanner.nextInt();
          break;
         }
        System.out.print("You did not enter a number. Try again: ");
        myScanner.next(); // this removes the previous input from the command line.
                          // such that the next input is empty at the beginning.
                          // Otherwise the input is always not a number if the
                          // first input is not a number
      }
      return firstNumber;
    }
  }

