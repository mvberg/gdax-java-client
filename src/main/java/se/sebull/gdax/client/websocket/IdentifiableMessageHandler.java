package se.sebull.gdax.client.websocket;

public interface IdentifiableMessageHandler<T> {

	String getId();
    void onMessage(T message);

}
