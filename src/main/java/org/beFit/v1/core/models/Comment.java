package org.beFit.v1.core.models;

public class Comment {
	public final Integer id;
	public final Integer user_id;
	public final String content;
	public final Integer depth;
	public final Integer parentId;

	public Comment(Integer id, Integer user_id, String content, Integer depth, Integer parentId) {
		this.id = id;
		this.user_id = user_id;
		this.content = content;
		this.depth = depth;
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id=" + id +
				", user_id=" + user_id +
				", content='" + content + '\'' +
				", depth=" + depth +
				", parentId=" + parentId +
				'}';
	}
}
