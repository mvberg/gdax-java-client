package se.sebull.gdax;

import java.util.Objects;

public class GdaxConfig {

	private final String apiBaseUrl;
	private final String webSocketUrl;
	private final String apiKey;
	private final String secret;
	private final String passphrase;
	
	public GdaxConfig(String apiBaseUrl, String webSocketUrl, String apiKey, String secret, String passphrase) {
		this.apiBaseUrl = Objects.requireNonNull(apiBaseUrl);
		this.webSocketUrl = Objects.requireNonNull(webSocketUrl);
		this.apiKey = apiKey;
		this.secret = secret;
		this.passphrase = passphrase;
	}

	public static GdaxConfig sandbox(String apiKey, String secret, String passphrase) {
		return new GdaxConfig(
				"https://api-public.sandbox.gdax.com",
				"wss://ws-feed-public.sandbox.gdax.com",
				apiKey, secret, passphrase);
	}
	
	public static GdaxConfig live(String apiKey, String secret, String passphrase) {
		return new GdaxConfig(
				"https://api.gdax.com",
				"wss://ws-feed.gdax.com",
				apiKey, secret, passphrase);
	}

	public static GdaxConfig sandboxNoAuth() {
		return GdaxConfig.sandbox(null, null, null);
	}
	
	public static GdaxConfig liveNoAuth() {
		return GdaxConfig.live(null, null, null);
	}
	
	public String getApiBaseUrl() {
		return apiBaseUrl;
	}
	
	public String getWebSocketUrl() {
		return webSocketUrl;
	}

	public String getApiKey() {
		return apiKey;
	}
	
	public String getSecret() {
		return secret;
	}

	public String getPassphrase() {
		return passphrase;
	}
	
	public boolean hasAuth() {
		return apiKey != null && passphrase != null && secret != null;
	}
	
}