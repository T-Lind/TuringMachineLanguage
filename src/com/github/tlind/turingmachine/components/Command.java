package com.github.tlind.turingmachine.components;


import com.github.tlind.turingmachine.TuringMachine;

public interface Command {
    void invoke(TuringMachine m);
}
