package se.sebull.gdax.common;

/**
 * Optional when placing an order
 */
public enum TimeInForce {

	GTC,
	GTT, // TODO impl this together with cancel_after
	IOC,
	FOK

}
