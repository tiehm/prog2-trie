package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Shell {

  private static Trie trie = new Trie();

  private Shell() {}

  public static void main(String[] args) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    execute(stdin);
  }

  private static void execute(BufferedReader stdin) throws IOException {
    boolean quit = false;
    while (!quit) {
      System.out.print("trie> ");
      String line = stdin.readLine();
      if (line == null)  {
        break;
      }

      String[] tokens = line.split(" ");

      if (tokens.length == 0) {
        System.out.println("Error!: invalid command");
        continue;
      }

      String command = tokens[0];
      int pointsInt;
      String name;
      String points;
      switch (command) {
        case "new":
          trie = new Trie();
          break;
        case "add":
          if (tokens.length != 3) {
            System.out.println("Error!: invalid command");
            continue;
          }
          name = tokens[1];
          points = tokens[2];
          try {
            pointsInt = Integer.parseInt(points);
          } catch (NumberFormatException e) {
            System.out.println("Error!: invalid command");
            continue;
          }
          trie.add(name, pointsInt);
          break;
        case "change":
          if (tokens.length != 3) {
            System.out.println("Error!: invalid command");
            continue;
          }
          name = tokens[1];
          points = tokens[2];

          try {
            pointsInt = Integer.parseInt(points);
          } catch (NumberFormatException e) {
            System.out.println("Error!: invalid command");
            continue;
          }
          trie.change(name, pointsInt);
          break;
        case "delete":
          if (tokens.length != 2) {
            System.out.println("Error!: invalid command");
            continue;
          }
          name = tokens[1];
          trie.remove(name);
          break;
        case "points":
          if (tokens.length != 2) {
            System.out.println("Error!: invalid command");
            continue;
          }
          name = tokens[1];
          System.out.println(trie.points(name));
          break;
        case "trie":
          System.out.println(trie.toString());
          break;
        case "help":
          System.out.println("""
            new - creates a new trie
            add <name> <points> - adds a new name with the given points
            change <name> <points> - changes the points of the given name
            delete <name> - deletes the given name
            points <name> - returns the points of the given name
            trie - prints the trie
            help - prints this help message
            quit - quits the program
              """);
        case "quit":
          quit = true;
          break;
      }

    }
  }

}
