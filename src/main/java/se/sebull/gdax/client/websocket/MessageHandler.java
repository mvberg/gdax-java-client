package se.sebull.gdax.client.websocket;

import javax.websocket.MessageHandler.Whole;

public interface MessageHandler<T> extends Whole<T> {

}
