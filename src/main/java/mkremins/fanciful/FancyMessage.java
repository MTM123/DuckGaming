package mkremins.fanciful;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

public class FancyMessage implements JsonRepresentedObject, Iterable<MessagePart> {
	private static final Pattern URL_PATTERN = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
	private static final JsonParser STRING_PARSER = new JsonParser();

	public static FancyMessage fromJson(String json) {
		JsonObject data = STRING_PARSER.parse(json).getAsJsonObject();
		JsonArray extra = data.getAsJsonArray("extra");
		FancyMessage ret = new FancyMessage();
		ret.messageParts.clear();
		for (JsonElement mPrt : extra) {
			MessagePart component = new MessagePart();
			JsonObject messagePart = mPrt.getAsJsonObject();
			for (Map.Entry<String, JsonElement> entry : messagePart.entrySet()) {
				if (TextualComponent.isTextKey(entry.getKey())) {
					Map<String, String> map = new HashMap<>();
					map.put("key", entry.getKey());
					if (entry.getValue().isJsonPrimitive()) {
						map.put("value", entry.getValue().getAsString());
					} else {
						for (Map.Entry<String, JsonElement> e : entry.getValue().getAsJsonObject().entrySet()) {
							map.put("value." + e.getKey(), e.getValue().getAsString());
						}
					}
					component.text = TextualComponent.deserialize(map);
				} else if (MessagePart.STYLES_TO_NAMES.inverse().containsKey(entry.getKey())) {
					if (entry.getValue().getAsBoolean()) {
						component.styles.add(MessagePart.STYLES_TO_NAMES.inverse().get(entry.getKey()));
					}
				} else if (entry.getKey().equals("color")) {
					component.color = ChatColor.valueOf(entry.getValue().getAsString().toUpperCase());
				} else if (entry.getKey().equals("clickEvent")) {
					JsonObject object = entry.getValue().getAsJsonObject();
					component.clickActionName = object.get("action").getAsString();
					component.clickActionData = object.get("value").getAsString();
				} else if (entry.getKey().equals("hoverEvent")) {
					JsonObject object = entry.getValue().getAsJsonObject();
					component.hoverActionName = object.get("action").getAsString();
					if (object.get("value").isJsonPrimitive()) {
						component.hoverActionData = new JsonString(object.get("value").getAsString());
					} else {
						component.hoverActionData = fromJson(object.get("value").toString());
					}
				} else if (entry.getKey().equals("insertion")) {
					component.insertionData = entry.getValue().getAsString();
				} else if (entry.getKey().equals("with")) {
					for (JsonElement object : entry.getValue().getAsJsonArray()) {
						if (object.isJsonPrimitive()) {
							component.translationReplacements.add(new JsonString(object.getAsString()));
						} else {
							component.translationReplacements.add(fromJson(object.toString()));
						}
					}
				}
			}
			ret.messageParts.add(component);
		}
		return ret;
	}

