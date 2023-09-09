package com.github.SoyDary.NekoBot;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

public class Launcher {

	static Main main;
	public static String bottoken;

	public static void main(String[] args) throws IOException {
		if (System.console() == null) {
			JOptionPane.showMessageDialog(null, "No se puede ejecutar así", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		bottoken = Arrays.stream(args)
			    .filter(a -> a.startsWith("token="))
			    .map(a -> a.split("token=")[1])
			    .findFirst()
			    .orElse(null);
		if(bottoken == null) {
			System.err.println(StringUtils.repeat("-", 75));
			System.err.println("Es necesario un \"token\" de un bot como argumento para poder ejecutarse:");
			System.err.println("java -jar NekoBot.jar 'token=TOKEN'");
			System.err.println(StringUtils.repeat("-", 75));
			System.out.println();
			return;
		}
		main = new Main();
		main.onEnable();
		String line;
		while (main.isRunning && (line = main.getConsoleReader().readLine(">")) != null) {
			if (!command(line))
				System.out.println("Comando desconocido!");
		}
		main.onDisable(true);
	}

	public static boolean command(String line) {
		line = clean(line);
		if (line.isEmpty())
			return true;

		if (line.equalsIgnoreCase("uptime")) {
			System.out.println("Tiempo activo: " + format(main.getUptime()));
			return true;
		}

		if (line.equalsIgnoreCase("stop")) {
			main.onDisable(true);
			return true;
		}

		if (line.equalsIgnoreCase("reloadcommands")) {
			main.getDiscordManager().loadCommands();
			return true;
		}

		if (line.equalsIgnoreCase("unregistercommands")) {
			main.getLogger().info("Eliminando comandos...");
			main.getDiscordManager().unregisterCommands();
			return true;
		}

		return false;
	}

	static String clean(String line) {
		if (line.replaceAll(" ", "").isEmpty())
			return "";
		return line;
	}

	static String format(long ms) {
		long days = TimeUnit.MILLISECONDS.toDays(ms);
		long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
		long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;
		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append(days).append("d ");
		if (hours > 0)
			sb.append(hours).append("h ");
		if (minutes > 0)
			sb.append(minutes).append("m ");
		if (seconds > 0)
			sb.append(seconds).append("s");
		return sb.toString().trim();
	}

}
