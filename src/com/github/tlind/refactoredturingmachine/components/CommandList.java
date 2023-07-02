package com.github.tlind.refactoredturingmachine.components;

public class CommandList {
    public static int STOP = Integer.MAX_VALUE;
    public static int SECTION = Integer.MIN_VALUE;
    public static String SECTION_CHAR = "#";

    public static String MACHINE_SYMBOL = "";
    public static String FUTURE_SYMBOL = "?";
    public static String CONNECTOR = "";
    public static String DEFINE = "define";

    public static String POSITION = CONNECTOR + "position";
    public static String READ_TAPE = CONNECTOR + "readTape";
    public static String READ_AWARENESS = CONNECTOR + "awareness";

    public static String VAL = "const";

    public static String PAGE = "PAGE";
    public static String AWARENESS = "AWARENESS";

    public static String INIT_PART = "setValues";
    public static String SET_POS_PART = "setPosition";
    public static String PRINT_PART = "print";
    public static String CMD_PART = "setCommand";

    public static String STOP_PART = "stop";

    public static String SETTAPE_PART = "setTape";

    public static String MOVE_PART = "move";

    public static String GO_TO_PAGE_PART = "goToPage";

    public static String NEXT_SECTION_PART = "nextSection";

    public static String PREV_SECTION_PART = "prevSection";

    public static String RUN_PART = "run";


    public static String INITIALIZE_VALUES = MACHINE_SYMBOL + CONNECTOR + INIT_PART;
    public static String SET_POSITION = MACHINE_SYMBOL + CONNECTOR + SET_POS_PART;

    public static String PRINT = MACHINE_SYMBOL + CONNECTOR + PRINT_PART;
    public static String FUTURE_PRINT = CONNECTOR + PRINT_PART;
    public static String CMD = MACHINE_SYMBOL + CONNECTOR + CMD_PART;
    public static String RUN = MACHINE_SYMBOL + CONNECTOR + RUN_PART;


    public static String FUTURE_STOP = CONNECTOR + STOP_PART;
    public static String FUTURE_SETTAPE = CONNECTOR + SETTAPE_PART;
    public static String FUTURE_MOVE = CONNECTOR + MOVE_PART;
    public static String FUTURE_GOTOPAGE = CONNECTOR + GO_TO_PAGE_PART;
    public static String FUTURE_GOTONEXTSEC = CONNECTOR + NEXT_SECTION_PART;
    public static String FUTURE_GOTOPREVSEC = CONNECTOR + PREV_SECTION_PART;


    public static void refreshList() {
        INITIALIZE_VALUES = MACHINE_SYMBOL + CONNECTOR + INIT_PART;
        SET_POSITION = MACHINE_SYMBOL + CONNECTOR + SET_POS_PART;
        PRINT = MACHINE_SYMBOL + CONNECTOR + PRINT_PART;
        FUTURE_PRINT = CONNECTOR + PRINT_PART;
        CMD = MACHINE_SYMBOL + CONNECTOR + CMD_PART;
        FUTURE_STOP = CONNECTOR + STOP_PART;
        FUTURE_SETTAPE = CONNECTOR + SETTAPE_PART;
        FUTURE_MOVE = CONNECTOR + MOVE_PART;
        FUTURE_GOTOPAGE = CONNECTOR + GO_TO_PAGE_PART;
        FUTURE_GOTONEXTSEC = CONNECTOR + NEXT_SECTION_PART;
        FUTURE_GOTOPREVSEC = CONNECTOR + PREV_SECTION_PART;
        RUN = MACHINE_SYMBOL + CONNECTOR + RUN_PART;
    }
}
