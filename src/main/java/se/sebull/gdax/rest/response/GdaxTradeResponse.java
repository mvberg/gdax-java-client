package se.sebull.gdax.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.sebull.gdax.common.TradeSide;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxTradeResponse extends GdaxHttpResponse {

	public LocalDateTime time;
	public Integer trade_id;
	public Double price;
	public Double size;
	public TradeSide side;
	
	@Override
	public String toString() {
		return "GdaxTrade [time=" + time + ", trade_id=" + trade_id + ", price=" + price + ", size=" + size + ", side="
				+ side + "]";
	}
	
}
