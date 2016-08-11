package se.sebull.gdax.rest.response;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxOrderBookResponse extends GdaxHttpResponse {

	public long sequence;
	public Aggregate[] bids;
	public Aggregate[] asks;
	
	@JsonDeserialize(using = OrderBookAggregateDeserializer.class)
	public static class Aggregate {
		public double price;
		public double size;
		public int numOrders;
		
		@Override
		public String toString() {
			return "[price=" + price + ", size=" + size + ", numOrders=" + numOrders + "]";
		}
		
	}
	
	@Override
	public String toString() {
		return "GdaxOrderBook [sequence=" + sequence + ", bids=" + bids + ", asks=" + asks + "]";
	}
	
	public static class OrderBookAggregateDeserializer extends JsonDeserializer<Aggregate> {

		@Override
		public Aggregate deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			JsonNode json = parser.getCodec().readTree(parser);
			Aggregate aggregate = new Aggregate();
			aggregate.price = json.get(0).asDouble();
			aggregate.size = json.get(1).asDouble();
			aggregate.numOrders = json.get(2).asInt();
			return aggregate;
		}
		
	}
	
}
