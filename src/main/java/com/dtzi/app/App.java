package com.dtzi.app;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

public class App {
  public static void main(String[] args) {
    File dataFile = new File("data.json");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    ExpensesCalculator tracker = new ExpensesCalculator();
    tracker = tracker.importData(dataFile);
    // similar to with keyword in python, good practice to catch errors in the statement.
    try (FileWriter writer = new FileWriter(dataFile)) {
      while (true) {
        Integer action = tracker.retrieveAction();
        switch (action) {
          case 1: tracker.retrieveIncomeType();
                  tracker.retrieveAmount();
                  tracker.retrieveDate();
                  break;
          case 2: tracker.retrieveExpenseType();
                  tracker.retrieveAmount();
                  tracker.retrieveDate();
                  break;
          case 3: tracker.retrieveSummary();
                  break;
          case 4: tracker.retrieveTransactions();
                  break;
          default: System.out.print("not implemented");
                   break;
        }
        mapper.writeValue(dataFile, tracker);
      }
    } catch (IOException e) {
      System.out.println(e + ": File is inaccessible");
      System.exit(1);
    }
  }
}

class ExpensesCalculator {
  public List<Integer> transactionType, expenseType;
  public List<Double> amounts;
  public List<LocalDate> dates; // use parse of String with some try-catch
  Scanner inputScanner = new Scanner(System.in); 
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
      return TRANSACTIONS.get(index); // ??? I forgot why its like this;
                                      // seems like I wanted this to be some kind of 
                                      // wrapper that would only allow ints to be input? :D
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

  // placeholder initializer to avoid using non-sensical static methods and fields.
  ExpensesCalculator() {
  }

  ExpensesCalculator(List<Integer> transactions, List<Integer> expenses, List<Double> amounts, List<LocalDate> dates) {
    this.transactionType = transactions;
    this.expenseType = expenses;
    this.amounts = amounts;
    this.dates = dates;
  }

  // for serialization
  List<Integer> getTransactionTypes() {
    return transactionType;
  }
  List<Integer> getExpenseTypes() {
    return expenseType;
  }
  List<Integer> getAmounts() {
    return transactionType;
  }
  List<Integer> getDates() {
    return transactionType;
  }

  Integer retrieveAction() {
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
  void retrieveExpenseType() {
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
  void retrieveIncomeType() {
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
  void retrieveAmount() {
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
  void retrieveDate() {
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
  void retrieveSummary(){
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
  //public function
  
  //returns nothing
  public void retrieveTransactions () {
    System.out.println("===== List of transactions ====="); 
    for (int a = 0; a <
        this.transactionType.size(); a++) {
      ExpensesCalculator.TransactionType transactionType =
        ExpensesCalculator.TransactionType.byIndex(this.transactionType.get(a));
      System.out.println("Transaction type: " + transactionType); 
      if (transactionType == ExpensesCalculator.TransactionType.INCOME) {
        ExpensesCalculator.IncomeType moneyPurpose =
          ExpensesCalculator.IncomeType.byIndex(this.expenseType.get(a));
        System.out.println("Income type: " + moneyPurpose); 
      } 
      else {
          ExpensesCalculator.ExpenseType moneyPurpose =
            ExpensesCalculator.ExpenseType.byIndex(this.expenseType.get(a));
          System.out.println("Expense type: " + moneyPurpose);
      }
      System.out.println("Amount: " + this.amounts.get(a));
      System.out.println("Date: " + this.dates.get(a) + "\n"); 
    }
  }
  //public function
  //
  // Attempts to read the data json and creates an ExpensesCalculator from it.
  // Unless fetching JSON fails, then creates an empty ExpensesCalculator.
  //
  //returns ExpensesCalculator tracker
  public ExpensesCalculator importData (File dataFile) {
    ExpensesCalculator tracker; 
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String data = "";
    try {
      System.out.println("File exists: " + dataFile.exists());
      System.out.println("File path: " + dataFile.getAbsolutePath());
      Scanner scanner = new Scanner(dataFile);
      // Scanner next method only returns the next line of the file. Implement while looped nexting.
      while (scanner.hasNext()) {
        // Adding text to a string does not create new lines all the time, they can be added manually tho.
        data = data + scanner.next() + "\n";
      }
      scanner.close();
      // Missing some error handling, but idk what errors. Can separately parse 
      // each line into list? But how? Cant create variables in a loop.
      // Maybe create a mapping and then create the expenses calculator by using the 
      // lists within the map?
      tracker = mapper.readValue(data, ExpensesCalculator.class);
    }
    catch (FileNotFoundException e) {
      System.out.println("File data.json for storing data does not exist, creating a new file");
      this.transactionType = new ArrayList<>();
      this.expenseType = new ArrayList<>();
      this.amounts = new ArrayList<>();
      this.dates = new ArrayList<>();
      tracker = new ExpensesCalculator(transactionType, expenseType, amounts, dates); 
    }
    catch (JsonProcessingException e) {
      System.out.println(e + ": Issue processing JSON");
      this.transactionType = new ArrayList<>();
      this.expenseType = new ArrayList<>();
      this.amounts = new ArrayList<>();
      this.dates = new ArrayList<>();
      tracker = new ExpensesCalculator(transactionType, expenseType, amounts, dates); 
    }
    return tracker; 
  }

  //public static function
  //returns nothing
  public static void write () {
    
  }
}
