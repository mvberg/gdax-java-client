package se.sebull.gdax.client;

import java.util.List;

import se.sebull.gdax.rest.request.GdaxOrderRequest;
import se.sebull.gdax.rest.response.GdaxAccountResponse;
import se.sebull.gdax.rest.response.GdaxOrderResponse;
import se.sebull.gdax.websocket.GdaxConfig;

/**
 * Requires API authentication to be present in {@link GdaxConfig}
 */
public interface GdaxPrivateApi {

	/**
	 * Get a list of trading accounts.
	 */
	List<GdaxAccountResponse> getAccounts();
	
	/**
	 * Information for a single account. Use this endpoint when you know the account_id.
	 */
	GdaxAccountResponse getAccount(String accountId);
	
	/**
	 * You can place different types of orders: limit, market, and stop.
	 * Orders can only be placed if your account has sufficient funds.
	 * Once an order is placed, your account funds will be put on hold for the duration of the order.
	 * How much and which funds are put on hold depends on the order type and parameters specified. 
	 */
	GdaxOrderResponse placeOrder(GdaxOrderRequest order);
	
	// TODO: returns the order id on the format ["1430bffb-05ff-40a4-8eef-5c7bdff65c4e"] when successful
	// or a message field with the reason for failure. make a return type
	void cancelOrder(String orderId);
	
	/**
	 * List your current open orders. Only open or un-settled orders are returned
	 */
	List<GdaxOrderResponse> listOrders();

	/**
	 * Get a single order by order id.
	 * May be used to get done orders
	 */
	GdaxOrderResponse getOrder(String orderId);
}

