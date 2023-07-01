package com.github.tlind.refactoredturingmachine.components;

import com.github.tlind.refactoredturingmachine.TuringMachine;

public class StopCommand implements Command {
    @Override
    public void invoke(TuringMachine turingMachine) {
        turingMachine.setPage(CommandList.STOP);
    }
}
