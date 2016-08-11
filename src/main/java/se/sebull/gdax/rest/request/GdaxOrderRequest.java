package se.sebull.gdax.rest.request;

import java.util.Objects;

import se.sebull.gdax.common.OrderType;
import se.sebull.gdax.common.TimeInForce;
import se.sebull.gdax.common.TradeSide;

public abstract class GdaxOrderRequest {

	private final String product_id;
	private final TradeSide side;
	private final OrderType type;

	public GdaxOrderRequest(String product_id, TradeSide side, OrderType type) {
		this.product_id = Objects.requireNonNull(product_id);
		this.side = Objects.requireNonNull(side);
		this.type = Objects.requireNonNull(type);
	}

	public static LimitOrderBuilder limitOrder(String product_id, TradeSide side, OrderType type) {
		return new LimitOrderBuilder(product_id, side, type);
	}
	
	public static MarketOrderBuilder marketOrder(String product_id, TradeSide side, OrderType type) {
		return new MarketOrderBuilder(product_id, side, type);
	}
	
	public String getProduct_id() {
		return product_id;
	}

	public TradeSide getSide() {
		return side;
	}

	public OrderType getType() {
		return type;
	}

	public static class LimitOrderBuilder {
		private final String product_id;
		private final TradeSide side;
		private final OrderType type;
		private TimeInForce time_in_force;
		private String cancel_after;
		private Boolean post_only;
		public LimitOrderBuilder(String product_id, TradeSide side, OrderType type) {
			this.product_id = product_id;
			this.side = side;
			this.type = type;
		}
		public LimitOrderBuilder withTimeInForce(TimeInForce timeInForce) {
			this.time_in_force = timeInForce;
			return this;
		}
		public LimitOrderBuilder withCancelAfter(String cancelAfter) {
			this.cancel_after = cancelAfter;
			return this;
		}
		public LimitOrderBuilder withPostOnly(boolean postOnly) {
			this.post_only = postOnly;
			return this;
		}
		public GdaxLimitOrderRequest build(double size, double price) {
			return new GdaxLimitOrderRequest(product_id, side, type, size, price,
					time_in_force, cancel_after, post_only);
		}
	}
	
	public static class MarketOrderBuilder {
		private final String product_id;
		private final TradeSide side;
		private final OrderType type;
		public MarketOrderBuilder(String product_id, TradeSide side, OrderType type) {
			this.product_id = product_id;
			this.side = side;
			this.type = type;
		}
		public GdaxMarketOrderRequest size(double size) {
			return new GdaxMarketOrderRequest(product_id, side, type, size, null);
		}
		public GdaxMarketOrderRequest funds(double funds) {
			return new GdaxMarketOrderRequest(product_id, side, type, null, funds);
		}
	}

}
