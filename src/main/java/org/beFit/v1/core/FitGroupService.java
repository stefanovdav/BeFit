package org.beFit.v1.core;

import org.beFit.v1.core.models.FitGroup;
import org.beFit.v1.repositories.FitGroupRepository;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.FitGroupEntity;

import java.math.BigDecimal;

public class FitGroupService {
	private final FitGroupRepository fitGroupRepository;

	public FitGroupService(FitGroupRepository fitGroupRepository) {
		this.fitGroupRepository = fitGroupRepository;
	}

	public FitGroup createFitGroup(String name, BigDecimal stake) {
		return Mappers.fromFitGroupEntity(
				fitGroupRepository.createFitGroup(name, stake)
		);
	}

	public FitGroup getFitGroup(int id) {
		return Mappers.fromFitGroupEntity(
				fitGroupRepository.getFitGroup(id)
		);
	}

	public void deleteFitGroup(int id) {
		fitGroupRepository.deleteFitGroup(id);
	}
}
