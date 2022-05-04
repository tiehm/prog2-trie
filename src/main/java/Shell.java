package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Shell {

  private Trie trie = new Trie();

  private Shell() {}

  public static void main(String[] args) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    new Shell().execute(stdin);
  }

  private void execute(BufferedReader stdin) throws IOException {
    boolean quit = false;
    while (!quit) {
      System.out.print("trie> ");
      String line = stdin.readLine();
      if (line == null)  {
        break;
      }

      String[] tokens = line.split(" ");

      if (tokens.length == 0) {
        printError("invalid command, see 'help' for more information");
        continue;
      }

      String command = tokens[0];

      switch (command) {
        case "new" -> execNewCmd();
        case "add" -> execAddCmd(tokens);
        case "change" -> execChangeCmd(tokens);
        case "delete" -> execDeleteCmd(tokens);
        case "points" -> execPointsCmd(tokens);
        case "trie" -> execTrieCmd();
        case "help" -> execHelpCmd();
        case "quit" -> quit = true;
        default -> printError("invalid command, see 'help' for more information");
      }
    }
  }

  private void execNewCmd() {
    trie = new Trie();
  }

  private void execAddCmd(String[] tokens) {
    if (tokens.length != 3) {
      printError("invalid command, see 'help' for more information");
      return;
    }
    String name = tokens[1];
    String points = tokens[2];
    int pointsInt;
    try {
      pointsInt = Integer.parseInt(points);
    } catch (NumberFormatException e) {
      printError("invalid command, see 'help' for more information");
      return;
    }
    boolean isNewEntry = trie.add(name, pointsInt);
    if (!isNewEntry) {
      printError("an entry with the given name already exists");
    }
  }

  private void execChangeCmd(String[] tokens) {
    if (tokens.length != 3) {
      printError("invalid command, see 'help' for more information");
      return;
    }

    String name = tokens[1];
    String points = tokens[2];
    int pointsInt;
    try {
      pointsInt = Integer.parseInt(points);
    } catch (NumberFormatException e) {
      printError("invalid command, see 'help' for more information");
      return;
    }
    boolean changedEntry = trie.change(name, pointsInt);
    if (!changedEntry) {
      printError("an entry with the given name does not exist");
    }
  }

  private void execDeleteCmd(String[] tokens) {
    if (tokens.length != 2) {
      printError("invalid command, see 'help' for more information");
      return;
    }
    String name = tokens[1];
    boolean entryDeleted = trie.remove(name);
    if (!entryDeleted) {
      printError("an entry with the given name does not exist");
    }
  }

  private void execPointsCmd(String[] tokens) {
    if (tokens.length != 2) {
      printError("invalid command, see 'help' for more information");
      return;
    }
    String name = tokens[1];
    Integer points = trie.points(name);
    if (points == null) {
      printError("an entry with the given name does not exist");
      return;
    }
    System.out.println(points);
  }

  private void execTrieCmd() {
    System.out.println(trie.toString());
  }

  private void execHelpCmd() {
    System.out.println("""
      new - creates a new trie
      add <name> <points> - adds a new name with the given points
      change <name> <points> - changes the points of the given name
      delete <name> - deletes the given name
      points <name> - returns the points of the given name
      trie - prints the trie
      help - prints this help message
      quit - quits the program""");
  }

  private void printError(String message) {
    System.out.println("Error!: " + message);
  }

}
