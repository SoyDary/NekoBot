package com.github.SoyDary.NekoBot.Terminal;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import jline.console.ConsoleReader;

public class CustomLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    public CustomLogger(String loggerName , ConsoleReader reader) {
        super(loggerName, null);
        setLevel(Level.ALL);
        setUseParentHandlers( false );

        ColouredWriter consoleHandler = new ColouredWriter(reader);
		consoleHandler.setLevel(Level.INFO);
		consoleHandler.setFormatter( new ConciseFormatter( true ) );
		addHandler( consoleHandler );

        dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue( record );
    }

    void doLog(LogRecord record) {
        super.log( record );
    }
}