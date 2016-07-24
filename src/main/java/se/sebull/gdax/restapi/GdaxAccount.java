package se.sebull.gdax.restapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxAccount {

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
