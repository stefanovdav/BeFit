package org.beFit.v1.core.models;

import java.time.Instant;

public class Post {
	public final Integer id;
	public final Integer userId;
	public final Integer imageId;
	public final String content;
	public final Integer votes;
	public final Instant postTime;
	public final boolean isArchived;

	public Post(Integer id, Integer userId, Integer imageId, String content, Integer votes, Instant postTime, boolean isArchived) {
		this.id = id;
		this.userId = userId;
		this.imageId = imageId;
		this.content = content;
		this.votes = votes;
		this.postTime = postTime;
		this.isArchived = isArchived;
	}

	@Override
	public String toString() {
		return "Post{" +
				"id=" + id +
				", userId=" + userId +
				", imageId=" + imageId +
				", content='" + content + '\'' +
				", votes=" + votes +
				", postTime=" + postTime +
				", isArchived=" + isArchived +
				'}';
	}
}
