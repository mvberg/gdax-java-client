package se.sebull.gdax.websocket;

public interface GdaxWebSocketSubscription {

	String getProductId();
	void onMessage(GdaxWebSocketResponse response);
	
	default String toSubscriptionJson() {
		return "{\"type\":\"subscribe\",\"product_id\":\"" + getProductId() + "\"}";
	}
	
}
