package org.beFit.v1.repositories.entities;

public class CommentEntity {
	public final Integer id;
	public final Integer post_id;
	public final Integer user_id;
	public final String content;
	public final String commentPath;

	public CommentEntity(Integer id, Integer post_id, Integer user_id, String content, String commentPath) {
		this.id = id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.content = content;
		this.commentPath = commentPath;
	}
}
