package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostInput {
	public final String imageUrl;
	public final String content;

@JsonCreator
	public PostInput(@JsonProperty("imageUrl") String imageUrl, @JsonProperty("content") String content) {
	this.imageUrl = imageUrl;
	this.content = content;
	}
}
