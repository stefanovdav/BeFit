package org.beFit.v1.core;

import org.beFit.v1.core.models.FitGroup;
import org.beFit.v1.repositories.FitGroupRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Random;

public class FitGroupService {
	private int id = 0;
	private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final FitGroupRepository fitGroupRepository;

	public FitGroupService(FitGroupRepository fitGroupRepository) {
		this.fitGroupRepository = fitGroupRepository;
	}

	public FitGroup createFitGroup(String name, BigDecimal stake, Date end, int tax) {
		String key = generateGroupKey(id++);
		return Mappers.fromFitGroupEntity(fitGroupRepository.createFitGroup(name, stake, tax, end, generateGroupKey(id)));
	}

	public FitGroup getFitGroup(int id) {
		return Mappers.fromFitGroupEntity(fitGroupRepository.getFitGroup(id));
	}

	public void deleteFitGroup(int id) {
		fitGroupRepository.deleteFitGroup(id);
	}

	private String generateGroupKey(int id) {
		StringBuilder key = new StringBuilder();
		key.append(id);
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			key.append(CHARS.charAt(random.nextInt(CHARS.length())));
		}
		return key.toString();
	}

	public void updateParticipantsCount(Integer groupId, int count) {
		fitGroupRepository.updateParticipantsCount(groupId, count);
	}

}
