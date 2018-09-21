package mkremins.fanciful;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

interface JsonRepresentedObject {
	void writeJson(JsonWriter writer) throws IOException;

	JsonRepresentedObject copy();
}
