package com.github.tlind.turingmachine.components;

public class TML_Exception {
    public TML_Exception(String message) {
        // ANSI escape code for red text
        String red = "\033[0;31m";
        String reset = "\033[0m"; // reset the color to default
        System.out.println(red + "TML Exception:" + reset);
        System.out.println(red + message + reset);
        System.exit(1);
    }

}
