package com.github.SoyDary.NekoBot.Managers;

import java.util.List;

import com.github.SoyDary.NekoBot.Main;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class DiscordManager {
	
	Main main;
	List<String> commands = List.of("embed", "Editar");
	
	public DiscordManager(Main main) {
		this.main = main;
	}
	
	public void loadCommands() {
		unregisterCommands();
		main.getLogger().info("Registrando comandos...");
		
		registerCommnand(Commands.slash("embed", "Crea un embed")
				.addOption(OptionType.STRING, "json", "Embed a partir de un json")
				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
		registerCommnand(Commands.message("Editar")
				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
		
	}
	
	public void unregisterCommands() {
		for (Command cmd : main.getJda().retrieveCommands().complete()) {
			if(commands.contains(cmd.getName())) 
				cmd.delete().queue(c -> {
					main.getLogger().info("[----] Eliminado el comando "+cmd.getName());
				});
		}
	}
	
	void registerCommnand(CommandData command) {
		try {
			main.getJda().upsertCommand(command).queue(cmd ->
			{
				main.getLogger().info("[^^^^] Registrado el comando "+command.getName());
			}, new ErrorHandler().handle(ErrorResponse.APPLICATION_COMMAND_NAME_ALREADY_EXISTS, (e)-> {
				main.getLogger().warning("El comando '"+command.getName()+"' no pudo ser registrado porque ya hay otro con ese nombre.");
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
