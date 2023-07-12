package com.github.tlind.turingmachine;


import com.github.tlind.turingmachine.components.Command;
import com.github.tlind.turingmachine.components.CommandList;
import com.github.tlind.turingmachine.components.TML_Exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class TuringMachine extends CommandList {

    private ArrayList<HashMap<Integer, ArrayList<Command>>> pages;

    private Integer awareness;
    private Integer page;

    private HashMap<Integer, Integer> tape;

    private Integer position;


    public TuringMachine(int startPos) {
        pages = new ArrayList<>();
        awareness = 0;
        page = 0;
        position = startPos;
        tape = new HashMap<>();
    }

    public TuringMachine(int... defaultValues) {
        pages = new ArrayList<>();
        awareness = 0;
        page = 0;
        tape = new HashMap<>();
        for (int i = 0; i < defaultValues.length; i++)
            tape.put(i, defaultValues[i]);
        position = 0;
    }

    public TuringMachine(Integer startPos, int... defaultValues) {
        pages = new ArrayList<>();
        awareness = defaultValues[1];
        page = 0;
        tape = new HashMap<>();
        for (int i = 0; i < defaultValues.length; i++)
            tape.put(i, defaultValues[i]);
        position = startPos;
    }

    public TuringMachine(Integer startPos, Integer numItemsTape) {
        pages = new ArrayList<>();
        awareness = 0;
        page = 0;
        tape = new HashMap<>();
        tape.put(0, SECTION);
        for (Integer i = 1; i < numItemsTape; i++)
            tape.put(i, 0);
        position = startPos;
    }


    public void addCommand(Integer page, Integer awareness, Command command) {
        addCommand(page, awareness, command, false);
    }

    public void addCommand(Integer page, Integer awareness, Command command, boolean autoStart) {
        if (page >= pages.size())
            pages.add(new HashMap<>());
        if (pages.get(page) == null || pages.get(page).get(awareness) == null) {
            pages.get(page).put(awareness, new ArrayList<>());
        }
        if (autoStart)
            pages.get(page).get(awareness).add((m -> m.setAwareness(m.getTape())));
        pages.get(page).get(awareness).add(command);
    }

    public int getPosition() {
        return position;
    }

    public void printPosition() {
        System.out.println("Position: " + position);
    }

    public void printPosition(String fmt) {
        printf("Position: " + position, fmt);
    }

    public void run() {
        while (page != STOP) {
            var commands = pages.get(page).get(awareness);
            if (commands != null && !commands.isEmpty()) {
                for (Command command : commands) {
                    command.invoke(this);
                }
            } else {
                if (awareness == NONE_INT)
                        new TML_Exception("No commands specified for page " + page + " and awareness '" + NONE_CHAR + "' (none, with NONE_INT=" + NONE_INT + ")");
                new TML_Exception("No commands specified for page " + page + " and awareness " + awareness);
            }
        }
    }

    public Integer setAwareness(Integer awareness) {
        var old = this.awareness;
        this.awareness = removeNull(awareness);
        return removeNull(old);
    }


    public void move(Integer moveAmount) {
        int startPos = position;
        if (moveAmount > 0) {
            for (int i = startPos; i < startPos + moveAmount; i++) {
                position++;
                while (getTape() == SECTION)
                    position++;
            }
        } else {
            for (int i = startPos; i > startPos + moveAmount; i--) {
                position--;
                while (getTape() == SECTION)
                    position--;
            }
        }
        awareness = getTape();
    }

    public void goToNextSection() {
        boolean nextSectionFound = false;
        while (!nextSectionFound) {
            nextSectionFound = getTape().equals(SECTION);
            position++;
        }

        awareness = getTape();
    }

    public void goToPrevSection() {
        boolean prevSectionFound = false;
        position -= 2;
        while (!prevSectionFound) {
            prevSectionFound = getTape().equals(SECTION);
            position--;
        }
        position += 2;

        awareness = getTape();
    }

    public void setTape(Integer value) {
        tape.put(position, value);
    }

    public Integer getTape() {
        Integer result = tape.get(position);
        if (result == null)
            return NONE_INT;
        return result;
    }

    public String getTapeString() {
        Integer item = getTape();
        if (item == NONE_INT)
            return NONE_CHAR;
        return item.toString();
    }

    public void printTapeAt() {
        System.out.println("Current value: " + getTapeString());
    }

    public void printTapeAt(String fmt) {
        printf("Current value: " + getTape(), fmt);
    }

    public void printAwareness() {
        System.out.println("Current awareness: " + awareness);
    }

    public void printAwareness(String fmt) {
        printf("Current awareness: " + awareness, fmt);
    }

    public void printTape() {
        printTape("yellow bold");
    }

    public void printTape(String fmt) {
        var printStr = new StringBuilder();
        Set<Integer> keys = tape.keySet();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (Integer value : keys) {
            if (value < min)
                min = value;
            if (value > max)
                max = value;
        }
        for (int i = min; i <= max; i++) {
            var value = tape.get(i);
            if (value != NONE_INT) {
                if (value != SECTION)
                    printStr.append(tape.get(i)).append(" ");
                else {
                    printStr.append(SECTION_CHAR).append(" ");
                }
            } else {
                printStr.append(NONE_CHAR).append(" ");
            }
        }
        printf(printStr.toString(), fmt);
    }

    public void goToPage(int pageNum) {
        page = pageNum;
    }

    public void stop() {
        goToPage(STOP);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private static Integer removeNull(Integer input) {
        if (input == null)
            return 0;
        return input;
    }

    public int getAwareness() {
        return awareness;
    }

    public int getTapeAt(int position) {
        return tape.get(position);
    }


    public static void printf(String text, String format) {
        text = text.replace("\\n", "\n").replace("\\t", "\t");
        if (format.equals("")) {
            if (text.contains("<<<")) {
                text = text.replace("<<<", "");
                System.out.print(text);
            } else {
                System.out.println(text);
            }
            return;
        }

        String[] parts = format.split(" ");
        String colorCode = "", styleCode = "";

        for (String part : parts) {
            switch (part.toLowerCase()) {
                case "black":
                    colorCode = "\u001B[30m";
                    break;
                case "red":
                    colorCode = "\u001B[31m";
                    break;
                case "green":
                    colorCode = "\u001B[32m";
                    break;
                case "yellow":
                    colorCode = "\u001B[33m";
                    break;
                case "blue":
                    colorCode = "\u001B[34m";
                    break;
                case "purple":
                    colorCode = "\u001B[35m";
                    break;
                case "cyan":
                    colorCode = "\u001B[36m";
                    break;
                case "white":
                    colorCode = "\u001B[37m";
                    break;
                case "bright_black":
                    colorCode = "\u001B[90m";
                    break;
                case "bright_red":
                    colorCode = "\u001B[91m";
                    break;
                case "bright_green":
                    colorCode = "\u001B[92m";
                    break;
                case "bright_yellow":
                    colorCode = "\u001B[93m";
                    break;
                case "bright_blue":
                    colorCode = "\u001B[94m";
                    break;
                case "bright_purple":
                    colorCode = "\u001B[95m";
                    break;
                case "bright_cyan":
                    colorCode = "\u001B[96m";
                    break;
                case "bright_white":
                    colorCode = "\u001B[97m";
                    break;
                case "bold":
                    styleCode = "\u001B[1m";
                    break;
                case "italic":
                    styleCode = "\u001B[3m";
                    break;
                case "underline":
                    styleCode = "\u001B[4m";
                    break;
                case "strikethrough":
                    styleCode = "\u001B[9m";
                    break;
                default:
                    new TML_Exception("Invalid print color/style option: " + part);
            }
        }
        if (text.contains("<<<")) {
            text = text.replace("<<<", "");
            System.out.print(colorCode + styleCode + text + "\u001B[0m");
        } else {
            System.out.println(colorCode + styleCode + text + "\u001B[0m");
        }
    }

    public void print(String printString, String fmt) {
        if (printString == null || Objects.equals(printString.strip(), "")) {
            printTape();
            return;
        }

        // Handle the print command.
        TuringMachine.printf(printString, fmt);
    }
}
