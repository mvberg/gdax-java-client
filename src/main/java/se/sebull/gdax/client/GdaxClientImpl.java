package se.sebull.gdax.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import se.sebull.gdax.GdaxConfig;
import se.sebull.gdax.client.websocket.GdaxWebSocketResponse;
import se.sebull.gdax.client.websocket.GdaxWebSocketSubscription;
import se.sebull.gdax.client.websocket.IdentifiableMessageHandler;
import se.sebull.gdax.client.websocket.WebSocketClient;
import se.sebull.gdax.restapi.GdaxAccount;
import se.sebull.gdax.restapi.GdaxLocalDateTimeDeserializer;
import se.sebull.gdax.restapi.GdaxOrderRequest;
import se.sebull.gdax.restapi.GdaxOrderResponse;
import se.sebull.gdax.restapi.GdaxProduct;
import se.sebull.gdax.restapi.GdaxProductStats;
import se.sebull.gdax.restapi.GdaxTrade;

public class GdaxClientImpl implements GdaxClient {

	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Mac sha256;
	static {
		objectMapper.registerModule(
				new SimpleModule().addDeserializer(LocalDateTime.class, new GdaxLocalDateTimeDeserializer()));
		try {
			sha256 = Mac.getInstance(HMAC_SHA256);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final GdaxConfig gdaxConfig;
	private final HttpClient httpClient;
	private final WebSocketClient webSocketClient;

	private GdaxClientImpl(GdaxConfig gdaxConfig, HttpClient httpClient) {
		this.gdaxConfig = Objects.requireNonNull(gdaxConfig);
		this.httpClient = Objects.requireNonNull(httpClient);
		this.webSocketClient = new WebSocketClient(gdaxConfig.getWebSocketUrl());
		if (gdaxConfig.hasAuth()) {
			SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(gdaxConfig.getSecret()),
					HMAC_SHA256);
			try {
				sha256.init(secretKeySpec);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static GdaxClientImpl create(GdaxConfig gdaxConfig, HttpClient httpClient) {
		return new GdaxClientImpl(gdaxConfig, httpClient);
	}
	
	public static GdaxClientImpl simple(GdaxConfig gdaxConfig) {
		return new GdaxClientImpl(gdaxConfig, HttpClients.createDefault());
	}

	@Override
	public List<GdaxProduct> getProducts() {
		return toList(request(createGet("/products")));
	}

	@Override
	public List<GdaxTrade> getTrades(String productId) {
		return toList(request(createGet("/products/" + productId + "/trades")));
	}

	@Override
	public List<GdaxAccount> getAccounts() {
		String endpoint = "/accounts";
		return toList(privateRequest(createGet(endpoint), endpoint));
	}

	@Override
	public GdaxAccount getAccount(String accountId) {
		String endpoint = "/accounts/" + accountId;
		return toObject(privateRequest(createGet(endpoint), endpoint), GdaxAccount.class);
	}

	@Override
	public GdaxProductStats get24hStats(String productId) {
		return toObject(request(createGet("/products/" + productId + "/stats")), GdaxProductStats.class);
	}

	@Override
	public List<GdaxOrderResponse> listOrders() {
		String endpoint = "/orders";
		return toList(privateRequest(createGet(endpoint), endpoint));
	}

	@Override
	public GdaxOrderResponse getOrder(String orderId) {
		String endpoint = "/orders/" + orderId;
		return toObject(privateRequest(createGet(endpoint), endpoint), GdaxOrderResponse.class);
	}

	@Override
	public GdaxOrderResponse placeOrder(GdaxOrderRequest order) {
		String endpoint = "/orders";
		String body = unchecked(() -> objectMapper.writeValueAsString(order));
		return toObject(privateRequest(createPost(endpoint, body), endpoint, body), GdaxOrderResponse.class);
	}
	
	@Override
	public void cancelOrder(String orderId) {
		String endpoint = "/orders/" + orderId;
		String string = privateRequest(createDelete(endpoint), endpoint);
		System.out.println(string);
	}

	@Override
	public void subscribeToProduct(GdaxWebSocketSubscription subscription) {
		webSocketClient.sendMessage(subscription.toSubscriptionJson());
		webSocketClient.addMessageHandler(new IdentifiableMessageHandler<String>() {
			@Override
			public void onMessage(String message) {
				subscription.onMessage(toObject(message, GdaxWebSocketResponse.class));
			}
			@Override
			public String getId() {
				return subscription.getProductId();
			}
		});
	}

	@Override
	public void closeWebSocket() {
		webSocketClient.disconnect();
	}

	private HttpGet createGet(String endpoint) {
		return new HttpGet(gdaxConfig.getApiBaseUrl() + endpoint);
	}

	private HttpDelete createDelete(String endpoint) {
		return new HttpDelete(gdaxConfig.getApiBaseUrl() + endpoint);
	}

	private HttpPost createPost(String endpoint, String body) {
		HttpPost httpPost = new HttpPost(gdaxConfig.getApiBaseUrl() + endpoint);
		StringEntity entity = unchecked(() -> new StringEntity(body));
		httpPost.setEntity(entity);
		return httpPost;
	}

	private String privateRequest(HttpUriRequest request, String endpoint) {
		return privateRequest(request, endpoint, "");
	}

	private String privateRequest(HttpUriRequest request, String endpoint, String body) {
		long timestamp = Instant.now().getEpochSecond();
		return privateRequest(request, generateSignature(request.getMethod(), endpoint, body, timestamp), timestamp);
	}

	private String privateRequest(HttpUriRequest request, String signature, long timestamp) {
		request.addHeader("CB-ACCESS-KEY", gdaxConfig.getApiKey());
		request.addHeader("CB-ACCESS-SIGN", signature);
		request.addHeader("CB-ACCESS-TIMESTAMP", String.valueOf(timestamp));
		request.addHeader("CB-ACCESS-PASSPHRASE", gdaxConfig.getPassphrase());
		return request(request);
	}

	private String generateSignature(String method, String endpoint, String body, long timestamp) {
		String prehash = timestamp + method + endpoint + body;
		return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes()));
	}

	private String request(HttpUriRequest request) {
		request.addHeader("content-type", "application/json");
		return unchecked(() -> {
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClient.execute(request).getEntity().getContent()))) {
				return br.lines().collect(Collectors.joining());
			}
		});
	}

	private static <T> List<T> toList(String json) {
		return unchecked(() -> objectMapper.readValue(json, new TypeReference<List<?>>() {}));
	}

	private static <T> T toObject(String json, Class<T> clazz) {
		return unchecked(() -> objectMapper.readValue(json, clazz));
	}

	private static <T> T unchecked(Callable<T> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
