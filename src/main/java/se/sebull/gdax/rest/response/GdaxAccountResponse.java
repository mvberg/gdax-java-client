package se.sebull.gdax.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxAccountResponse extends GdaxHttpResponse {

	public String id;
	public Double balance;
	public Double hold;
	public Double available;
	public String currency;

	@Override
	public String toString() {
		return "GdaxAccount [id=" + id + ", balance=" + balance + ", hold=" + hold + ", available=" + available
				+ ", currency=" + currency + "]";
	}
	
}
