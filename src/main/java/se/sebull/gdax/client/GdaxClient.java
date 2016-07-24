package se.sebull.gdax.client;

import se.sebull.gdax.client.websocket.GdaxWebSocketSubscription;

public interface GdaxClient extends GdaxPublicApi, GdaxPrivateApi {

	void subscribeToProduct(GdaxWebSocketSubscription subscription);

	void closeWebSocket();
	
}