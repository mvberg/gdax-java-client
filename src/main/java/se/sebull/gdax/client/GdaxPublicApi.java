package se.sebull.gdax.client;

import java.time.LocalDateTime;
import java.util.List;

import se.sebull.gdax.common.OrderBookLevel;
import se.sebull.gdax.rest.response.GdaxHistoricRatesResponse;
import se.sebull.gdax.rest.response.GdaxOrderBookResponse;
import se.sebull.gdax.rest.response.GdaxProductResponse;
import se.sebull.gdax.rest.response.GdaxProductStatsResponse;
import se.sebull.gdax.rest.response.GdaxProductTickerResponse;
import se.sebull.gdax.rest.response.GdaxServerTimeResponse;
import se.sebull.gdax.rest.response.GdaxTradeResponse;

/**
 * Accessible without API authentication
 */
public interface GdaxPublicApi {

	/**
	 * List the latest trades for a product.
	 */
	List<GdaxTradeResponse> getTrades(String productId);
	
	/**
	 * Get a list of available currency pairs for trading.
	 */
	List<GdaxProductResponse> getProducts();
	
	/**
	 * Get order book  for a product. See {@link OrderBookLevel}
	 */
	GdaxOrderBookResponse getOrderBook(String productId, OrderBookLevel level);
	
	/**
	 * Snapshot information about the last trade (tick), best bid/ask and 24h volume.
	 */
	GdaxProductTickerResponse getTicker(String productId);
	
	/**
	 * Historic rates for a product.
	 * Rates are returned in grouped buckets based on requested granularity.
	 * @param productId
	 * @param start Start time in ISO 8601
	 * @param end End time in ISO 8601
	 * @param granularity Desired timeslice in seconds
	 */
	GdaxHistoricRatesResponse getHistoricRates(String productId, LocalDateTime start, LocalDateTime end, int granularity);
	
	/**
	 * Get 24 hr stats for the product.
	 * volume is in base currency units. open, high, low are in quote currency units.
	 */
	GdaxProductStatsResponse get24hStats(String productId);
	
	GdaxServerTimeResponse getServerTime();
	
}
