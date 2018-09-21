package mkremins.fanciful;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;

public abstract class TextualComponent {
	static TextualComponent deserialize(Map<String, String> map) {
		if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
			return ArbitraryTextTypeComponent.deserialize(map);
		} else if (map.size() >= 2 && map.containsKey("key") && !map.containsKey("value")) {
			return ComplexTextTypeComponent.deserialize(map);
		}
		return null;
	}

	static boolean isTextKey(String key) {
		return key.equals("translate") || key.equals("text") || key.equals("score") || key.equals("selector");
	}

	static boolean isTranslatableText(TextualComponent component) {
		return component instanceof ComplexTextTypeComponent && component.getKey().equals("translate");
	}

	public static TextualComponent rawText(String textValue) {
		return new ArbitraryTextTypeComponent("text", textValue);
	}

	public static TextualComponent localizedText(String translateKey) {
		return new ArbitraryTextTypeComponent("translate", translateKey);
	}

	public static TextualComponent objectiveScore(String scoreboardObjective) {
		return objectiveScore("*", scoreboardObjective);
	}

	public static TextualComponent objectiveScore(String playerName, String scoreboardObjective) {
		return new ComplexTextTypeComponent("score", ImmutableMap.<String, String>builder().put("name", playerName).put("objective", scoreboardObjective).build());
	}

	public static TextualComponent selector(String selector) {
		return new ArbitraryTextTypeComponent("selector", selector);
	}

	@Override
	public String toString() {
		return getReadableString();
	}

	public abstract String getKey();

	public abstract String getReadableString();

	public abstract TextualComponent copy();

	public abstract void writeJson(JsonWriter writer) throws IOException;

	private static final class ArbitraryTextTypeComponent extends TextualComponent {
		public static ArbitraryTextTypeComponent deserialize(Map<String, String> map) {
			return new ArbitraryTextTypeComponent(map.get("key"), map.get("value"));
		}

		private String key;
		private String value;

		public ArbitraryTextTypeComponent(String key, String value) {
			setKey(key);
			setValue(value);
		}

		@Override
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			Preconditions.checkArgument(value != null, "The value must be specified.");
			this.value = value;
		}

		@Override
		public TextualComponent copy() {
			return new ArbitraryTextTypeComponent(getKey(), getValue());
		}

		@Override
		public void writeJson(JsonWriter writer) throws IOException {
			writer.name(getKey()).value(getValue());
		}

		@Override
		public String getReadableString() {
			return getValue();
		}
	}

	private static final class ComplexTextTypeComponent extends TextualComponent {
		public static ComplexTextTypeComponent deserialize(Map<String, String> map) {
			String key = null;
			Map<String, String> values = new HashMap<String, String>();
			for (Map.Entry<String, String> valEntry : map.entrySet()) {
				if (valEntry.getKey().equals("key")) {
					key = valEntry.getValue();
				} else if (valEntry.getKey().startsWith("value.")) {
					values.put(valEntry.getKey().substring(6), valEntry.getValue());
				}
			}
			return new ComplexTextTypeComponent(key, values);
		}

		private String key;
		private Map<String, String> value;

		public ComplexTextTypeComponent(String key, Map<String, String> values) {
			setKey(key);
			setValue(values);
		}

		@Override
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
			this.key = key;
		}

		public Map<String, String> getValue() {
			return value;
		}

		public void setValue(Map<String, String> value) {
			Preconditions.checkArgument(value != null, "The value must be specified.");
			this.value = value;
		}

		@Override
		public TextualComponent copy() {
			return new ComplexTextTypeComponent(getKey(), getValue());
		}

		@Override
		public void writeJson(JsonWriter writer) throws IOException {
			writer.name(getKey());
			writer.beginObject();
			for (Map.Entry<String, String> jsonPair : value.entrySet()) {
				writer.name(jsonPair.getKey()).value(jsonPair.getValue());
			}
			writer.endObject();
		}

		@Override
		public String getReadableString() {
			return getKey();
		}
	}
}
