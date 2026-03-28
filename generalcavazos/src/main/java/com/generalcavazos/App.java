package com.cavazos;

import java.util.Arrays;
import java.util.Random;

import org.json.simple.*;

public class App {

    public static void main(String[] args) {
        String fileName = "commands.json";

        // read commands from JSON file and read out commands into an array
        JSONArray commandJSONArray = JSONFile.readArray(fileName);

        if (commandJSONArray == null) {
            System.err.println("Count not load commands from resources: " + fileName);
            return;
        }

        // convert json array to string array
        String[] commandArray = getCommandArray(commandJSONArray);

        // output command array to console
        System.out.println(Arrays.toString(commandArray));

        startMenu(commandArray);
    }

    private static void startMenu(String[] commandArray) {
        throw new UnsupportedOperationException("Not supported yet.");
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

}