	public static FancyMessage fromLegacyText(String message) {
		List<MessagePart> components = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		MessagePart component = new MessagePart();
		Matcher matcher = URL_PATTERN.matcher(message);
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			if (c == ChatColor.COLOR_CHAR) {
				i++;
				c = message.charAt(i);
				if (c >= 'A' && c <= 'Z') {
					c += 32;
				}
				ChatColor format = ChatColor.getByChar(c);
				if (format == null) {
					continue;
				}
				if (builder.length() > 0) {
					MessagePart old = component;
					component = old.copy();
					old.text = TextualComponent.rawText(builder.toString());
					builder = new StringBuilder();
					components.add(old);
				}
				switch (format) {
				case BOLD:
				case ITALIC:
				case UNDERLINE:
				case STRIKETHROUGH:
				case MAGIC:
					component.styles.add(format);
					break;
				case RESET:
					format = ChatColor.WHITE;
				default:
					component = new MessagePart();
					component.color = format;
					break;
				}
				continue;
			}
			int pos = message.indexOf(' ', i);
			if (pos == -1) {
				pos = message.length();
			}
			if (matcher.region(i, pos).find()) {
				if (builder.length() > 0) {
					MessagePart old = component;
					component = old.copy();
					old.text = TextualComponent.rawText(builder.toString());
					builder = new StringBuilder();
					components.add(old);
				}
				MessagePart old = component;
				component = old.copy();
				String urlString = message.substring(i, pos);
				component.text = TextualComponent.rawText(urlString);
				component.clickActionName = "open_url";
				component.clickActionData = urlString.startsWith("http") ? urlString : "http://" + urlString;
				components.add(component);
				i += pos - i - 1;
				component = old;
				continue;
			}
			builder.append(c);
		}
		if (builder.length() > 0) {
			component.text = TextualComponent.rawText(builder.toString());
			components.add(component);
		}
		if (components.isEmpty()) {
			components.add(new MessagePart(TextualComponent.rawText("")));
		}
		return new FancyMessage(components);
	}

	private List<MessagePart> messageParts;
	private String jsonString;
	private boolean dirty;

	private FancyMessage(List<MessagePart> parts) {
		this.messageParts = parts;
		jsonString = null;
		dirty = false;
	}

	public FancyMessage(String firstPartText) {
		this(TextualComponent.rawText(firstPartText));
	}

	public FancyMessage(TextualComponent firstPartText) {
		messageParts = new ArrayList<>();
		messageParts.add(new MessagePart(firstPartText));
		jsonString = null;
		dirty = false;
	}

	public FancyMessage() {
		this((TextualComponent) null);
	}

	@Override
	public FancyMessage copy() {
		FancyMessage instance = new FancyMessage();
		instance.messageParts = new ArrayList<>(messageParts.size());
		for (int i = 0; i < messageParts.size(); i++) {
			instance.messageParts.add(i, messageParts.get(i).copy());
		}
		instance.dirty = false;
		instance.jsonString = null;
		return instance;
	}

	public FancyMessage apply(Consumer<FancyMessage> consumer) {
		consumer.accept(this);
		return this;
	}

	public <T> FancyMessage apply(T t, BiConsumer<FancyMessage, T> consumer) {
		consumer.accept(this, t);
		return this;
	}

	public FancyMessage text(String text) {
		MessagePart latest = latest();
		latest.text = TextualComponent.rawText(text);
		dirty = true;
		return this;
	}

	public FancyMessage text(TextualComponent text) {
		MessagePart latest = latest();
		latest.text = text;
		dirty = true;
		return this;
	}

	public FancyMessage color(ChatColor color) {
		if (!color.isColor()) {
			throw new IllegalArgumentException(color.name() + " is not a color");
		}
		latest().color = color;
		dirty = true;
		return this;
	}

	public FancyMessage style(ChatColor... styles) {
		for (final ChatColor style : styles) {
			if (!style.isFormat()) {
				throw new IllegalArgumentException(style.name() + " is not a style");
			}
		}
		latest().styles.addAll(Arrays.asList(styles));
		dirty = true;
		return this;
	}

	public FancyMessage file(String path) {
		onClick("open_file", path);
		return this;
	}

	public FancyMessage link(String url) {
		onClick("open_url", url);
		return this;
	}

	public FancyMessage suggest(String command) {
		onClick("suggest_command", command);
		return this;
	}

	public FancyMessage insert(String command) {
		latest().insertionData = command;
		dirty = true;
		return this;
	}

	public FancyMessage command(String command) {
		onClick("run_command", command);
		return this;
	}

	public FancyMessage achievementTooltip(String name) {
		onHover("show_achievement", new JsonString("achievement." + name));
		return this;
	}

	public FancyMessage tooltip(String text) {
		onHover("show_text", new JsonString(text));
		return this;
	}

	public FancyMessage tooltip(Iterable<String> lines) {
		tooltip(ArrayWrapper.toArray(lines, String.class));
		return this;
	}

	public FancyMessage tooltip(String... lines) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			builder.append(lines[i]);
			if (i != lines.length - 1) {
				builder.append('\n');
			}
		}
		tooltip(builder.toString());
		return this;
	}

	public FancyMessage formattedTooltip(FancyMessage text) {
		for (MessagePart component : text.messageParts) {
			if (component.clickActionData != null && component.clickActionName != null) {
				throw new IllegalArgumentException("The tooltip text cannot have click data.");
			} else if (component.hoverActionData != null && component.hoverActionName != null) {
				throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
			}
		}
		onHover("show_text", text);
		return this;
	}

	public FancyMessage formattedTooltip(FancyMessage... lines) {
		if (lines.length < 1) {
			onHover(null, null);
			return this;
		}
		FancyMessage result = new FancyMessage();
		result.messageParts.clear();
		for (int i = 0; i < lines.length; i++) {
			for (MessagePart component : lines[i]) {
				if (component.clickActionData != null && component.clickActionName != null) {
					throw new IllegalArgumentException("The tooltip text cannot have click data.");
				} else if (component.hoverActionData != null && component.hoverActionName != null) {
					throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
				}
				if (component.hasText()) {
					result.messageParts.add(component.copy());
				}
			}
			if (i != lines.length - 1) {
				result.messageParts.add(new MessagePart(TextualComponent.rawText("\n")));
			}
		}
		return formattedTooltip(result.messageParts.isEmpty() ? null : result);
	}

	public FancyMessage formattedTooltip(final Iterable<FancyMessage> lines) {
		return formattedTooltip(ArrayWrapper.toArray(lines, FancyMessage.class));
	}

	public FancyMessage then(String text) {
		return then(TextualComponent.rawText(text));
	}

	public FancyMessage then(TextualComponent text) {
		if (!latest().hasText()) {
			throw new IllegalStateException("previous message part has no text");
		}
		messageParts.add(new MessagePart(text));
		dirty = true;
		return this;
	}

	public FancyMessage then() {
		if (!latest().hasText()) {
			throw new IllegalStateException("previous message part has no text");
		}
		messageParts.add(new MessagePart());
		dirty = true;
		return this;
	}

	@Override
	public void writeJson(JsonWriter writer) throws IOException {
		if (messageParts.size() == 1) {
			latest().writeJson(writer);
		} else {
			writer.beginObject().name("text").value("").name("extra").beginArray();
			for (final MessagePart part : this) {
				part.writeJson(writer);
			}
			writer.endArray().endObject();
		}
	}

	public String exportToJson() {
		if (!dirty && jsonString != null) {
			return jsonString;
		}
		StringWriter string = new StringWriter();
		JsonWriter json = new JsonWriter(string);
		try {
			writeJson(json);
			json.close();
		} catch (IOException e) {
			throw new RuntimeException("invalid message");
		}
		jsonString = string.toString();
		dirty = false;
		return jsonString;
	}

	public String toOldMessageFormat() {
		StringBuilder result = new StringBuilder();
		for (MessagePart part : this) {
			result.append(part.color == null ? "" : part.color);
			for (ChatColor formatSpecifier : part.styles) {
				result.append(formatSpecifier);
			}
			result.append(part.text);
		}
		return result.toString();
	}

	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}

	private void onClick(final String name, final String data) {
		final MessagePart latest = latest();
		latest.clickActionName = name;
		latest.clickActionData = data;
		dirty = true;
	}

	private void onHover(final String name, final JsonRepresentedObject data) {
		final MessagePart latest = latest();
		latest.hoverActionName = name;
		latest.hoverActionData = data;
		dirty = true;
	}

	public Iterator<MessagePart> iterator() {
		return messageParts.iterator();
	}
}
