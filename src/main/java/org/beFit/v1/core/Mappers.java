package org.beFit.v1.core;

import org.beFit.v1.core.models.Comment;
import org.beFit.v1.core.models.FitGroup;
import org.beFit.v1.core.models.Post;
import org.beFit.v1.core.models.User;
import org.beFit.v1.repositories.entities.CommentEntity;
import org.beFit.v1.repositories.entities.FitGroupEntity;
import org.beFit.v1.repositories.entities.PostEntity;
import org.beFit.v1.repositories.entities.UserEntity;

import java.math.BigDecimal;

class Mappers {
	public static User fromUserEntity(UserEntity c) {
		return new User(c.id, c.username, c.roles, c.avatarUrl, c.balance);
	}

	public static Post fromPostEntity(PostEntity p) {
		return new Post(p.id, p.userId, p.imageUrl, p.content, p.votes, p.postTime, p.isArchived);
	}

	public static Comment fromCommentEntity(CommentEntity c) {
		return new Comment(c.id, c.user_id, c.content, c.depth, c.parentId);
	}

	public static FitGroup fromFitGroupEntity(FitGroupEntity fg) {
		return new FitGroup(fg.id, fg.name, fg.stake, fg.balance);
	}
}
