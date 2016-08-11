package se.sebull.gdax.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxProductTickerResponse extends GdaxHttpResponse {

	public Long trade_id;
	public Double price;
	public Double size;
	public Double bid;
	public Double ask;
	public Double volume;
	public LocalDateTime time;
	
	@Override
	public String toString() {
		return "GdaxProductTicker [trade_id=" + trade_id + ", price=" + price + ", size=" + size + ", bid=" + bid
				+ ", ask=" + ask + ", volume=" + volume + ", time=" + time + "]";
	}
	
}
