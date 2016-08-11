package se.sebull.gdax.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import se.sebull.gdax.common.OrderType;
import se.sebull.gdax.common.TradeSide;
import se.sebull.gdax.rest.request.GdaxMarketOrderRequest;
import se.sebull.gdax.rest.request.GdaxOrderRequest;
import se.sebull.gdax.rest.response.GdaxAccountResponse;
import se.sebull.gdax.rest.response.GdaxOrderResponse;
import se.sebull.gdax.websocket.GdaxConfig;

@Ignore
public class GdaxPrivateApiTest {
	
	private static final GdaxConfig sandbox = GdaxConfig.sandbox(
			System.getProperty("gdax.apikey"),
			System.getProperty("gdax.secret"),
			System.getProperty("gdax.passphrase")
			);
	
	private static GdaxClientImpl client;

	@BeforeClass
	public static void shouldRun() {
		Assume.assumeTrue("No auth details.", sandbox.hasAuth());
		client = GdaxClientImpl.simple(sandbox);
	}
	
	@After
	public void apiCallDelay() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			Thread.interrupted();
		}
	}

	@Test
	public void listOrdersTest() {
		List<GdaxOrderResponse> orders = client.listOrders();
		System.out.println("listOrders() " + orders);
		assertNotNull(orders);
	}

	@Test
	public void getOrderTest() {
		GdaxOrderResponse order = client.getOrder("123");
		System.out.println("getOrder() " + order);
		assertNotNull(order);
	}
	
	@Test
	public void cancelOrderTest() {
		client.cancelOrder("1430bffb-05ff-40a4-8eef-5c7bdff65c4e");
	}
	
	@Test
	public void placeOrderTest() {
		GdaxMarketOrderRequest request = GdaxOrderRequest.marketOrder("BTC-USD", TradeSide.sell, OrderType.limit)
				.size(1);
		GdaxOrderResponse order = client.placeOrder(request);
		System.out.println("placeOrder() " + order);
		assertNotNull(order);
	}
	
	@Test
	public void getAccountTest() {
		GdaxAccountResponse account = client.getAccount("123");
		System.out.println("getAccount() " + account);
		assertNotNull(account);
	}
	
	@Test
	public void getAccountsTest() {
		List<GdaxAccountResponse> accounts = client.getAccounts();
		System.out.println("getAccounts() " + accounts);
		assertTrue(accounts.size() > 0);
	}
	 
}
