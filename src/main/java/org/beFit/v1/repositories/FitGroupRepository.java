package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.FitGroupEntity;

import java.math.BigDecimal;
import java.sql.Date;

public interface FitGroupRepository {
	FitGroupEntity createFitGroup(String name, BigDecimal stake, Integer tax, Date end, String key);

	FitGroupEntity getFitGroup(int id);

	void deleteFitGroup(int id);
}
