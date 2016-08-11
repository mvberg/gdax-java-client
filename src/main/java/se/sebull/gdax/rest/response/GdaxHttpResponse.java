package se.sebull.gdax.rest.response;

public abstract class GdaxHttpResponse {

	public String message;
	public Integer statusCode;
	
	public GdaxHttpResponse withStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}
	
}
