package se.sebull.gdax.client;

import org.junit.Ignore;
import org.junit.Test;

import se.sebull.gdax.websocket.GdaxConfig;
import se.sebull.gdax.websocket.GdaxWebSocketResponse;
import se.sebull.gdax.websocket.GdaxWebSocketSubscription;

@Ignore
public class GdaxWebsocketTest {
	
	private static final GdaxClientImpl client = GdaxClientImpl.simple(GdaxConfig.liveNoAuth());
	
	@Test
	public void webSocketTest() throws Exception {
		client.subscribeToProduct(
			new GdaxWebSocketSubscription() {
				@Override
				public void onMessage(GdaxWebSocketResponse response) {
					System.out.println(response);
				}
				@Override
				public String getProductId() {
					return "BTC-USD";
				}
			});
		Thread.sleep(2000);
		client.closeWebSocket();
		System.out.println("Closed");
		Thread.sleep(1000);
		System.out.println("Done");
	}

}
