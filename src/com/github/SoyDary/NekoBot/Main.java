package com.github.SoyDary.NekoBot;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

import com.github.SoyDary.NekoBot.Managers.DiscordManager;
import com.github.SoyDary.NekoBot.Objects.Bot;
import com.github.SoyDary.NekoBot.Terminal.CommandCompleter;
import com.github.SoyDary.NekoBot.Terminal.CustomLogger;
import com.github.SoyDary.NekoBot.Terminal.LoggingForwardHandler;
import com.github.SoyDary.NekoBot.Terminal.LoggingOutputStream;

import jline.Terminal;
import jline.console.ConsoleReader;
import kotlin.io.LineReader;
import net.dv8tion.jda.api.JDA;


public class Main {
	long startTime = System.currentTimeMillis();
	private Bot bot;
	private JDA jda;
    public Terminal terminal;
    public LineReader reader;
    public ConsoleReader consoleReader;
    public boolean isRunning;
    private final Logger mainLogger;
    public static Main instance;
    private static final Logger logger = Logger.getLogger("NekoBot");   
    public List<String> new_responses = new ArrayList<String>();
    private DiscordManager discordManager;
      
	public Main() throws IOException{ 
		this.isRunning = true;
		instance = this;
        System.setProperty("library.jansi.version", "JDA");	
        AnsiConsole.systemInstall();
        consoleReader = new ConsoleReader();
        consoleReader.setExpandEvents(false);
        consoleReader.addCompleter(new CommandCompleter(this));
        mainLogger = new CustomLogger(this.getClass().getName(), consoleReader);   
        Logger rootLogger = Logger.getLogger("");
        for(Handler handler : rootLogger.getHandlers()) rootLogger.removeHandler(handler);     
        rootLogger.addHandler(new LoggingForwardHandler(mainLogger));
        System.setErr(new PrintStream(new LoggingOutputStream(mainLogger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(mainLogger, Level.INFO), true));
	}	
    
    public void onEnable() {
    	getLogger().info("Iniciando bot...");
    	loadBot();	
    	if(jda == null) {
    		isRunning = false;
    		return;
    	}
    	discordManager = new DiscordManager(this);
    		
    }
    
    public void onDisable(boolean exit) {
    	getLogger().info("Apagando bot...");
    	bot.stopBot();
    	isRunning = false;
    	System.exit(0);
    }
    
    public void reload() {
    	onDisable(false);
    	onEnable();
    }
    

	void loadBot() {
		bot = new Bot(this);
		if(bot.initBot()) {
			jda = bot.getJda();
			try {
				jda.awaitReady();
				getLogger().info("["+jda.getSelfUser().getAsTag()+"] JDA iniciada en "+(System.currentTimeMillis() - startTime)+"ms");				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bot.registerEvents();
		} 
	}
    
	public static Main getInstance() {
		return instance;
	}
	
	public DiscordManager getDiscordManager() {
		return this.discordManager;	
	}
	
	public ConsoleReader getConsoleReader() {
		return this.consoleReader;
	}
	
	public long getUptime() {
		return System.currentTimeMillis() - startTime;
	}
	
	public JDA getJda() {
		return jda;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public Bot getBot() {
		return bot;
	}
}