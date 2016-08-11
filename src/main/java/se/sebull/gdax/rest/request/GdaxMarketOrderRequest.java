package se.sebull.gdax.rest.request;

import se.sebull.gdax.common.OrderType;
import se.sebull.gdax.common.TradeSide;

public class GdaxMarketOrderRequest extends GdaxOrderRequest {

	private final Double size;
	private final Double funds;
	
	public GdaxMarketOrderRequest(String product_id, TradeSide side, OrderType type, Double size,
			Double funds) {
		super(product_id, side, type);
		this.size = size;
		this.funds = funds;
	}

	public Double getSize() {
		return size;
	}

	public Double getFunds() {
		return funds;
	}

}
