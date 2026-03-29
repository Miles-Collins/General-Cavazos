package com.cavazos;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import org.json.simple.*;

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
            System.out.println("3. undo   - Undo last command");
            System.out.println("4. redo   - Redo last undone command");
            System.out.println("5. quit   - Exit the program");
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
                case "undo":
                case "3":
                    System.out.println("Undo command selected");
                    undoCommand();
                    break;
                case "redo":
                case "4":
                    System.out.println("Redo command selected");
                    redoCommand();
                    break;
                case "quit":
                case "5":
                    System.out.println("Quit command selected. Exiting...");
                    System.out.println("Exiting General Cavazos Commander. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }

        scanner.close();
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

    // randomly issue commands from General Cavazos
    public static void randomCommand(String[] commandArray, int numCommand) {
        Random rand = new Random();
        System.out.printf("Number\tCommand\n");
        System.out.printf("------\t---------------\n");
        for (int i = 0; i < numCommand; i++) {
            int randIndex = rand.nextInt(commandArray.length);
            System.out.printf("%04d\t%s\n", i, commandArray[randIndex]);
        }
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
