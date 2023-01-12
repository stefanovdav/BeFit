package org.beFit.v1.repositories.entities;

public class CommentEntity {
	public final Integer id;
	public final Integer post_id;
	public final Integer user_id;
	public final String content;
	public final Integer depth;
	public final Integer parentId;

	public CommentEntity(Integer id, Integer post_id, Integer user_id, String content, Integer depth, Integer parentId) {
		this.id = id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.content = content;
		this.depth = depth;
		this.parentId = parentId;
	}
}
