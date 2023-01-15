package org.beFit.v1.core;

import org.beFit.v1.core.models.*;
import org.beFit.v1.repositories.entities.*;

public class Mappers {
	public static User fromUserEntity(UserEntity c) {
		return new User(c.id, c.username, c.roles, c.imageId, c.balance);
	}

	public static Post fromPostEntity(PostEntity p) {
		return new Post(p.id, p.userId, p.imageId, p.content, p.votes, p.postTime, p.isArchived);
	}

	public static Comment fromCommentEntity(CommentEntity c) {
		return new Comment(c.id, c.post_id, c.user_id, c.content, c.commentPath);
	}

	public static FitGroup fromFitGroupEntity(FitGroupEntity fg) {
		return new FitGroup(fg.id, fg.name, fg.stake, fg.taxPercent, fg.startDate, fg.endDate, fg.participants);
	}

	public static Image fromImageEntity(ImageEntity i) {
		return new Image(i.getId(), i.getTitle(), i.getUrl(), i.getPublicId());
	}
}
