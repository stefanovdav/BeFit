package org.beFit.v1.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentInput {

	public final String content;
	public final Integer post_id;

	@JsonCreator
	public CommentInput(@JsonProperty("content") String content, @JsonProperty("post_id") Integer post_id) {
		this.content = content;
		this.post_id = post_id;
	}
}