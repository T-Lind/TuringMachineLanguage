package com.github.tlind.refactoredturingmachine;

import com.github.tlind.refactoredturingmachine.components.Command;
import com.github.tlind.refactoredturingmachine.components.CommandList;
import com.github.tlind.refactoredturingmachine.components.StopCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TuringInterpreter extends CommandList {

    private static TuringMachine turingMachine;

    public static void main(String[] args) throws Exception {
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


    public static void executeProgram(String program) throws Exception {
        // Initialize the turing machine
        turingMachine = new TuringMachine(new Integer(-1));

        Map<String, Integer> constants = new HashMap<>();


        // Remove all multiline comments
        Pattern multiLineCommentPattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        program = multiLineCommentPattern.matcher(program).replaceAll("");

        // Remove all single line comments
        Pattern singleLineCommentPattern = Pattern.compile("//.*?$", Pattern.MULTILINE);
        program = singleLineCommentPattern.matcher(program).replaceAll("");

        // Define a pattern for the const declaration
        Pattern constPattern = Pattern.compile(VAL + "\\s+(\\w+)\\s*=\\s*(-?\\d+)\\s*;");


        // Create a matcher with the const pattern and your program string
        Matcher constMatcher = constPattern.matcher(program);

        // While the matcher finds const declarations
        while (constMatcher.find()) {
            // Extract the constant name and its value
            String name = constMatcher.group(1);
            int value = Integer.parseInt(constMatcher.group(2));

            // Add the constant to the map
            constants.put(name, value);
        }

        Pattern anyFunctionPattern = Pattern.compile("(\\w+)\\((.*?)\\);", Pattern.DOTALL);

        Matcher commandMatcher = anyFunctionPattern.matcher(program);
        while (commandMatcher.find()) {
            String funcName = commandMatcher.group(1);
            String funcArgs = commandMatcher.group(2);

            if (funcName.equals(INITIALIZE_VALUES)) {
                String[] characterArray = funcArgs.split("\\s+"); // Split the characters by whitespace
                setValues(characterArray);
            } else if (funcName.equals(SET_POSITION)) {
                int position = Integer.parseInt(funcArgs);
                setPosition(position);
            } else if (funcName.equals(CMD)) {
                Matcher setCommandMatcher = Pattern.compile(AWARENESS + "=(-?\\d+),\\s*" + PAGE + "=(-?\\d+),\\s*(.*?)\\s*\\);", Pattern.DOTALL).matcher(funcArgs + ");");
                if (setCommandMatcher.find()) {
                    int awareness = Integer.parseInt(setCommandMatcher.group(1));
                    int page = Integer.parseInt(setCommandMatcher.group(2));
                    String commandDetails = setCommandMatcher.group(3);
                    Matcher innerCommandMatcher = Pattern.compile("\\?([^\\s(]+)\\((.*?)\\)").matcher(commandDetails);
                    while (innerCommandMatcher.find()) {
                        String innerCommandName = innerCommandMatcher.group(1);
                        String innerCommandArgs = innerCommandMatcher.group(2);

                        Command cmd;

                        if (innerCommandName.equals(FUTURE_STOP))
                            cmd = TuringMachine::stop;
                        else if (innerCommandName.equals(FUTURE_PRINT))
                            cmd = TuringMachine::printTape;
                        else if (innerCommandName.equals(POSITION))
                            cmd = TuringMachine::printPosition;
                        else if (innerCommandName.equals(READ_TAPE))
                            cmd = TuringMachine::printTapeAt;

                        else if (innerCommandName.equals(READ_AWARENESS))
                            cmd = TuringMachine::printAwareness;
                        else if (innerCommandName.equals(FUTURE_GOTONEXTSEC))
                            cmd = TuringMachine::goToNextSection;
                        else if (innerCommandName.equals(FUTURE_GOTOPREVSEC))
                            cmd = TuringMachine::goToPrevSection;
                        else if (innerCommandName.equals(FUTURE_GOTOPAGE)) {
                            final int value = getValueOrConstant(innerCommandArgs, constants);
                            cmd = (m) -> m.goToPage(value);
                        } else if (innerCommandName.equals(FUTURE_SETTAPE)) {
                            final int value = getValueOrConstant(innerCommandArgs, constants);
                            cmd = (m) -> m.setTape(value);
                        } else if (innerCommandName.equals(FUTURE_MOVE)) {
                            final int value = getValueOrConstant(innerCommandArgs, constants);
                            cmd = (m) -> m.move(value);
                        } else if (innerCommandName.equals(innerCommandArgs)) {
                            final int value = getValueOrConstant(innerCommandArgs, constants);
                            cmd = (m) -> m.goToPage(value);
                        } else {
                            throw new RuntimeException("Invalid command: " + innerCommandName);
                        }
                        turingMachine.addCommand(page, awareness, cmd, false);
                    }
                }
            } else if (funcName.equals(PRINT)) {
                String printString = funcArgs.replaceAll("^\"|\"$", ""); // remove quotation marks from the beginning and end of the string
                print(printString);
            } else if (funcName.equals(RUN)) {
                run();
            } else {
                throw new RuntimeException("Invalid command: " + funcName);
            }


        }
    }

    public static int getValueOrConstant(String arg, Map<String, Integer> constants) {
        if (constants.containsKey(arg)) {
            return constants.get(arg);
        } else {
            try {
                return Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid function argument: " + arg+" from "+constants);
            }
        }
    }


    public static void setValues(String[] data) throws Exception {
        if (!data[0].equals(SECTION_CHAR)) {
            throw new Exception("First value must be " + SECTION_CHAR + " to indicate the start of the tape.");
        }

        // Parse valuesString and set the values on the Turing machine.
        int[] values = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(SECTION_CHAR))
                values[i] = SECTION;
            else
                values[i] = Integer.parseInt(data[i]);
        }
        int pos = turingMachine.getPosition();
        if (data[0].equals(SECTION_CHAR)) {
            pos += 1;
        }
        turingMachine = new TuringMachine(pos, values);
    }

    public static void setPosition(int position) {
        if (turingMachine.getTapeAt(0) == SECTION) {
            position += 1;
        }
        turingMachine.move(position - turingMachine.getPosition());
        turingMachine.setAwareness(turingMachine.getTape());
    }


    public static void run() {
        // Run the Turing machine.
        turingMachine.run();
    }

    public static void print(String printString) {
        if (printString == null || Objects.equals(printString.strip(), "")) {
            turingMachine.printTape();
            return;
        }

        // Handle the print command.
        System.out.println(printString);
    }
}
