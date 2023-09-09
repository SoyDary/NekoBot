package com.github.SoyDary.NekoBot.Objects;

import com.github.SoyDary.NekoBot.Main;
import com.github.SoyDary.NekoBot.Util.Utils;
import com.github.SoyDary.NekoBot.Util.MessageUtil;
import com.github.SoyDary.NekoBot.Util.MessageUtil.Component;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.AuthorInfo;
import net.dv8tion.jda.api.entities.MessageEmbed.Footer;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInput.Builder;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class JDAListener extends ListenerAdapter {

	Main main;

	public JDAListener(Main main) {
		this.main = main;
	}
	
    public void onModalInteraction(ModalInteractionEvent e) {
    	String id = e.getModalId();
    	if(id.equals("EMBED_CREATE")) {
    		try {  			
            	e.getChannel().sendMessage(MessageUtil.getMessageFromModal(e.getValues())).queue();
            	e.deferEdit().queue();
    		} catch (Exception ex) {
    			e.reply("```diff\n❗ Error al crear el mensaje ❗\n\n" + ex.getMessage().replaceAll("(?m)^", "- ")+"\n- (Más info en consola)```").setEphemeral(true).queue();
    			ex.printStackTrace();
    		}
    	}
    	if(id.startsWith("EMBED_EDIT")) {
    		try {
        		e.editMessage(MessageUtil.getEditedMessageFromModal(e.getValues(), e.getMessage())).queue();
    		} catch (Exception ex) {
    			e.reply("```diff\n❗ Error al editar el mensaje ❗\n\n" + ex.getMessage().replaceAll("(?m)^", "- ")+"\n- (Más info en consola)```").setEphemeral(true).queue();
    			ex.printStackTrace();
    		}
    	}	
    }
    
    public void onMessageContextInteraction(MessageContextInteractionEvent e) {
    	Message message = e.getTarget();
    	if(e.getName().equals("Editar")) {
    		if(!message.getAuthor().getId().equals(e.getJDA().getSelfUser().getId())) {
    			e.reply("> :x: Solo se pueden editar mensajes propios.").setEphemeral(true).queue();
    			return;
    		}
    		MessageCreateBuilder messageBuilder = MessageCreateBuilder.fromMessage(message);
    		/*
            StringSelectMenu menu = StringSelectMenu.create("EMBED_EDIT:"+message.getId())     
            .addOptions(
            		SelectOption.of("Contenido", "content").withDescription("Texto normal del mensaje").withEmoji(Emoji.fromUnicode("✏️")), 
            		SelectOption.of("Color", "color").withDescription("Color del embed").withEmoji(Emoji.fromUnicode("🌈")),
            		SelectOption.of("Autor", "author").withDescription("Autor del embed").withEmoji(Emoji.fromUnicode("👤")),
            		SelectOption.of("Título", "title").withDescription("Título del embed").withEmoji(Emoji.fromUnicode("🏷️")),
            		SelectOption.of("Descripción", "description").withDescription("Descripción del embed").withEmoji(Emoji.fromUnicode("📑")),
            		SelectOption.of("Miniatura", "thumbnail").withDescription("Imagen pequeña en la esquina del embed").withEmoji(Emoji.fromUnicode("🖼️")),
            		SelectOption.of("Imagen", "image").withDescription("Imagen principal del embed").withEmoji(Emoji.fromUnicode("🗺️")),
            		SelectOption.of("Píe", "footer").withDescription("Píe del embed").withEmoji(Emoji.fromUnicode("🥾"))).build();
            messageBuilder.addActionRow(menu);
            */
    		messageBuilder.addActionRow(
    				Button.primary("EMBED_EDIT:"+message.getId()+":content", "Contenido").withEmoji((Emoji.fromUnicode("✏️"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":color", "Color").withEmoji((Emoji.fromUnicode("🌈"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":author", "Autor").withEmoji((Emoji.fromUnicode("👤"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":description", "Descripción").withEmoji((Emoji.fromUnicode("📑"))));

    		messageBuilder.addActionRow(
    				Button.primary("EMBED_EDIT:"+message.getId()+":title", "Título").withEmoji((Emoji.fromUnicode("🏷️"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":thumbail", "Miniatura").withEmoji((Emoji.fromUnicode("🖼️"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":image", "Imagen").withEmoji((Emoji.fromUnicode("🗺️"))),
    				Button.primary("EMBED_EDIT:"+message.getId()+":footer", "Píe").withEmoji((Emoji.fromUnicode("🥾"))));

            messageBuilder.addActionRow((Button.success("EDIT_MESSAGE:"+message.getId(), "Confirmar").withEmoji(Emoji.fromUnicode("✅"))));
            e.reply(messageBuilder.build()).setEphemeral(true).queue();
           
    	}
    }
    
    public void onButtonInteraction(ButtonInteractionEvent e) {
    	String id = e.getButton().getId();
    	if(id.startsWith("EDIT_MESSAGE:")){		
    		String msg_id = id.split(":")[1];
    		Message original = e.getChannel().retrieveMessageById(msg_id).complete();
    		MessageCreateBuilder builder = MessageCreateBuilder.fromMessage(e.getMessage());
    		builder.setComponents(original.getComponents());
    		original.editMessage(MessageEditData.fromCreateData(builder.build())).queue();
    		e.editButton(e.getButton()).queue();
    	}
    	if(id.startsWith("EMBED_EDIT:")) {
        	Message message = e.getMessage();
        	String component_id = "EMBED_EDIT:"+id.split(":")[1];
        	switch(id.split(":")[2]) { 	
        	case "content" : {
        		Builder content = TextInput.create("CONTENT", "Contenido", TextInputStyle.PARAGRAPH)
        				.setMaxLength(4000).setRequired(false);
        		String txt = message.getContentRaw();
        		if(txt != null && !txt.isEmpty()) content.setValue(txt);
        		e.replyModal( Modal.create(component_id+"=CONTENT", "Editar contenido").addComponents(ActionRow.of(content.build())).build()).queue();
        		return;
        	}
        	case "color" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		
        		Builder color = TextInput.create(Component.COLOR.name(), "Color", TextInputStyle.SHORT)
        				.setMinLength(6).setMaxLength(6).setRequired(false);
        		if(embed != null && embed.getColor() != null) color.setValue(Utils.hexColor(embed.getColor()));
        		e.replyModal( Modal.create(component_id+"=COLOR", "Editar color").addComponents(ActionRow.of(color.build())).build()).queue();	
        		return;
        	} 
        	case "author" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;		 
        		Builder author = TextInput.create(Component.AUTHOR.name(), "Autor", TextInputStyle.SHORT).setRequired(false);
        		Builder author_img = TextInput.create(Component.AUTHOR_IMG.name(), "Avatar del autor", TextInputStyle.SHORT).setRequired(false);   
        		Builder author_url = TextInput.create(Component.AUTHOR_URL.name(), "Url del autor", TextInputStyle.SHORT).setRequired(false);
        		if(embed != null && embed.getAuthor() != null) {
        			AuthorInfo authorinfo = embed.getAuthor();   
            		if(authorinfo.getName() != null) author.setValue(authorinfo.getName());
            		if(authorinfo.getIconUrl() != null) author_img.setValue(authorinfo.getIconUrl());
            		if(authorinfo.getUrl() != null) author_url.setValue(authorinfo.getUrl());
        		}
        		e.replyModal( Modal.create(component_id+"=AUTHOR", "Editar autor").addComponents(ActionRow.of(author.build()), ActionRow.of(author_img.build()), ActionRow.of(author_url.build())).build()).queue();
        		return;
        	}   
        	case "title" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		Builder title = TextInput.create(Component.TITLE.name(), "Título", TextInputStyle.SHORT).setMaxLength(256).setRequired(false);
        		if(embed != null && embed.getTitle() != null) title.setValue(embed.getTitle());
        		Builder title_url = TextInput.create(Component.TITLE_URL.name(), "Url del título", TextInputStyle.SHORT).setRequired(false);
        		if(embed != null && embed.getUrl() != null) title_url.setValue(embed.getUrl());
        		e.replyModal( Modal.create(component_id+"=TITLE", "Editar título").addComponents(ActionRow.of(title.build()), ActionRow.of(title_url.build())).build()).queue();
        		return;
        	}   
        	case "description" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		Builder description = TextInput.create(Component.DESCRIPTION.name(), "Descripción", TextInputStyle.PARAGRAPH).setMaxLength(2048).setRequired(false);
        		if(embed != null && embed.getDescription() != null) description.setValue(embed.getDescription());
     
        		e.replyModal( Modal.create(component_id+"=DESCRIPTION", "Editar descripción").addComponents(ActionRow.of(description.build())).build()).queue();
        		return;
        	}   
        	case "thumbnail" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		Builder thumbnail = TextInput.create(Component.THUMBNAIL.name(), "Miniatura", TextInputStyle.SHORT).setRequired(false);
        		if(embed != null && embed.getThumbnail() != null) thumbnail.setValue(embed.getThumbnail().getUrl());
        		e.replyModal( Modal.create(component_id+"=THUMBNAIL", "Editar miniatura").addComponents(ActionRow.of(thumbnail.build())).build()).queue();
        		return;
        	}   
        	case "image" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		Builder image = TextInput.create(Component.IMAGE.name(), "Imagen", TextInputStyle.SHORT).setRequired(false);
        		if(embed != null && embed.getImage() != null) image.setValue(embed.getImage().getUrl());
        		e.replyModal( Modal.create(component_id+"=IMAGE", "Editar imagen").addComponents(ActionRow.of(image.build())).build()).queue();
        		return;
        	} 
        	case "footer" : {
        		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
        		Builder footer = TextInput.create(Component.FOOTER.name(), "Píe", TextInputStyle.SHORT).setRequired(false);
        		Builder footer_url = TextInput.create(Component.FOOTER_IMG.name(), "Imagen del píe", TextInputStyle.SHORT).setRequired(false);		
        		if(embed != null && embed.getFooter() != null) {
        			Footer footerinfo = embed.getFooter();
            		if(footerinfo.getText() != null) footer.setValue(footerinfo.getText());
            		if(footerinfo.getIconUrl() != null) footer_url.setValue(footerinfo.getIconUrl());
        		}
        		e.replyModal( Modal.create(component_id+"=FOOTER", "Editar píe").addComponents(ActionRow.of(footer.build()), ActionRow.of(footer_url.build())).build()).queue();
        		return;
        	}
        	}
    		
    	}
    	
    }
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
    	if(e.getName().equals("embed")) {
    		if(e.getOption("json") != null) {
    			String json = e.getOption("json").getAsString();
    			try {
        			EmbedBuilder builder = EmbedBuilder.fromData(DataObject.fromJson(json));
        			e.getChannel().sendMessageEmbeds(builder.build()).queue();
        			e.reply("> Mensaje enviado :thumbsup:").setEphemeral(true).queue();
    			} catch (Exception ex) {
    				e.reply("```diff\n❗ Error al construir el mensaje ❗\n\n" + ex.getMessage().replaceAll("(?m)^", "- ")+"\n- (Más info en consola)```").setEphemeral(true).addActionRow(Button.link("https://zira.bot/embedbuilder/", "Constructor de embeds").withEmoji(Emoji.fromUnicode("📋"))).queue();
    				ex.printStackTrace();
    			}
    			return;
    		}
    		TextInput title = TextInput
    				.create(Component.TITLE.name(), "título", TextInputStyle.SHORT)
    				.setRequired(false)	
    				.build();
    		TextInput thumbnail = TextInput
    				.create(Component.THUMBNAIL.name(), "url miniatura", TextInputStyle.SHORT)
    				.setRequired(false)	
    				.build();
    		TextInput description = TextInput
    				.create(Component.DESCRIPTION.name(), "descripción", TextInputStyle.PARAGRAPH)
    				.setRequired(false)
    				.setMaxLength(4000)
    				.build();
    		TextInput image = TextInput.create(Component.IMAGE.name(), "imagen", TextInputStyle.SHORT).setRequired(false).build();
    		TextInput footer = TextInput.create(Component.FOOTER.name(), "píe", TextInputStyle.SHORT).setRequired(false).build();
    		
    		Modal modal = Modal.create("EMBED_CREATE", "Crear embed").addComponents(
    						ActionRow.of(title),
    						ActionRow.of(thumbnail),
    						ActionRow.of(description),
    						ActionRow.of(image),
    						ActionRow.of(footer)).build();
    		e.replyModal(modal).queue();
    	}	
    }
    
    /*
    public void selectionMenu(StringSelectInteractionEvent e) {	
    	String component_id = e.getComponent().getId();
    	if(!component_id.startsWith("EMBED_EDIT:")) return;
    	String id = e.getValues().get(0);
    	Message message = e.getMessage();
    	switch(id) { 	
    	case "content" : {
    		Builder content = TextInput.create("CONTENT", "Contenido", TextInputStyle.PARAGRAPH)
    				.setMaxLength(4000).setRequired(false);
    		String txt = message.getContentRaw();
    		if(txt != null && !txt.isEmpty()) content.setValue(txt);
    		e.replyModal( Modal.create(component_id+"=CONTENT", "Editar contenido").addComponents(ActionRow.of(content.build())).build()).queue();
    		return;
    	}
    	case "color" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		
    		Builder color = TextInput.create(Component.COLOR.name(), "Color", TextInputStyle.SHORT)
    				.setMinLength(6).setMaxLength(6).setRequired(false);
    		if(embed != null && embed.getColor() != null) color.setValue(Utils.hexColor(embed.getColor()));
    		e.replyModal( Modal.create(component_id+"=COLOR", "Editar color").addComponents(ActionRow.of(color.build())).build()).queue();	
    		return;
    	} 
    	case "author" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;		 
    		Builder author = TextInput.create(Component.AUTHOR.name(), "Autor", TextInputStyle.SHORT).setRequired(false);
    		Builder author_img = TextInput.create(Component.AUTHOR_IMG.name(), "Avatar del autor", TextInputStyle.SHORT).setRequired(false);   
    		Builder author_url = TextInput.create(Component.AUTHOR_URL.name(), "Url del autor", TextInputStyle.SHORT).setRequired(false);
    		if(embed != null && embed.getAuthor() != null) {
    			AuthorInfo authorinfo = embed.getAuthor();   
        		if(authorinfo.getName() != null) author.setValue(authorinfo.getName());
        		if(authorinfo.getIconUrl() != null) author_img.setValue(authorinfo.getIconUrl());
        		if(authorinfo.getUrl() != null) author_url.setValue(authorinfo.getUrl());
    		}
    		e.replyModal( Modal.create(component_id+"=AUTHOR", "Editar autor").addComponents(ActionRow.of(author.build()), ActionRow.of(author_img.build()), ActionRow.of(author_url.build())).build()).queue();
    		return;
    	}   
    	case "title" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		Builder title = TextInput.create(Component.TITLE.name(), "Título", TextInputStyle.SHORT).setMaxLength(256).setRequired(false);
    		if(embed != null && embed.getTitle() != null) title.setValue(embed.getTitle());
    		Builder title_url = TextInput.create(Component.TITLE_URL.name(), "Url del título", TextInputStyle.SHORT).setRequired(false);
    		if(embed != null && embed.getUrl() != null) title_url.setValue(embed.getUrl());
    		e.replyModal( Modal.create(component_id+"=TITLE", "Editar título").addComponents(ActionRow.of(title.build()), ActionRow.of(title_url.build())).build()).queue();
    		return;
    	}   
    	case "description" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		Builder description = TextInput.create(Component.DESCRIPTION.name(), "Descripción", TextInputStyle.PARAGRAPH).setMaxLength(2048).setRequired(false);
    		if(embed != null && embed.getDescription() != null) description.setValue(embed.getDescription());
 
    		e.replyModal( Modal.create(component_id+"=DESCRIPTION", "Editar descripción").addComponents(ActionRow.of(description.build())).build()).queue();
    		return;
    	}   
    	case "thumbnail" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		Builder thumbnail = TextInput.create(Component.THUMBNAIL.name(), "Miniatura", TextInputStyle.SHORT).setRequired(false);
    		if(embed != null && embed.getThumbnail() != null) thumbnail.setValue(embed.getThumbnail().getUrl());
    		e.replyModal( Modal.create(component_id+"=THUMBNAIL", "Editar miniatura").addComponents(ActionRow.of(thumbnail.build())).build()).queue();
    		return;
    	}   
    	case "image" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		Builder image = TextInput.create(Component.IMAGE.name(), "Imagen", TextInputStyle.SHORT).setRequired(false);
    		if(embed != null && embed.getImage() != null) image.setValue(embed.getImage().getUrl());
    		e.replyModal( Modal.create(component_id+"=IMAGE", "Editar imagen").addComponents(ActionRow.of(image.build())).build()).queue();
    		return;
    	} 
    	case "footer" : {
    		MessageEmbed embed = !message.getEmbeds().isEmpty() ? message.getEmbeds().get(0) : null;
    		Builder footer = TextInput.create(Component.FOOTER.name(), "Píe", TextInputStyle.SHORT).setRequired(false);
    		Builder footer_url = TextInput.create(Component.FOOTER_IMG.name(), "Imagen del píe", TextInputStyle.SHORT).setRequired(false);		
    		if(embed != null && embed.getFooter() != null) {
    			Footer footerinfo = embed.getFooter();
        		if(footerinfo.getText() != null) footer.setValue(footerinfo.getText());
        		if(footerinfo.getIconUrl() != null) footer_url.setValue(footerinfo.getIconUrl());
    		}
    		e.replyModal( Modal.create(component_id+"=FOOTER", "Editar píe").addComponents(ActionRow.of(footer.build()), ActionRow.of(footer_url.build())).build()).queue();
    		return;
    	}
    	}
    	
    }
    */
    
}
