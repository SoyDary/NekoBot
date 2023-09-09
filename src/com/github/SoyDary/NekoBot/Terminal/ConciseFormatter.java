package com.github.SoyDary.NekoBot.Terminal;

import java.util.logging.Formatter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.github.SoyDary.NekoBot.Terminal.ColouredWriter.ChatColor;

public class ConciseFormatter extends Formatter
{

    private final DateFormat date = new SimpleDateFormat("HH:mm:ss");
    private boolean coloured = false;

    public ConciseFormatter(boolean coloured) {
    	this.coloured = coloured;
	}

	@Override
    public String format(LogRecord record) {
        StringBuilder formatted = new StringBuilder();
        formatted.append(ChatColor.WHITE).append(date.format(record.getMillis())).append(ChatColor.RESET);
        formatted.append( " [" );
        appendLevel(formatted, record.getLevel());
        formatted.append( "] " );
        formatted.append( formatMessage(record));
        if(record.getLoggerName().equals("NekoBot"))
        	formatted.append("\n\n");

        if ( record.getThrown() != null )
        {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }

        return formatted.toString();
    }

    private void appendLevel(StringBuilder builder, Level level) {
        if (!coloured) {
            builder.append(level.getLocalizedName());
            return;
        }

        ChatColor color;

        if (level == Level.INFO) {
            color = ChatColor.BLUE;
        } else if (level == Level.WARNING)
        {
            color = ChatColor.YELLOW;
        } else if (level == Level.SEVERE)
        {
            color = ChatColor.RED;
        } else
        {
            color = ChatColor.AQUA;
        }

        builder.append(color).append(level.getLocalizedName()).append(ChatColor.RESET);
    }
}