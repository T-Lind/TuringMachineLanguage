package com.github.tlind.refactoredturingmachine.components;

public class CommandList {
    public static int STOP = Integer.MAX_VALUE;
    public static int SECTION = Integer.MIN_VALUE;
    public static String SECTION_CHAR = "#";

    public static String MACHINE_SYMBOL = "";
    public static String FUTURE_SYMBOL = "?";
    public static String CONNECTOR = "";
    public static String DEFINE = "DEFINE";

    public static String POSITION = CONNECTOR + "position";
    public static String READ_TAPE = CONNECTOR + "readTape()";
    public static String READ_AWARENESS = CONNECTOR + "awareness";

    public static String DELIMITER_OPEN = "(";
    public static String DELIMITER_CLOSE = ")";
    public static String COMMENT = "//";
    public static String[] BLOCK_COMMENT = {"/*", "*/"};
    public static String INTEGERS = "-0123456789";
    public static String VAL = "const";

    public static String PAGE = "PAGE";
    public static String AWARENESS = "AWARENESS";

    public static String INITIALIZE_VALUES = MACHINE_SYMBOL + CONNECTOR + "setValues";
    public static String SET_POSITION = MACHINE_SYMBOL + CONNECTOR + "setPosition";
    public static String GENERATE_MACHINE = MACHINE_SYMBOL + CONNECTOR + "generate";

    public static String PRINT = MACHINE_SYMBOL + CONNECTOR + "print";
    public static String FUTURE_PRINT =  CONNECTOR + "print";
    public static String CMD = MACHINE_SYMBOL + CONNECTOR + "setCommand";


    public static String FUTURE_STOP = CONNECTOR + "stop";
    public static String FUTURE_SETTAPE = CONNECTOR + "setTape";
    public static String FUTURE_MOVE = CONNECTOR + "move";
    public static String FUTURE_GOTOPAGE = CONNECTOR + "goToPage";
    public static String FUTURE_GOTONEXTSEC = CONNECTOR + "nextSection";
    public static String FUTURE_GOTOPREVSEC = CONNECTOR + "prevSection";

    public static String RUN = MACHINE_SYMBOL + CONNECTOR + "run";

    public static void refreshList() {
        refreshList(true);
    }

    public static void refreshList(boolean beforeFunction) {
        if (beforeFunction) {
            INITIALIZE_VALUES = MACHINE_SYMBOL + CONNECTOR + "setValues";
            SET_POSITION = MACHINE_SYMBOL + CONNECTOR + "setPosition";
            GENERATE_MACHINE = MACHINE_SYMBOL + CONNECTOR + "generate";

            PRINT = MACHINE_SYMBOL + CONNECTOR + "print";
            FUTURE_PRINT = CONNECTOR + "print";
            CMD = MACHINE_SYMBOL + CONNECTOR + "setCommand";


            FUTURE_STOP = CONNECTOR + "stop";
            FUTURE_SETTAPE = CONNECTOR + "setTape";
            FUTURE_MOVE = CONNECTOR + "move";
            FUTURE_GOTOPAGE = CONNECTOR + "goToPage";

            RUN = MACHINE_SYMBOL + CONNECTOR + "run";

            FUTURE_GOTONEXTSEC = CONNECTOR + "nextSection";
            FUTURE_GOTOPREVSEC = CONNECTOR + "prevSection";

            POSITION = CONNECTOR + "position";
            READ_TAPE = CONNECTOR + "readTape";
            READ_AWARENESS = CONNECTOR + "awareness";
        }


    }
}
