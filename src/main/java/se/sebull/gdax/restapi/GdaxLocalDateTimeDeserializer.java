package se.sebull.gdax.restapi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GdaxLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	public static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.appendLiteral("T")
			.appendPattern("HH:mm:ss")
			.appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, true)
			.appendLiteral("Z")
			.toFormatter();

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return LocalDateTime.parse(parser.getCodec().readTree(parser).toString().replaceAll("\"", ""), dateTimeFormatter);
	}

}