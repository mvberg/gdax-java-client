package se.sebull.gdax.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import se.sebull.gdax.common.OrderBookLevel;
import se.sebull.gdax.rest.response.GdaxHistoricRatesResponse;
import se.sebull.gdax.rest.response.GdaxOrderBookResponse;
import se.sebull.gdax.rest.response.GdaxProductResponse;
import se.sebull.gdax.rest.response.GdaxProductStatsResponse;
import se.sebull.gdax.rest.response.GdaxProductTickerResponse;
import se.sebull.gdax.rest.response.GdaxServerTimeResponse;
import se.sebull.gdax.rest.response.GdaxTradeResponse;
import se.sebull.gdax.websocket.GdaxConfig;

@Ignore
public class GdaxPublicApiTest {

	private static final GdaxClientImpl client = GdaxClientImpl.simple(GdaxConfig.liveNoAuth());
	
	@After
	public void apiCallDelay() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			Thread.interrupted();
		}
	}
	
	@Test
	public void getTradesTest() {
		List<GdaxTradeResponse> trades = client.getTrades("BTC-USD");
		System.out.println("getTrades()");
		trades.stream().forEach(System.out::println);
		assertTrue(trades.size() > 1);
	}

	@Test
	public void getProductsTest() {
		List<GdaxProductResponse> products = client.getProducts();
		System.out.println("getProducts() " + products);
		assertTrue(products.size() > 1);
	}
	
	@Test
	public void getOrderBookTest() {
		GdaxOrderBookResponse orderBook = client.getOrderBook("BTC-USD", OrderBookLevel.TWO);
		System.out.println("getOrderBook() " + orderBook);
		assertNotNull(orderBook);
	}

	@Test
	public void get24hStatsTest() {
		GdaxProductStatsResponse stats = client.get24hStats("BTC-USD");
		System.out.println("get24hStats() " + stats);
		assertNotNull(stats);
	}

	@Test
	public void getTickerTest() {
		GdaxProductTickerResponse ticker = client.getTicker("BTC-USD");
		System.out.println("getTicker() " + ticker);
		assertNotNull(ticker);
	}
	
	@Test
	public void getHistoricRatesTest() {
		GdaxHistoricRatesResponse historicRates = client.getHistoricRates("BTC-USD", LocalDateTime.now(),
				LocalDateTime.now().minusMinutes(10), 60);
		System.out.println("getHistoricRates() " + historicRates);
		assertNotNull(historicRates);
	}
	
	@Test
	public void getServerTimeTest() {
		GdaxServerTimeResponse serverTime = client.getServerTime();
		System.out.println("getServerTime() " + serverTime);
		assertNotNull(serverTime);
	}
	
}
