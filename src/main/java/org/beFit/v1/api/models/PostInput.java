package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostInput {
	public final Integer imageId;
	public final String content;

@JsonCreator
	public PostInput(@JsonProperty("imageId") Integer imageId, @JsonProperty("content") String content) {
	this.imageId = imageId;
	this.content = content;
	}
}
