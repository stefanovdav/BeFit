package org.beFit.v1.core.scheduled;

import org.beFit.v1.core.Mappers;
import org.beFit.v1.core.models.FitGroup;
import org.beFit.v1.core.models.User;
import org.beFit.v1.repositories.CommentRepository;
import org.beFit.v1.repositories.FitGroupRepository;
import org.beFit.v1.repositories.PostRepository;
import org.beFit.v1.repositories.UserRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class ScheduledTasks {

	public final UserRepository userRepository;
	public final FitGroupRepository fitGroupRepository;
	public final PostRepository postRepository;
	public final CommentRepository commentRepository;

	public ScheduledTasks(UserRepository userRepository, FitGroupRepository fitGroupRepository, PostRepository postRepository, CommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.fitGroupRepository = fitGroupRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	//TODO: OPTIMIZE!!!!!!
	@Scheduled(cron = "0 0 * * * *")
	public void takeAndGive() {
		List<FitGroup> groups = fitGroupRepository.listGroups()
				.stream()
				.map(Mappers::fromFitGroupEntity)
				.collect(Collectors.toList());

		for (FitGroup g : groups) {
			if (g.stake.equals(BigDecimal.ZERO)) {
				continue;
			}
			List<User> users = userRepository.usersInGroup(g.id)
					.stream()
					.map(Mappers::fromUserEntity)
					.collect(Collectors.toList());

			List<User> usersThatPosted = userRepository.usersWithPost(g.id)
					.stream()
					.map(Mappers::fromUserEntity)
					.collect(Collectors.toList());

			List<User> usersThatDidntPost = users.stream()
					.filter(user -> !usersThatPosted.contains(user))
					.collect(Collectors.toList());

			BigDecimal takenAssets = BigDecimal.ZERO;
			for (int i = 0; i < users.size(); i++) {
				Integer userId = usersThatDidntPost.get(i).id;
				BigDecimal assets = userRepository.frozenAssetsInGroup(userId, g.id);
				BigDecimal fee = assets.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(g.taxPercent));

				//case when the user frozen assets are >= fee
				if (assets.compareTo(fee) >= 0) {
					userRepository.changeFrozenAssets(userId, fee.negate(), g.id);
					takenAssets.add(fee);
				}
				//case when the user frozen assets are < fee
				else {
					userRepository.changeFrozenAssets(userId, assets.negate(), g.id);
					takenAssets.add(assets);
					userRepository.removeUserFromGroup(userId, g.id, BigDecimal.ZERO);
				}
				BigDecimal payment = takenAssets.divide(BigDecimal.valueOf(usersThatPosted.size()));
				for (User fitUser : usersThatPosted) {
					userRepository.changeFrozenAssets(fitUser.id, payment, g.id);
				}
			}
			if (g.endDate.equals(new java.sql.Date(System.currentTimeMillis()))) {
				fitGroupRepository.deleteFitGroup(g.id);
			}
			fitGroupRepository.archivePosts(g.id);
		}

	}
}
