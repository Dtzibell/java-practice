package com.dtzi.app;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Set;

public class App {
  public static void main(String[] args) {
    Random numberGenerator = new Random();
    Scanner sc = new Scanner(System.in);
    int[] correctIncorrect = new int[2];
    Set<Integer> validGuesses = Set.of(1,2,3,4,5,6,7,8,9,10);
    int number;
    int guess;
    for (int i = 0; i < correctIncorrect.length; i++) {
      correctIncorrect[i] = 0;
    }
    while (true) {
      while (true) {
        number = numberGenerator.nextInt(10) + 1;
        System.out.print("Guess a number between 1 and 10: ");
        while (true) {
          if (sc.hasNextInt()) {
            guess = sc.nextInt();
            break;
          }
          sc.next();
        }
        if (!validGuesses.contains(guess)) {
          sc.close();
          System.exit(1);;
        }
        if (guess == number) {
          correctIncorrect[0]++;
        }
        else correctIncorrect[1]++;
        System.out.println("Number was " + number + "\n" +
            "Current score: \n" +
            "Guessed correctly: " + correctIncorrect[0] + "\n" +
            "Guessed incorrectly: " + correctIncorrect[1] + "\n");
      }
    }
  }
} 
