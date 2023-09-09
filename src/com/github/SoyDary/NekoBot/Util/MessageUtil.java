package com.github.SoyDary.NekoBot.Util;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.AuthorInfo;
import net.dv8tion.jda.api.entities.MessageEmbed.Footer;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class MessageUtil {
	
	public enum Component {
		CONTENT,
		COLOR,
		AUTHOR,
		AUTHOR_IMG,
		AUTHOR_URL,
		TITLE,
		TITLE_URL,
		DESCRIPTION,
		THUMBNAIL,
		IMAGE,
		FOOTER,
		FOOTER_IMG	
	}
	
	public static MessageCreateData getMessage(Map<Component, String> components, List<LayoutComponent> actions) {
		MessageCreateBuilder message = new MessageCreateBuilder();
		String content = components.get(Component.CONTENT);	
		String color = components.get(Component.COLOR);
		String author = components.get(Component.AUTHOR);
		String author_url = components.get(Component.AUTHOR_URL);
		String author_img = components.get(Component.AUTHOR_IMG);
		String thumbnail = components.get(Component.THUMBNAIL);
		String title = components.get(Component.TITLE);
		String title_url = components.get(Component.TITLE_URL);	
		String description = components.get(Component.DESCRIPTION);	
		String image = components.get(Component.IMAGE);
		String footer = components.get(Component.FOOTER);
		String footer_img = components.get(Component.FOOTER_IMG);
		EmbedBuilder embed = (components.size() >= 2 || (components.size() == 1 && !components.containsKey(Component.CONTENT))) ? new EmbedBuilder() : null;
		if(content != null) message.setContent(content);
		if(embed != null) {
			embed.setAuthor(author, author_url, author_img);
			embed.setColor(color != null ? Color.decode('#'+color) : null);
			embed.setTitle(title, title_url);
			embed.setDescription(description);
			embed.setThumbnail(thumbnail);
			embed.setImage(image);
			if(footer_img != null) embed.setFooter(footer, footer_img); else embed.setFooter(footer);
			message.addEmbeds(embed.build());
		}
		if(actions != null) message.setComponents(actions);
		return message.build();
	}
	
	public static MessageCreateData editMessage(Map<Component, String> components, Message original) {
		Map<Component, String> original_components = new HashMap<Component, String>();
		original_components.put(Component.CONTENT, original.getContentRaw());
		MessageEmbed embed = original.getEmbeds().isEmpty() ? null : original.getEmbeds().get(0);
		if(embed.getAuthor() != null) {
			AuthorInfo author = embed.getAuthor();
			original_components.put(Component.AUTHOR, author.getName());
			original_components.put(Component.AUTHOR_IMG, author.getIconUrl());
			original_components.put(Component.AUTHOR_URL, author.getUrl());
		}
		if(embed.getFooter() != null) {
			Footer footer = embed.getFooter();
			original_components.put(Component.FOOTER, footer.getText());
			original_components.put(Component.FOOTER_IMG, footer.getIconUrl());
		}
		original_components.put(Component.COLOR, embed.getColor() != null ? Utils.hexColor(embed.getColor()) : null);
		original_components.put(Component.TITLE, embed.getTitle());
		original_components.put(Component.TITLE_URL, embed.getUrl());
		original_components.put(Component.DESCRIPTION, embed.getDescription());
		original_components.put(Component.THUMBNAIL, embed.getThumbnail() != null ? embed.getThumbnail().getUrl() : null);
		original_components.put(Component.IMAGE, embed.getImage() != null ? embed.getImage().getUrl() : null);
		for(Entry<Component, String> entry : components.entrySet()) {
			String v = entry.getValue();
			original_components.put(entry.getKey(), (v == null || v.isEmpty()) ? null : v);
		}
		return getMessage(original_components, original.getComponents());
	}
	
	public static MessageCreateData getMessageFromModal(List<ModalMapping>  modal) {
		Map<Component, String> components = new HashMap<Component, String>();
		for(ModalMapping value : modal) {
			String v = value.getAsString();
			components.put(Component.valueOf(value.getId()), (v == null || v.isEmpty()) ? null : v);
		}
		return getMessage(components, null);
	}
	
	public static MessageEditData getEditedMessageFromModal(List<ModalMapping> modal, Message original) {
		Map<Component, String> components = new HashMap<Component, String>();
		for(ModalMapping value : modal) {
			String v = value.getAsString();
			components.put(Component.valueOf(value.getId()), (v == null || v.isEmpty()) ? null : v);
		}
		return MessageEditData.fromCreateData(editMessage(components, original));
	}
	
}


