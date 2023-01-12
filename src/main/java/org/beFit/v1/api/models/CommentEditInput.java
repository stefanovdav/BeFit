package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentEditInput {
	public final String content;

	@JsonCreator
	public CommentEditInput(@JsonProperty("content") String content) {
		this.content = content;
	}
}
