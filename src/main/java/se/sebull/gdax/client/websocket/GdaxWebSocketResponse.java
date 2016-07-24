package se.sebull.gdax.client.websocket;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import se.sebull.gdax.restapi.OrderType;
import se.sebull.gdax.restapi.TradeSide;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxWebSocketResponse {

	public MessageType type;
	public LocalDateTime time;
	public String product_id;
	public Integer sequence;
	public String order_id;
	public Double size;
	public Double price;
	public TradeSide side;
	public OrderType order_type;
	
	public Double remaining_size;

	// Market orders
	public Double funds;
	
	// Order matching
	public String maker_order_id;
	public String taker_order_id;
	
	// Order changes
	public Double new_funds;
	public Double old_funds;

	// Errors only
	public String message;

	@Override
	public String toString() {
		return "GdaxWebSocketResponse [type=" + type + ", time=" + time + ", product_id=" + product_id + ", sequence="
				+ sequence + ", order_id=" + order_id + ", size=" + size + ", price=" + price + ", side=" + side
				+ ", order_type=" + order_type + ", remaining_size=" + remaining_size + ", funds=" + funds
				+ ", maker_order_id=" + maker_order_id + ", taker_order_id=" + taker_order_id + ", new_funds="
				+ new_funds + ", old_funds=" + old_funds + ", message=" + message + "]";
	}
	
}
