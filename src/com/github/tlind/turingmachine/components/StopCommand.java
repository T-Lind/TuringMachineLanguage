package com.github.tlind.turingmachine.components;

import com.github.tlind.turingmachine.TuringMachine;

public class StopCommand implements Command {
    @Override
    public void invoke(TuringMachine turingMachine) {
        turingMachine.setPage(CommandList.STOP);
    }
}
