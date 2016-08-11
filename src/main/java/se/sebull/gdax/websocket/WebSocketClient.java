package se.sebull.gdax.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient {

	private final String socketUrl;
	private Session session;
	private final List<MessageHandler<String>> messageHandlers = new ArrayList<>();
	
	public WebSocketClient(String socketUrl) {
		this.socketUrl = Objects.requireNonNull(socketUrl);
	}
	
	void connect() {
		if (session != null && session.isOpen()) {
			throw new RuntimeException("Already connected");
		}
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			container.connectToServer(this, new URI(socketUrl));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void disconnect() {
		try {
			session.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			session = null;
		}
	}
	
	public void sendMessage(String message) {
		boolean connected = true;
		if (session == null || !session.isOpen()) {
			connected = false;
			connect();
			for (int i = 0; i < 30; i++) {
				sleep(100);
				if (session != null) {
					connected = true;
					break;
				}
			}
			if (!connected) {
				throw new RuntimeException("Failed to connect");
			}
		}
		try {
			session.getAsyncRemote().sendText(message).get(3, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void sleep(int millis) {
		try {
			TimeUnit.MICROSECONDS.sleep(millis);
		} catch (InterruptedException e) {
			Thread.interrupted();
			throw new RuntimeException(e);
		}
	}
	
	public void addMessageHandler(MessageHandler<String> messageHandler) {
		messageHandlers.add(messageHandler);
	}
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}
	
	@OnMessage
	public void onMessage(String message) {
		messageHandlers.forEach(handler -> handler.onMessage(message));
	}
	
	@OnClose
    public void onClose() {
		this.session = null;
	}
	
    @OnError
    public void onError(Throwable t) {
    	throw new RuntimeException(t);
    }	
	
}