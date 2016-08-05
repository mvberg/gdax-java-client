package se.sebull.gdax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import se.sebull.gdax.client.GdaxClientImpl;
import se.sebull.gdax.client.websocket.GdaxWebSocketResponse;
import se.sebull.gdax.client.websocket.GdaxWebSocketSubscription;
import se.sebull.gdax.restapi.GdaxAccount;
import se.sebull.gdax.restapi.GdaxOrderRequest;
import se.sebull.gdax.restapi.GdaxOrderResponse;
import se.sebull.gdax.restapi.GdaxProduct;
import se.sebull.gdax.restapi.GdaxProductStats;
import se.sebull.gdax.restapi.GdaxTrade;
import se.sebull.gdax.restapi.OrderType;
import se.sebull.gdax.restapi.TradeSide;

public class GdaxClientTest {
	
	private static final GdaxConfig sandbox = GdaxConfig.sandbox(
			System.getProperty("gdax.apikey"),
			System.getProperty("gdax.secret"),
			System.getProperty("gdax.passphrase")
			);
	
	private static GdaxClientImpl client;

	@BeforeClass
	public static void shouldRun() {
		Assume.assumeTrue("No auth details. Assuming should not run.", sandbox.hasAuth());
		client = GdaxClientImpl.simple(sandbox);
	}
	
	@After
	public void apiCallDelay() {
		try {
			TimeUnit.SECONDS.sleep(1);
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
		GdaxOrderRequest orderRequest = GdaxOrderRequest.create("BTC-USD", TradeSide.sell, OrderType.limit, 1, 1);
		GdaxOrderResponse order = client.placeOrder(orderRequest);
		System.out.println("placeOrder() " + order);
		assertNotNull(order);
	}
	
	@Test
	public void getAccountTest() {
		GdaxAccount account = client.getAccount("123");
		System.out.println("getAccount() " + account);
		assertNotNull(account);
	}
	
	@Test
	public void getTradesTest() {
		List<GdaxTrade> trades = client.getTrades("BTC-USD");
		System.out.println("getTrades()");
		trades.stream().forEach(System.out::println);
		assertTrue(trades.size() > 1);
	}

	@Test
	public void getProductsTest() {
		List<GdaxProduct> products = client.getProducts();
		System.out.println("getProducts() " + products);
		assertTrue(products.size() > 1);
	}

	@Test
	public void get24hStatsTest() {
		GdaxProductStats stats = client.get24hStats("BTC-USD");
		System.out.println("get24hStats() " + stats);
		assertNotNull(stats);
	}

	@Test
	public void getAccountsTest() {
		List<GdaxAccount> accounts = client.getAccounts();
		System.out.println("getAccounts() " + accounts);
		assertTrue(accounts.size() > 0);
	}
	
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
	}

}
