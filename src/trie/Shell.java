package trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


/**
 * Shell is the entry class for the Trie program. It handles the processing of
 * all CLI input from a user and acts with the Trie structure behind the user
 * commands.
 */
public final class Shell {

  private Trie trie = new Trie();

  /**
   * The class should never be initialised from somewhere else as it is the
   * entry point and just a utility class.
   */
  private Shell() {
  }

  /**
   * Entrypoint of the REPL shell.
   *
   * @param args The program arguments.
   * @throws IOException Reading a line might trow an IOException.
   */
  public static void main(String[] args) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    new Shell().handleUserInput(stdin);
  }

  /**
   * Handles user input in a classical REPL way, parses commands and verifies
   * input for correctness.
   *
   * @param stdin the standard input within a BufferedReader
   * @throws IOException reading new lines can cause an exception
   * @see BufferedReader#readLine()
   */
  private void handleUserInput(BufferedReader stdin) throws IOException {
    // Quit is only ever to be set true if the user calls the "quit" command.
    boolean quit = false;

    while (!quit) {
      System.out.print("trie> ");
      String line = stdin.readLine();
      if (line == null) {
        /*
         Line can be null if the stream ended, we should also stop our
         program then.
        */
        break;
      }

      String[] tokens = line.split(" ");
      if (tokens.length == 0) {
        printError("invalid command, see 'help' for more information");
        continue;
      }

      char commandChar = tokens[0].toLowerCase().charAt(0);

      switch (commandChar) {
        case 'n' -> execNewCmd();
        case 'a' -> execAddCmd(tokens);
        case 'c' -> execChangeCmd(tokens);
        case 'd' -> execDeleteCmd(tokens);
        case 'p' -> execPointsCmd(tokens);
        case 't' -> execTrieCmd();
        case 'h' -> execHelpCmd();
        case 'q' -> quit = true;
        default ->
            printError("invalid command, see 'help' for more information");
      }
    }
  }

  /**
   * Executes the "new" command which creates a new trie structure and discards
   * the older one (by overwriting it).
   */
  private void execNewCmd() {
    trie = new Trie();
  }

  /**
   * Executes the "add" command which adds a new value to the trie. It requires
   * a name as the first argument (beware that the tokens Array has the command
   * in it, thus it is the second argument in the tokens Array) and an Integer
   * value to save for that name. Duplicate entries are not allowed, an error is
   * displayed to the user upon trying to do so.
   *
   * @param tokens the user input as an Array of Strings, split by whitespaces
   */
  private void execAddCmd(String[] tokens) {
    // The command is: add <name> <points>, thus we need exactly 3 tokens.
    if (tokens.length != 3) {
      printError("invalid command, see 'help' for more information");
      return;
    }

    String name = tokens[1];
    String nameErrorMessage = isValidName(name);
    if (nameErrorMessage != null) {
      printError(nameErrorMessage);
      return;
    }

    String points = tokens[2];
    int pointsInt;

    try {
      pointsInt = Integer.parseInt(points);
    } catch (NumberFormatException e) {
      printError("invalid argument for points, points needs to be an integer "
          + "value");
      return;
    }

    if (pointsInt < 0) {
      printError("invalid argument for points, points needs to be an integer "
          + "value greater or equal to 0");
      return;
    }

    boolean successfullyInserted = trie.add(name, pointsInt);
    if (!successfullyInserted) {
      printError("an entry with the given name already exists");
    }
  }

  /**
   * Executes the "change" command which changes the value of an existing name.
   * It requires a name as the first argument (beware that the tokens Array has
   * the command in it, thus it is the second argument in the tokens Array) and
   * an Integer value to change for that name. Changing values is only allowed
   * for names which are already associated with a value, trying to change
   * values of non-existent names will case an error presented to the user.
   *
   * @param tokens the user input as an Array of Strings, split by whitespaces
   */
  private void execChangeCmd(String[] tokens) {
    if (tokens.length != 3) {
      printError("invalid command, see 'help' for more information");
      return;
    }

    String name = tokens[1];
    String nameErrorMessage = isValidName(name);
    if (nameErrorMessage != null) {
      printError(nameErrorMessage);
      return;
    }

    String points = tokens[2];
    int pointsInt;

    try {
      pointsInt = Integer.parseInt(points);
    } catch (NumberFormatException e) {
      printError("invalid argument for points, points needs to be an integer "
          + "value");
      return;
    }

    if (pointsInt < 0) {
      printError("invalid argument for points, points needs to be an integer "
          + "value greater or equal to 0");
      return;
    }

    boolean successfullyChangedValue = trie.change(name, pointsInt);
    if (!successfullyChangedValue) {
      printError("an entry with the given name does not exist");
    }

  }

  /**
   * Executes the "delete" command which deletes the value of an existing name.
   * It requires a name as the first argument (beware that the tokens Array has
   * the command in it, thus it is the second argument in the tokens Array).
   * Deleting values is only allowed for names which are already associated with
   * a value, trying to delete values of non-existent names will case an error
   * presented to the user. Nodes which are not in need after deleting the value
   * for a name are also deleted.
   *
   * @param tokens the user input as an Array of Strings, split by whitespaces
   */
  private void execDeleteCmd(String[] tokens) {
    if (tokens.length != 2) {
      printError("invalid command, see 'help' for more information");
      return;
    }

    String name = tokens[1];
    String nameErrorMessage = isValidName(name);
    if (nameErrorMessage != null) {
      printError(nameErrorMessage);
      return;
    }

    boolean entryDeleted = trie.remove(name);
    if (!entryDeleted) {
      printError("an entry with the given name does not exist");
    }
  }

  /**
   * Executes the "points" command which returns the points a given name has set
   * to it. It requires a name as the first argument (beware that the tokens
   * Array has the command in it, thus it is the second argument in the tokens
   * Array). Only existing names are supported, trying to get the points of a
   * name which does not exist or a name which has no points associated will
   * result in an error.
   *
   * @param tokens the user input as an Array of Strings, split by whitespaces
   */
  private void execPointsCmd(String[] tokens) {
    if (tokens.length != 2) {
      printError("invalid command, see 'help' for more information");
      return;
    }

    String name = tokens[1];
    String nameErrorMessage = isValidName(name);
    if (nameErrorMessage != null) {
      printError(nameErrorMessage);
      return;
    }

    Integer points = trie.points(name);
    if (points == null) {
      printError("an entry with the given name does not exist");
      return;
    }
    System.out.println(points);
  }

  /**
   * Executes the "trie" command which prints the trie in the console. An empty
   * trie is represented as just "+".
   */
  private void execTrieCmd() {
    System.out.println(trie.toString());
  }

  /**
   * Executes the "help" command which displays general documentation about all
   * possible commands and how to use them.
   */
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

  /**
   * Prints the given error message to the console with the "Error!: " prefix.
   *
   * @param message error message to print
   */
  private void printError(String message) {
    System.out.println("Error! " + message);
  }

  /**
   * Checks if the given name qualifies our rules of just lowercase letters from
   * a-z.
   *
   * @param name The name input by the user.
   * @return An error message if any errors were found, otherwise null.
   */
  private String isValidName(String name) {
    if (!Objects.equals(name, name.toLowerCase())) {
      return "invalid argument for name, names can only be lowercase";
    }
    if (!name.matches("[a-zA-Z]+")) {
      return "invalid argument for name, names can only be lowercase "
          + "letters from the standard alphabet (a-z)";
    }
    return null;
  }

}
