package mkremins.fanciful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.stream.JsonWriter;

final class MessagePart implements JsonRepresentedObject {
	static final BiMap<ChatColor, String> STYLES_TO_NAMES;
	static {
		ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
		for (final ChatColor style : ChatColor.values()) {
			if (!style.isFormat()) {
				continue;
			}
			String styleName;
			switch (style) {
			case MAGIC:
				styleName = "obfuscated";
				break;
			case UNDERLINE:
				styleName = "underlined";
				break;
			default:
				styleName = style.name().toLowerCase();
				break;
			}
			builder.put(style, styleName);
		}
		STYLES_TO_NAMES = builder.build();
	}
	ChatColor color = ChatColor.WHITE;
	List<ChatColor> styles = new ArrayList<>();
	String clickActionName = null;
	String clickActionData = null;
	String hoverActionName = null;
	JsonRepresentedObject hoverActionData = null;
	TextualComponent text = null;
	String insertionData = null;
	List<JsonRepresentedObject> translationReplacements = new ArrayList<>();

	MessagePart(TextualComponent text) {
		this.text = text;
	}

	MessagePart() {
		this.text = null;
	}

	boolean hasText() {
		return text != null;
	}

	public MessagePart copy() {
		MessagePart obj = new MessagePart();
		obj.color = color;
		obj.styles = new ArrayList<>(styles);
		obj.clickActionName = clickActionName;
		obj.clickActionData = clickActionData;
		obj.hoverActionName = hoverActionName;
		obj.hoverActionData = hoverActionData.copy();
		obj.text = text.copy();
		obj.insertionData = insertionData;
		translationReplacements = new ArrayList<>(translationReplacements);
		return obj;
	}

	public void writeJson(JsonWriter json) {
		try {
			json.beginObject();
			text.writeJson(json);
			json.name("color").value(color.name().toLowerCase());
			for (final ChatColor style : styles) {
				json.name(STYLES_TO_NAMES.get(style)).value(true);
			}
			if (clickActionName != null && clickActionData != null) {
				json.name("clickEvent").beginObject().name("action").value(clickActionName).name("value").value(clickActionData).endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.name("hoverEvent").beginObject().name("action").value(hoverActionName).name("value");
				hoverActionData.writeJson(json);
				json.endObject();
			}
			if (insertionData != null) {
				json.name("insertion").value(insertionData);
			}
			if (translationReplacements.size() > 0 && text != null && TextualComponent.isTranslatableText(text)) {
				json.name("with").beginArray();
				for (JsonRepresentedObject obj : translationReplacements) {
					obj.writeJson(json);
				}
				json.endArray();
			}
			json.endObject();
		} catch (IOException ignored) {
		}
	}
}
