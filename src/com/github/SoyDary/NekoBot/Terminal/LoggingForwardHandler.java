package com.github.SoyDary.NekoBot.Terminal;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggingForwardHandler extends Handler {

    private final Logger logger;
    
    public LoggingForwardHandler(Logger logger) {
    	this.logger = logger;
    }
   
    @Override
    public void publish(LogRecord record) {	
        logger.log( record );
    }
        
    @Override
    public void flush(){
    }

    @Override
    public void close() throws SecurityException {
    }
}