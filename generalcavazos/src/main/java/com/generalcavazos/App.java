package com.cavazos;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import org.json.simple.JSONArray;

public class App {

    public static String[] commandArray;
    private static Stack<String> commandHistory = new Stack<>();
    private static Stack<String> redoStack = new Stack<>();

    public static void main(String[] args) {
        String fileName = "commands.json";

        // read commands from JSON file and read out commands into an array
        JSONArray commandJSONArray = JSONFile.readArray(fileName);

        if (commandJSONArray == null) {
            System.err.println("Count not load commands from resources: " + fileName);
            return;
        }

        // convert json array to string array
        commandArray = getCommandArray(commandJSONArray);

        // output command array to console
        System.out.println(Arrays.toString(commandArray));

        startMenu();
    }

    private static void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n====== General Cavazos Commander ======");

        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1. list   - Display all commands");
            System.out.println("2. issue  - Issue a command");
            System.out.println("3. random  - Issue a random command");
            System.out.println("4. undo   - Undo last command");
            System.out.println("5. redo   - Redo last undone command");
            System.out.println("6. history   - Display command history");
            System.out.println("7. clear   - Clear command history");
            System.out.println("8. help   - Show help information");
            System.out.println("9. exit    - Exit the program");
            System.out.print("\nEnter command: ");

            // convert input to lowercase and trim whitespace
            String input = scanner.nextLine().trim().toLowerCase();

            // switch statement to handle user input
            switch (input) {
                case "list":
                case "1":
                    displayAllCommands();
                    break;
                case "issue":
                case "2":
                    System.out.println("Issue command selected");
                    issueCommand();
                    break;
                case "random":
                case "3":
                    System.out.println("Random command selected");
                    issueRandomCommand();
                    break;
                case "undo":
                case "4":
                    System.out.println("Undo command selected");
                    undoCommand();
                    break;
                case "redo":
                case "5":
                    System.out.println("Redo command selected");
                    redoCommand();
                    break;
                case "history":
                case "6":
                    System.out.println("History command selected");
                    displayCommandHistory();
                    break;
                case "clear":
                case "7":
                    System.out.println("Clear command selected. Clearing command history...");
                    commandHistory.clear();
                    System.out.println("Command history cleared.");
                    break;
                case "help":
                case "8":
                    generalCavazosHelp();
                    break;
                case "exit":
                case "9":
                    System.out.println("Exit command selected. Exiting...");
                    System.out.println("Exiting General Cavazos Commander. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }

        scanner.close();
    }

    public static void issueCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n----- Select a Command to Issue -----");
        print(commandArray);

        System.out.print("Enter command number (0-" + (commandArray.length - 1) + "): ");

        String input = scanner.nextLine().trim();

        try {
            int index = Integer.parseInt(input);
            if (index >= 0 && index < commandArray.length) {
                String command = commandArray[index];
                commandHistory.push(command);
                redoStack.clear(); // Clear redo stack when a new command is issued
                System.out.println("Issued command: " + command);
            } else {
                System.out.println("Invalid command number. Please select a number between 0 and " + (commandArray.length - 1) + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    public static void undoCommand() {
        if (commandHistory.isEmpty()) {
            System.out.println("No commands to undo.");
        } else {
            String lastCommand = commandHistory.pop();
            redoStack.push(lastCommand);
            System.out.println("Undid command: " + lastCommand);
        }
    }

    public static void redoCommand() {
        if (redoStack.isEmpty()) {
            System.out.println("No commands to redo.");
        } else {
            String lastUndoneCommand = redoStack.pop();
            commandHistory.push(lastUndoneCommand);
            System.out.println("Redid command: " + lastUndoneCommand);
        }
    }

    public static void displayCommandHistory() {
        System.out.println("\n----- Command History -----");
        if (commandHistory.isEmpty()) {
            System.out.println("No commands have been issued yet.");
        } else {
            System.out.println("\n----- Command History -----");
            for (int i = 0; i < commandHistory.size(); i++) {
                System.out.printf("%d. %s\n", i, commandHistory.get(i));
            }
        }

        System.out.println("\n----- Redo Stack -----");
        if (redoStack.isEmpty()) {
            System.out.println("No commands available to redo.");
        } else {
            System.out.print("Commands Available to Redo:\n");
            // Convert redo stack to array for display
            Object[] redo = redoStack.toArray();
            for (int i = 0; i < redo.length; i++) {
                // Print redo commands with numbering starting from 1
                // Format %d to display the index starting from 1, and %s to display the command
                System.out.printf("%d. %s\n", i + 1, redo[i]);
            }
        }
    }

    public static void clearCommandHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to clear the command history? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes") || response.equals("y")) {
            commandHistory.clear();
            redoStack.clear();
            System.out.println("Command history cleared.");
        } else {
            System.out.println("Command history not cleared.");
        }
    }

    // randomly issue commands from General Cavazos
    public static void issueRandomCommand() {
        Random random = new Random();
        int randomIndex = random.nextInt(commandArray.length);
        String command = commandArray[randomIndex];
        commandHistory.push(command);
        redoStack.clear();
        System.out.println(">> Random command issued: " + command);
    }

    // print command array
    public static void print(String[] commandArray) {
        System.out.printf("Number\tCommand\n");
        System.out.printf("------\t---------------\n");
        for (int i = 0; i < commandArray.length; i++) {
            System.out.printf("%04d\t%s\n", i, commandArray[i]);
        }
    }

    // get array of commands
    public static String[] getCommandArray(JSONArray commandArray) {
        String[] arr = new String[commandArray.size()];

        // get names from json object
        for (int i = 0; i < commandArray.size(); i++) {
            String command = commandArray.get(i).toString();
            arr[i] = command;
        }
        return arr;
    }

    public static void displayAllCommands() {
        System.out.println("\n----- All Available Commands -----");
        print(commandArray);
    }

    public static void generalCavazosHelp() {
        System.out.println("\n===== General Cavazos Commander Help ======");
        System.out.println("This application allows you to issue commands from General Cavazos.");
        System.out.println("\nAvailable Commands:");
        System.out.println("  list   - Displays all available military commands");
        System.out.println("  issue  - Allows you to select and issue a command");
        System.out.println("  random  - Issues a random command from the list");
        System.out.println("  undo   - Undoes the last issued command");
        System.out.println("  redo   - Redoes the last undone command");
        System.out.println("  history - Shows the command history and redo stack");
        System.out.println("  clear  - Clears all command history");
        System.out.println("  help   - Shows this help message");
        System.out.println("  quit   - Exits the application");
        System.out.println("\nUsage Tips:");
        System.out.println("  - You can use either the command name or its number");
        System.out.println("  - Undo and Redo work with a stack-based history system");
        System.out.println("  - When you issue a new command after undo, the redo stack is cleared");
    }
}
