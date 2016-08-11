package se.sebull.gdax.rest.response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import se.sebull.gdax.rest.response.GdaxHistoricRatesResponse.GdaxHistoricRatesDeserializer;

@JsonDeserialize(using = GdaxHistoricRatesDeserializer.class)
public class GdaxHistoricRatesResponse extends GdaxHttpResponse {

	public List<Candle> candles;
	
	public static class Candle {
		public Long time;
		public Double low;
		public Double high;
		public Double open;
        public Double close;
        public Double volume;

        @Override
		public String toString() {
			return "Candle [time=" + time + ", low=" + low + ", high=" + high + ", open=" + open + ", close=" + close
					+ ", volume=" + volume + "]";
		}

	}

	@Override
	public String toString() {
		return "GdaxHistoricRates [candles=" + candles + "]";
	}

	public static class GdaxHistoricRatesDeserializer extends JsonDeserializer<GdaxHistoricRatesResponse> {

		@Override
		public GdaxHistoricRatesResponse deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			JsonNode json = parser.getCodec().readTree(parser);
			return new GdaxHistoricRatesResponse();
		}
		
	}
	
}
