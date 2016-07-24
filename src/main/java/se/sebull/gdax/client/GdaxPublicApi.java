package se.sebull.gdax.client;

import java.util.List;

import se.sebull.gdax.restapi.GdaxProduct;
import se.sebull.gdax.restapi.GdaxProductStats;
import se.sebull.gdax.restapi.GdaxTrade;

/**
 * Accessible without API authentication
 */
public interface GdaxPublicApi {

	/**
	 * List the latest trades for a product.
	 */
	List<GdaxTrade> getTrades(String productId);
	
	/**
	 * Get a list of available currency pairs for trading.
	 */
	List<GdaxProduct> getProducts();
	
	/**
	 * Get 24 hr stats for the product.
	 * volume is in base currency units. open, high, low are in quote currency units.
	 */
	GdaxProductStats get24hStats(String productId);
	
}
