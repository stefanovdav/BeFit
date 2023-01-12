package org.beFit.v1.repositories.entities;

import java.time.Instant;

public class PostEntity {
	public final Integer id;
	public final Integer userId;
	public final Integer imageId;
	public final String content;
	public final Integer votes;
	public final Instant postTime;
	public final boolean isArchived;

	public PostEntity(Integer id, Integer userId, Integer imageId, String content, Integer votes, Instant postTime, boolean isArchived) {
		this.id = id;
		this.userId = userId;
		this.imageId = imageId;

		this.content = content;
		this.votes = votes;
		this.postTime = postTime;
		this.isArchived = isArchived;
	}
}
