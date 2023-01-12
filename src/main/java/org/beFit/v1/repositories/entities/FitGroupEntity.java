package org.beFit.v1.repositories.entities;

import java.math.BigDecimal;

public class FitGroupEntity {
	public final Integer id;
	public final String name;
	public final BigDecimal stake;
	public final BigDecimal balance;

	public FitGroupEntity(Integer id, String name, BigDecimal stake, BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.stake = stake;
		this.balance = balance;
	}
}
