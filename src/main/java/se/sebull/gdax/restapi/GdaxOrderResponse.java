package se.sebull.gdax.restapi;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Reused in API for creating, listing and getting orders
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxOrderResponse {

	public String id;
	public Double price;
	public Double size;
	public String product_id;
	public TradeSide side;
	public OrderStatus status;
	public boolean settled;
	public Double filled_size;
	public Double executed_value;
	public Double fill_fees;
	public LocalDateTime created_at;
	
	// For done orders
	public LocalDateTime done_at;
	public String done_reason;
	
	@Override
	public String toString() {
		return "GdaxOrderResponse [id=" + id + ", price=" + price + ", size=" + size + ", product_id=" + product_id
				+ ", side=" + side + ", status=" + status + ", settled=" + settled + ", filled_size=" + filled_size
				+ ", executed_value=" + executed_value + ", fill_fees=" + fill_fees + ", created_at=" + created_at
				+ ", done_at=" + done_at + ", done_reason=" + done_reason + "]";
	}
	
}
