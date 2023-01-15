package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;

public class FitGroupInput {

	public final String name;
	public final BigDecimal stake;
	public final Integer tax;
	public final Date end;

	@JsonCreator
	public FitGroupInput(@JsonProperty("name") String name,
						 @JsonProperty("stake") BigDecimal stake,
						 @JsonProperty("tax") Integer tax,
						 @JsonProperty ("end") Date end) {
		this.name = name;
		this.stake = stake;
		this.tax = tax;
		this.end = end;
	}
}
