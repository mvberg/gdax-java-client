package se.sebull.gdax.common;

public enum OrderBookLevel {

	/**
	 * Only the best bid and ask
	 */
	ONE(1),
	/**
	 * Top 50 bids and asks (aggregated)
	 */
	TWO(2),
	/**
	 * Full order book (non aggregated)
	 */
	THREE(3);
	
	private final int level;

	private OrderBookLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
}
