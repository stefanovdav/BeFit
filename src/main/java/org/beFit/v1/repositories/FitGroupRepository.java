package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.FitGroupEntity;
import org.beFit.v1.repositories.entities.PostEntity;

import java.math.BigDecimal;
import java.util.List;

public interface FitGroupRepository {
	FitGroupEntity createFitGroup(String name, BigDecimal stake);

	FitGroupEntity getFitGroup(int id);

	void deleteFitGroup(int id);
}
