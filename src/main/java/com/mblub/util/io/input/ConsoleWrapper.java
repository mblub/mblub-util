package com.mblub.util.io.input;

import java.io.Console;

public class ConsoleWrapper {
    private Console console;

    public ConsoleWrapper() {

    }

    public ConsoleWrapper(Console console) {
        this.console = console;
    }

    public String readLine(String prompt, Object... args) {
        return console.readLine(prompt, args);
    }
}
