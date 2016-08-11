package se.sebull.gdax.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GdaxProductStatsResponse extends GdaxHttpResponse {

	public Double open;
	public Double high;
	public Double low;
	public Double volume;
	public Double volume_30day;

	@Override
	public String toString() {
		return "GdaxProductStats [open=" + open + ", high=" + high + ", low=" + low + ", volume=" + volume
				+ ", volume_30day=" + volume_30day + "]";
	}

}
