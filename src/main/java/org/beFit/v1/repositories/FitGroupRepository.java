package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.FitGroupEntity;
import org.beFit.v1.repositories.entities.UserEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface FitGroupRepository {
	FitGroupEntity createFitGroup(String name, BigDecimal stake, Integer tax, Date end, String key);

	FitGroupEntity getFitGroup(int id);

	void deleteFitGroup(int id);

	List<FitGroupEntity> listGroups();

	void archivePosts(Integer groupId);
}
