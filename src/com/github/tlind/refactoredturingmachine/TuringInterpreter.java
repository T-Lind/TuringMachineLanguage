package com.github.tlind.refactoredturingmachine;

import com.github.tlind.refactoredturingmachine.components.Command;
import com.github.tlind.refactoredturingmachine.components.CommandList;
import com.github.tlind.refactoredturingmachine.components.StopCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TuringInterpreter extends CommandList {

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

        // Remove all multiline comments
        Pattern multiLineCommentPattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        program = multiLineCommentPattern.matcher(program).replaceAll("");

        // Remove all single line comments
        Pattern singleLineCommentPattern = Pattern.compile("//.*?$", Pattern.MULTILINE);
        program = singleLineCommentPattern.matcher(program).replaceAll("");


        // Define the patterns for the commands
        Pattern setValuesPattern = Pattern.compile(INITIALIZE_VALUES + "\\((#.*?)\\)");
        Pattern setPositionPattern = Pattern.compile(SET_POSITION + "\\((\\d+)\\)");
        Pattern setCommandPattern = Pattern.compile(CMD + "\\(\\s*" + AWARENESS + "=(\\d), " + PAGE + "=(\\d),\\s*(.*?)\\s*\\);", Pattern.DOTALL);
        Pattern runPattern = Pattern.compile("(?<!\\Q" + FUTURE_SYMBOL + "\\E)" + Pattern.quote(RUN) + "\\(\"(.*?)\"\\);");
        Pattern printPattern = Pattern.compile("(?<!\\Q" + FUTURE_SYMBOL + "\\E)" + Pattern.quote(PRINT) + "\\(\\s*\\);|(?<!\\Q" + FUTURE_SYMBOL + "\\E)" + PRINT + "\\(\\s*\"(.*?)\"\\s*\\);");

        // Matcher for setValues command
        Matcher setValuesMatcher = setValuesPattern.matcher(program);
        if (setValuesMatcher.find()) {
            String characters = setValuesMatcher.group(1); // Retrieve the characters within the parentheses
            String[] characterArray = characters.split("\\s+"); // Split the characters by whitespace
            setValues(characterArray);
        }


        // Matcher for setPosition command
        Matcher setPositionMatcher = setPositionPattern.matcher(program);
        while (setPositionMatcher.find()) {
            int position = Integer.parseInt(setPositionMatcher.group(1));
            setPosition(position);
        }

        Matcher setCommandMatcher = setCommandPattern.matcher(program);
        while (setCommandMatcher.find()) {
            int awareness = Integer.parseInt(setCommandMatcher.group(1));
            int page = Integer.parseInt(setCommandMatcher.group(2));
            String commandDetails = setCommandMatcher.group(3);
            Pattern commandPattern = Pattern.compile("\\?([^\\s(]+)\\((.*?)\\)");
            Matcher commandMatcher = commandPattern.matcher(commandDetails);
            while (commandMatcher.find()) {
                String commandName = commandMatcher.group(1);
                String commandArgs = commandMatcher.group(2);
                Command cmd;

                if (commandName.equals(FUTURE_STOP))
                    cmd = (m) -> m.stop();
                else if (commandName.equals(FUTURE_PRINT))
                    cmd = (m) -> m.printTape();
                else if (commandName.equals(POSITION))
                    cmd = (m) -> m.printPosition();
                else if (commandName.equals(READ_TAPE))
                    cmd = (m) -> m.printTapeAt();
                else if (commandName.equals(READ_AWARENESS))
                    cmd = (m) -> m.printAwareness();
                else if (commandName.equals(FUTURE_GOTONEXTSEC))
                    cmd = (m) -> m.goToNextSection();
                else if (commandName.equals(FUTURE_GOTOPREVSEC))
                    cmd = (m) -> m.goToPrevSection();
                else if (commandName.equals(FUTURE_SETTAPE)) {
                    final int value = Integer.parseInt(commandArgs);
                    cmd = (m) -> m.setTape(value);
                } else if (commandName.equals(FUTURE_MOVE)) {
                    final int value = Integer.parseInt(commandArgs);
                    cmd = (m) -> m.move(value);
                } else if (commandName.equals(FUTURE_GOTOPAGE)) {
                    final int value = Integer.parseInt(commandArgs);
                    cmd = (m) -> m.goToPage(value);
                } else {
                    System.out.println("Unknown command: " + commandName);
                    cmd = (m) -> {
                    };
                }


                turingMachine.addCommand(page, awareness, cmd, false);
            }
        }


        // Matcher for run command
        Matcher runMatcher = runPattern.matcher(program);
        while (runMatcher.find()) {
            run();
        }

        // Matcher for print command
        Matcher printMatcher = printPattern.matcher(program);
        while (printMatcher.find()) {
            String printString = "";
            try {
                printString = printMatcher.group(1);
            } catch (Exception e) {
            }
            print(printString);
        }
    }

    public static void setValues(String[] data) {
        // Parse valuesString and set the values on the Turing machine.
        int[] values = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(SECTION_CHAR))
                values[i] = SECTION;
            else
                values[i] = Integer.parseInt(data[i]);
        }
        turingMachine = new TuringMachine(turingMachine.getPosition(), values);
    }

    public static void setPosition(int position) {
        // Set the position on the Turing machine.
        turingMachine.setPosition(position);
    }


    public static void run() {
        // Run the Turing machine.
        turingMachine.run();
    }

    public static void print(String printString) {
        if (printString == null) {
            turingMachine.printTape();
            return;
        }

        // Handle the print command.
        System.out.println(printString);
    }
}
