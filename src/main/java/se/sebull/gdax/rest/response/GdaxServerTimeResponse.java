package se.sebull.gdax.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxServerTimeResponse extends GdaxHttpResponse {

	public LocalDateTime iso;
	public String epoch;
	
	@Override
	public String toString() {
		return "GdaxServerTime [iso=" + iso + ", epoch=" + epoch + "]";
	}

}
