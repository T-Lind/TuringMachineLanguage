package com.github.tlind.turingmachine;

import com.github.tlind.turingmachine.components.Command;
import com.github.tlind.turingmachine.components.StopCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class RefactoredTuringInterpreter {

    private static TuringMachine turingMachine;

    public static void main(String[] args) throws FileNotFoundException {
        String program = readProgram(args);
        executeProgram(program);
    }

    public static String readProgram(String[] args) throws FileNotFoundException {
        StringBuilder readString = new StringBuilder();

        if (args.length == 0) { // if no arguments, read from the terminal
            Scanner terminalScanner = new Scanner(System.in);
            String line = "";
            while (!(line = terminalScanner.nextLine()).equals("RUN")) {
                readString.append(line).append("\n");
            }
            terminalScanner.close();
        } else { // else, read from the file specified in the command-line arguments
            StringBuilder summedArgs = new StringBuilder();
            for (String arg : args)
                summedArgs.append(arg).append(" ");

            File file = new File(summedArgs.toString());
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                var line = fileScanner.nextLine();
                readString.append(line).append("\n");
            }
            fileScanner.close();
        }

        return readString.toString();
    }


    public static void executeProgram(String program) {
        // Initialize the turing machine
        turingMachine = new TuringMachine(0);

        // Define the patterns for the commands
        Pattern setValuesPattern = Pattern.compile("setValues\\(# (.*?)#\\)");
        Pattern setPositionPattern = Pattern.compile("setPosition\\((\\d+)\\)");
        Pattern setCommandPattern = Pattern.compile("setCommand\\(\\s*AWARENESS=(\\d), PAGE=(\\d), (.*?)\\);");
        Pattern runPattern = Pattern.compile("run\\(\\);");
        Pattern printPattern = Pattern.compile("print\\(\"(.*?)\"\\);");

        // Matcher for setValues command
        Matcher setValuesMatcher = setValuesPattern.matcher(program);
        while (setValuesMatcher.find()) {
            String valuesString = setValuesMatcher.group(1);
            setValues(valuesString);
        }

        // Matcher for setPosition command
        Matcher setPositionMatcher = setPositionPattern.matcher(program);
        while (setPositionMatcher.find()) {
            int position = Integer.parseInt(setPositionMatcher.group(1));
            setPosition(position);
        }

        // Matcher for setCommand command
        Matcher setCommandMatcher = setCommandPattern.matcher(program);
        while (setCommandMatcher.find()) {
            int awareness = Integer.parseInt(setCommandMatcher.group(1));
            int page = Integer.parseInt(setCommandMatcher.group(2));
            String commandDetails = setCommandMatcher.group(3);
            setCommand(awareness, page, commandDetails);
        }

        // Matcher for run command
        Matcher runMatcher = runPattern.matcher(program);
        while (runMatcher.find()) {
            run();
        }

        // Matcher for print command
        Matcher printMatcher = printPattern.matcher(program);
        while (printMatcher.find()) {
            String printString = printMatcher.group(1);
            print(printString);
        }
    }

    public static void setValues(String valuesString) {
        // Parse valuesString and set the values on the Turing machine.
        String[] valuesArray = valuesString.split(", ");
        int[] values = new int[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            values[i] = Integer.parseInt(valuesArray[i]);
        }
        turingMachine = new TuringMachine(values);
    }

    public static void setPosition(int position) {
        // Set the position on the Turing machine.
        turingMachine.setPosition(position);
    }

    public static void setCommand(int awareness, int page, String commandDetails) {
        System.out.println("setting command...");
        // Parse the command details and set the command on the Turing machine.
        // commandDetails should be in the format: ?m.move(1);m.setAwareness(0); and so on
        Command command = m -> {
            String[] instructions = commandDetails.split("\n");
            for (String instruction : instructions) {
                // Remove leading question mark and parentheses
                instruction = instruction.trim().replace("?", "").replace("(", "").replace(")", "");
                String[] parts = instruction.split("\\.");
                String methodName = parts[0];
                System.out.println("method name: " + methodName);

                switch (methodName) {
                    case "stop":
                        turingMachine.addCommand(page, awareness, new StopCommand());
                        break;
                    case "move":
                        String direction = parts[1];
                        if (direction.equals("forward")) {
                            m.move(1);
                        } else if (direction.equals("backward")) {
                            m.move(-1);
                        }
                        break;
                    case "setAwareness":
                        m.setAwareness(Integer.parseInt(parts[1]));
                        break;
                    case "goToPage":
                        m.goToPage(Integer.parseInt(parts[1]));
                        break;
                    case "setTape":
                        m.setTape(Integer.parseInt(parts[1]));
                        break;
                    case "print":
                        if (parts[1].equals("readTape")) {
                            System.out.println("Current value: " + m.getTape());
                        } else {
                            System.out.println(parts[1]);
                        }
                        break;
                    // add more cases as needed for other methods in TuringMachine
                }
            }
        };

        turingMachine.addCommand(page, awareness, command);
    }


    public static void run() {
        // Run the Turing machine.
        turingMachine.run();
    }

    public static void print(String printString) {
        // Handle the print command.
        System.out.println(printString);
    }
}
