package se.sebull.gdax.websocket;

import javax.websocket.MessageHandler.Whole;

public interface MessageHandler<T> extends Whole<T> {

}
