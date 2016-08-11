package se.sebull.gdax.client;

import se.sebull.gdax.websocket.GdaxWebSocketSubscription;

/**
 * Accessible without API authentication
 */
public interface GdaxWebSocket {

	void subscribeToProduct(GdaxWebSocketSubscription subscription);

	void closeWebSocket();
	
}
