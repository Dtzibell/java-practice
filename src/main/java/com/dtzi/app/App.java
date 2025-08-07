package com.dtzi.app;
import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class App {
  public static void main(String[] args) {
    ExpensesCalculator tracker = new ExpensesCalculator();
    while (true) {
      Integer action = tracker.getAction();
      switch (action) {
        case 1: tracker.getIncomeType();
                tracker.getAmount();
                tracker.getDate();
                break;
        case 2: tracker.getExpenseType();
                tracker.getAmount();
                tracker.getDate();
                break;
        case 3: tracker.getSummary();
                break;
                /* This is potentially turnable into function, too lazy.
                 * Could cache everything into a dict real time while inputting
                 * expenses.
                 */
        case 4: System.out.println("===== List of transactions =====");
                for (int a = 0; a < tracker.transactionType.size(); a++) {
                  ExpensesCalculator.TransactionType transactionType = ExpensesCalculator.TransactionType.byIndex(tracker.transactionType.get(a));
                  System.out.println("Transaction type: " + transactionType);
                  if (transactionType == ExpensesCalculator.TransactionType.INCOME) {
                    ExpensesCalculator.IncomeType moneyPurpose = ExpensesCalculator.IncomeType.byIndex(tracker.expenseType.get(a));
                    System.out.println("Income type: " + moneyPurpose);
                  }
                  else {
                    ExpensesCalculator.ExpenseType moneyPurpose = ExpensesCalculator.ExpenseType.byIndex(tracker.expenseType.get(a));
                    System.out.println("Expense type: " + moneyPurpose);
                  }
                  System.out.println("Amount: " + tracker.amounts.get(a));
                  System.out.println("Date: " + tracker.dates.get(a) + "\n");
                }
                break;
        default: System.out.print("not implemented");
                 break;
      }
    }
  }
}

class ExpensesCalculator {
  List<Integer> transactionType, expenseType;
  List<Double> amounts;
  List<LocalDate> dates; // use parse of String with some try-catch
  Scanner inputScanner; 
  Integer action;
  enum TransactionType {
    INCOME(1), EXPENSE(2);
    
    public final int index;
    private TransactionType (int index) {
      this.index = index;
    }

    private static final Map<Integer, TransactionType> TRANSACTIONS = new HashMap<>();
    
    static { 
      for (TransactionType t: values()) {
        TRANSACTIONS.put(t.index, t);
      }
    }

    static TransactionType byIndex (int index) {
      return TRANSACTIONS.get(index);
    }
  }
  enum ExpenseType {
    FOOD(1), RENT(2), TRANSPORT(3), ENTERTAINMENT(4), OTHER(5);

    public final int index;
    private ExpenseType (int index) {
      this.index = index;
    }

    private static final Map<Integer, ExpenseType> TRANSACTIONS = new HashMap<>();
    
    static { 
      for (ExpenseType t: values()) {
        TRANSACTIONS.put(t.index, t);
      }
    }

    static ExpenseType byIndex (int index) {
      return TRANSACTIONS.get(index);
    }
  }
  enum IncomeType {
    SALARY(1), GIFT(2), ALIMONY(3), OTHER(4);
    
    public final int index;
    private IncomeType (int index) {
      this.index = index;
    }

    private static final Map<Integer, IncomeType> TRANSACTIONS = new HashMap<>();
    
    static { 
      for (IncomeType t: values()) {
        TRANSACTIONS.put(t.index, t);
      }
    }

    static IncomeType byIndex (int index) {
      return TRANSACTIONS.get(index);
    }
  }

  ExpensesCalculator(){
    inputScanner = new Scanner(System.in);
    transactionType = new ArrayList<>();
    expenseType = new ArrayList<>();
    amounts = new ArrayList<>();
    dates = new ArrayList<>();
  }

  Integer getAction() {
    String lines = "===== Personal expenses tracker ===== \n" + 
        "1. Add income \n" +
        "2. Add expense \n" +
        "3. View summary \n" +
        "4. View all transactions \n" +
        "5. Exit";
    System.out.println(lines);
    Set<Integer> validPurposes = Set.of(1,2);
    Set<Integer> validFunctions = Set.of(3,4);
    while (true) {
      if (inputScanner.hasNextInt()) {
        action = inputScanner.nextInt();
        if (validPurposes.contains(action)){
          transactionType.add(action);
          break;
        }
        else if (validFunctions.contains(action)) {
          break;
        }
        else if (action == 5) {
          System.out.print("Exiting the expenses tracker");
          System.exit(0);
        }
      }
      inputScanner.next();
      System.out.print("Invalid input. Enter a number between 1 and 5: ");
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
          expenseType.add(action);
          break;
        }
      }
      inputScanner.next();
      System.out.print("Invalid input. Please enter a number between 1 and 5: ");
    }
  }
  void getIncomeType() {
    String lines = "Select an income type: \n" +
      "1. Salary \n" +
      "2. Gift \n" +
      "3. Alimony \n" + 
      "4. Other";
    System.out.println(lines);
    Set<Integer> validOptions = Set.of(1,2,3,4);
    while (true) {
      if (inputScanner.hasNextInt()) {
        action = inputScanner.nextInt();
        if (validOptions.contains(action)){
          expenseType.add(action);
          break;
        }
      }
      inputScanner.next();
      System.out.print("Invalid input. Please enter a number between 1 and 4: ");
    }
  }
  void getAmount() {
    System.out.print("Enter the amount: ");
    while (true){
      if (inputScanner.hasNextDouble()) {
        if (transactionType.getLast() == 1) {
          amounts.add(inputScanner.nextDouble());
          break;
        }
        else {
          amounts.add(-inputScanner.nextDouble());
          break;
        }
      }
      inputScanner.next();
      System.out.print("Invalid input. Please enter a dot denominated decimal: ");
    }
  }
  void getDate() {
    System.out.print("Enter date of transaction (ISO-8601): ");
    while (true) {
      try {
        String input = inputScanner.next();
        LocalDate date = LocalDate.parse(input);
        dates.add(date);
        break;
      } 
      catch (DateTimeParseException e) {
        System.out.print("Incorrect date format. Try YYYY-MM-DD: ");
      }
    }
  }
  void getSummary(){
    System.out.println("===== Summary =====");
    Double sumOfIncoming = 0.0d;
    Double sumOfOutgoing = 0.0d;
    Double amount;
    for (int i = 0; i < amounts.size(); i++) {
      amount = amounts.get(i);
      if (amount < 0) {
        sumOfOutgoing += amount;
      }
      else {
        sumOfIncoming += amount;
      }
    }
    System.out.print("Total balance: ");
    System.out.println(sumOfIncoming + sumOfOutgoing);
    System.out.print("Total income: ");
    System.out.println(sumOfIncoming);
    System.out.print("Total expenses: ");
    System.out.println(-sumOfOutgoing);
  }
}
