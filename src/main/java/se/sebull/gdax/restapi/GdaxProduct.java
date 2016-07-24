package se.sebull.gdax.restapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxProduct {
	
	public String id;
	public String base_currency;
	public String quote_currency;
	public Double base_min_size;
	public Double base_max_size;
	public Double quote_increment;
	public String display_name;
	
	@Override
	public String toString() {
		return "GdaxProduct [id=" + id + ", base_currency=" + base_currency + ", quote_currency=" + quote_currency
				+ ", base_min_size=" + base_min_size + ", base_max_size=" + base_max_size + ", quote_increment="
				+ quote_increment + ", display_name=" + display_name + "]";
	}
	
}
