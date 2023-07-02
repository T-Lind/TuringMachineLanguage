package com.github.tlind.refactoredturingmachine;


import com.github.tlind.refactoredturingmachine.components.Command;
import com.github.tlind.refactoredturingmachine.components.CommandList;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void run() {
        while (page != STOP) {
            var commands = pages.get(page).get(awareness);
            if (commands != null && !commands.isEmpty()) {
                for (Command command : commands) {
                    command.invoke(this);
                }
            } else {
                throw new NullPointerException("No commands specified for page " + page + " and awareness " + awareness);
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
        return tape.get(position);
    }

    public void printTapeAt() {
        System.out.println("Current value: " + getTape());
    }

    public void printAwareness() {
        System.out.println("Current awareness: " + awareness);
    }

    public void printTape() {
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
            if (value != null) {
                if (value != SECTION)
                    printStr.append(tape.get(i)).append(" ");
                else {
                    printStr.append(SECTION_CHAR).append(" ");
                }
            } else {
                printStr.append("_ ");
            }
        }
        System.out.println(printStr);
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
}
