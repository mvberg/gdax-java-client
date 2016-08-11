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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;

import se.sebull.gdax.common.GdaxLocalDateTimeDeserializer;
import se.sebull.gdax.common.OrderBookLevel;
import se.sebull.gdax.rest.request.GdaxOrderRequest;
import se.sebull.gdax.rest.response.GdaxAccountResponse;
import se.sebull.gdax.rest.response.GdaxHistoricRatesResponse;
import se.sebull.gdax.rest.response.GdaxOrderBookResponse;
import se.sebull.gdax.rest.response.GdaxOrderResponse;
import se.sebull.gdax.rest.response.GdaxProductResponse;
import se.sebull.gdax.rest.response.GdaxProductStatsResponse;
import se.sebull.gdax.rest.response.GdaxProductTickerResponse;
import se.sebull.gdax.rest.response.GdaxServerTimeResponse;
import se.sebull.gdax.rest.response.GdaxTradeResponse;
import se.sebull.gdax.websocket.GdaxConfig;
import se.sebull.gdax.websocket.GdaxWebSocketResponse;
import se.sebull.gdax.websocket.GdaxWebSocketSubscription;
import se.sebull.gdax.websocket.MessageHandler;
import se.sebull.gdax.websocket.WebSocketClient;

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
	public List<GdaxProductResponse> getProducts() {
		return toList(request(createGet("/products")).getJson(), GdaxProductResponse.class);
	}

	@Override
	public List<GdaxTradeResponse> getTrades(String productId) {
		return toList(request(createGet("/products/" + productId + "/trades")).getJson(), GdaxTradeResponse.class);
	}
	
	@Override
	public GdaxOrderBookResponse getOrderBook(String productId, OrderBookLevel level) {
		return toObject(request(createGet("/products/" + productId + "/book?level=" + level.getLevel())).getJson(),
				GdaxOrderBookResponse.class);
	}

	@Override
	public List<GdaxAccountResponse> getAccounts() {
		String endpoint = "/accounts";
		return toList(privateRequest(createGet(endpoint), endpoint).getJson(), GdaxAccountResponse.class);
	}

	@Override
	public GdaxAccountResponse getAccount(String accountId) {
		String endpoint = "/accounts/" + accountId;
		return toObject(privateRequest(createGet(endpoint), endpoint).getJson(), GdaxAccountResponse.class);
	}

	@Override
	public GdaxProductStatsResponse get24hStats(String productId) {
		return toObject(request(createGet("/products/" + productId + "/stats")).getJson(), GdaxProductStatsResponse.class);
	}

	@Override
	public List<GdaxOrderResponse> listOrders() {
		String endpoint = "/orders";
		return toList(privateRequest(createGet(endpoint), endpoint).getJson(), GdaxOrderResponse.class);
	}

	@Override
	public GdaxOrderResponse getOrder(String orderId) {
		String endpoint = "/orders/" + orderId;
		return toObject(privateRequest(createGet(endpoint), endpoint).getJson(), GdaxOrderResponse.class);
	}

	@Override
	public GdaxOrderResponse placeOrder(GdaxOrderRequest order) {
		String endpoint = "/orders";
		String body = unchecked(() -> objectMapper.writeValueAsString(order));
		return toObject(privateRequest(createPost(endpoint, body), endpoint, body).getJson(), GdaxOrderResponse.class);
	}
	
	@Override
	public void cancelOrder(String orderId) {
		String endpoint = "/orders/" + orderId;
		privateRequest(createDelete(endpoint), endpoint);
	}
	
	@Override
	public GdaxProductTickerResponse getTicker(String productId) {
		return toObject(request(createGet("/products/" + productId + "/ticker")).getJson(), GdaxProductTickerResponse.class);
	}
	
	@Override
	public GdaxHistoricRatesResponse getHistoricRates(String productId, LocalDateTime start, LocalDateTime end,
			int granularity) {
		String params = String.format("?start=%s&end=%s&granularity=%d", start.toString(), end.toString(), granularity);
		return toObject(request(createGet("/products/" + productId + "/candles" + params)).getJson(), GdaxHistoricRatesResponse.class);
	}

	@Override
	public GdaxServerTimeResponse getServerTime() {
		return toObject(request(createGet("/time")).getJson(), GdaxServerTimeResponse.class);
	}

	/**
	 * TODO: singleton
	 */
	@Override
	public void subscribeToProduct(GdaxWebSocketSubscription subscription) {
		webSocketClient.sendMessage(subscription.toSubscriptionJson());
		webSocketClient.addMessageHandler(new MessageHandler<String>() {
			@Override
			public void onMessage(String message) {
				subscription.onMessage(toObject(message, GdaxWebSocketResponse.class));
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

	private HttpResponseHolder privateRequest(HttpUriRequest request, String endpoint) {
		return privateRequest(request, endpoint, "");
	}

	private HttpResponseHolder privateRequest(HttpUriRequest request, String endpoint, String body) {
		long timestamp = Instant.now().getEpochSecond();
		return privateRequest(request, generateSignature(request.getMethod(), endpoint, body, timestamp), timestamp);
	}

	private HttpResponseHolder privateRequest(HttpUriRequest request, String signature, long timestamp) {
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

	private HttpResponseHolder request(HttpUriRequest request) {
		request.addHeader("content-type", "application/json");
		return unchecked(() -> {
			return new HttpResponseHolder(httpClient.execute(request));
		});
	}
	
	private static class HttpResponseHolder {
		private final int status;
		private final String json;
		public HttpResponseHolder(org.apache.http.HttpResponse response) {
			this.status = response.getStatusLine().getStatusCode();
			this.json = getJson(response);
		}
		private String getJson(org.apache.http.HttpResponse response) {
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()))) {
				return br.lines().collect(Collectors.joining());
			} catch (Exception e) {
				return "";
			}
		}
		public int getStatus() {
			return status;
		}
		public String getJson() {
			return json;
		}
	}

	private <T> List<T> toList(String json, Class<T> clazz) {
		CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
		return unchecked(() -> objectMapper.readValue(json, collectionType));
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
