package se.sebull.gdax.rest.request;

import se.sebull.gdax.common.OrderType;
import se.sebull.gdax.common.TimeInForce;
import se.sebull.gdax.common.TradeSide;

public class GdaxLimitOrderRequest extends GdaxOrderRequest {

	private final double size;
	private final double price;
	private final TimeInForce time_in_force;
	private final String cancel_after;
	private final boolean post_only;

	public GdaxLimitOrderRequest(String product_id, TradeSide side, OrderType type, double size,
			double price, TimeInForce time_in_force, String cancel_after, boolean post_only) {
		super(product_id, side, type);
		this.size = size;
		this.price = price;
		this.time_in_force = time_in_force;
		this.cancel_after = cancel_after;
		this.post_only = post_only;
	}
	
	public double getSize() {
		return size;
	}

	public double getPrice() {
		return price;
	}

	public TimeInForce getTime_in_force() {
		return time_in_force;
	}

	public String getCancel_after() {
		return cancel_after;
	}

	public boolean isPost_only() {
		return post_only;
	}
	
}
