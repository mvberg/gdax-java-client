package se.sebull.gdax.restapi;

public class GdaxOrderRequest {

	public String product_id;
	public TradeSide side;
	public OrderType type;
	public double price;
	public double size;

	public boolean post_only; // TODO this is nice impl it
	
	public static GdaxOrderRequest create(String productId, TradeSide side, OrderType type, double price, double size) {
		GdaxOrderRequest order = new GdaxOrderRequest();
		order.product_id = productId;
		order.side = side;
		order.type = type;
		order.price = price;
		order.size = size;
		return order;
	}

	@Override
	public String toString() {
		return "GdaxOrderRequest [product_id=" + product_id + ", side=" + side + ", type=" + type + ", price=" + price
				+ ", size=" + size + ", post_only=" + post_only + "]";
	}

}
