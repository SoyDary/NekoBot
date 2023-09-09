package com.github.SoyDary.NekoBot.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.github.SoyDary.NekoBot.Main;

import jline.console.completer.Completer;


public class CommandCompleter implements Completer {
	
	List<String> commands = List.of("uptime", "stop", "reloadcommands", "unregistercommands");
	Main main;

    public CommandCompleter(Main main) {
    	this.main = main;
	}


	@Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        int lastSpace = buffer.lastIndexOf(' ');
        if (lastSpace == -1) {
            String lowerCase = buffer.toLowerCase( Locale.ROOT );
            candidates.addAll( commands.stream()
                    .filter((name) -> name.toLowerCase(Locale.ROOT).startsWith(lowerCase))
                    .collect(Collectors.toList()));
        } else {
            List<String> suggestions = new ArrayList<>();
            candidates.addAll( suggestions );
        }

        return (lastSpace == -1) ? cursor - buffer.length() : cursor - ( buffer.length() - lastSpace - 1 );
    }

}