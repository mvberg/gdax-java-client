package se.sebull.gdax.client.websocket;

/**
 * lower case so they can be deserialized as is
 */
public enum MessageType {

	received,
	open,
	done,
	match,
	change,
	heartbeat,
	error
	
}
