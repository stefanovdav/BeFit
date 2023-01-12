package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FitGroupInput {

	public final String name;
	public final BigDecimal stake;

	@JsonCreator
	public FitGroupInput(@JsonProperty("name") String name,
						 @JsonProperty("stake") BigDecimal stake) {
		this.name = name;
		this.stake = stake;
	}
}
