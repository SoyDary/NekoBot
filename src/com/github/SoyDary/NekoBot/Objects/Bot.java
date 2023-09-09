package com.github.SoyDary.NekoBot.Objects;

import java.util.concurrent.TimeUnit;

import com.github.SoyDary.NekoBot.Launcher;
import com.github.SoyDary.NekoBot.Main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot extends Thread {
	private JDA jda;
	Main main;
	JDAListener listener;
	
	public Bot(Main main) {
		super("JDA Main Thread");
		this.start();
		this.main = main;
	}
	
	public void registerEvents() {
		listener = new JDAListener(main);
		jda.addEventListener(listener);
	}

	public boolean initBot() {
	    stopBot();
	    try {
	    	
	    	this.jda = JDABuilder.createLight(Launcher.bottoken)
	    			.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGE_REACTIONS)
	    			.build();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }	  
	   return true;
	}
	
	  public JDA getJda() {
		  return this.jda;
	  }
	  
	  public void sendMessage(TextChannel channel, String message) {
		  sendMessage(channel, message, -1);
	  }
	  
	  public void sendMessage(TextChannel channel, String message, int duration) {
		  channel.sendMessage(message).queue(msg -> {
			  if(duration != -1) {
				  msg.delete().queueAfter(duration, TimeUnit.SECONDS);
				}
			});
	  }
	  
	  public void stopBot() {
		  if (jda != null) {
			  jda.shutdownNow();
		    	try {
					jda.awaitShutdown(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		  }
		  if(listener != null) jda.removeEventListener(listener);
	  }  
	  public String getInviteLink() {
		  return this.jda.getInviteUrl(new Permission[] {Permission.ADMINISTRATOR, Permission.USE_APPLICATION_COMMANDS});
	  }
}