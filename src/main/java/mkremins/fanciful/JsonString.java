package mkremins.fanciful;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

final class JsonString implements JsonRepresentedObject {
	private String value;

	public JsonString(CharSequence value) {
		this.value = value == null ? null : value.toString();
	}

	@Override
	public void writeJson(JsonWriter writer) throws IOException {
		writer.value(getValue());
	}

	@Override
	public JsonRepresentedObject copy() {
		return new JsonString(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
