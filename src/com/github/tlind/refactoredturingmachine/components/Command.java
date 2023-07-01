package com.github.tlind.refactoredturingmachine.components;


import com.github.tlind.refactoredturingmachine.TuringMachine;

public interface Command {
    void invoke(TuringMachine m);
}
