package com.dtzi.app;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class App {
  public static void main(String[] args) {
    ExpensesCalculator tracker = new ExpensesCalculator();
    Integer action = tracker.getAction();
    switch (action) {
      case 1: tracker.getIncomeType();
              break;
      case 2: tracker.getExpenseType();
              break;
      case 4: for (int a = 0; a < tracker.transactionType.size(); a++) {
              System.out.println("Transaction type: " + tracker.transactionType.get(a));
              System.out.println("Expense/income type: " + tracker.expenseType.get(a));
              System.out.println("Amount: " + tracker.amounts.get(a));
              System.out.println("Date: " + tracker.dates.get(a));
              }
              break;
      default: System.out.print("not implemented");
               break;
      }
    // tracker.getExpenseType();
    // tracker.getAmount();
    // tracker.getDate();
  }
}

class ExpensesCalculator {
  List<Integer> transactionType, expenseType;
  List<Double> amounts;
  List<LocalDate> dates; // use parse of String with some try-catch
  Scanner inputScanner; 

  ExpensesCalculator(){
    inputScanner = new Scanner(System.in);
    transactionType = new ArrayList<>();
    expenseType = new ArrayList<>();
    amounts = new ArrayList<>();
    dates = new ArrayList<>();
  }

  Integer getAction() {
    Integer action;
    String lines = "===== Personal expenses tracker ===== \n" + 
        "1. Add income \n" +
        "2. Add expense \n" +
        "3. View summary \n" +
        "4. View all transactions \n" +
        "5. Exit";
    System.out.println(lines);
    Set<Integer> validOptions = Set.of(1,2,3,4);
    while (true) {
      if (inputScanner.hasNextInt()) {
        action = inputScanner.nextInt();
        if (validOptions.contains(action)){
          expenseType.add(action);
          break;
        }
        else if (action == 5) {
          System.out.print("Exiting the expenses tracker");
          System.exit(0);
        }
      }
      System.out.print("Invalid input. Enter a number between 1 and 5: ");
      inputScanner.next();
    }
    return action;
  }
  void getExpenseType() {
    Integer action;
    Set<Integer> validOptions = Set.of(1,2,3,4,5);
    String lines = "Select an expense type: \n" +
      "1. Food \n" +
      "2. Rent \n" +
      "3. Transport \n" + 
      "4. Entertainment \n" +
      "5. Other";
    System.out.println(lines);
    while (true) {
      if (inputScanner.hasNextInt()) {
        action = inputScanner.nextInt();
        if (validOptions.contains(action)){
          transactionType.add(inputScanner.nextInt());
          break;
        }
      }
      System.out.print("Invalid input. Please enter a number between 1 and 5: ");
      inputScanner.next();
    }
  }
  void getIncomeType() {
    String lines = "Select an income type: \n" +
      "1. Salary \n" +
      "2. Gift \n" +
      "3. Alimony \n" + 
      "4. Other";
    System.out.println(lines);
    while (true) {
      if (inputScanner.hasNextInt()) {
        transactionType.add(inputScanner.nextInt());
        break;
      }
      System.out.print("Invalid input. Please enter a number between 1 and 4: ");
      inputScanner.next();
    }
  }
  void getAmount() {
    System.out.print("Enter the amount: ");
    while (true){
      if (inputScanner.hasNextDouble()) {
        amounts.add(inputScanner.nextDouble());
        break;
      }
      System.out.print("Invalid input. Please enter a dot denominated decimal: ");
      inputScanner.next();
    }
  }
  void getDate() {
    System.out.print("Enter date of transaction (ISO-8601): ");
    while (true) {
      try {
        String input = inputScanner.next();
        LocalDate date = LocalDate.parse(input);
        dates.add(date);
      } 
      catch (DateTimeParseException e) {
        inputScanner.next();
      }
    }
  }
}
