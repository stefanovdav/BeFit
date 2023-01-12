package org.beFit.v1.core.models;

import java.math.BigDecimal;

public class FitGroup {
	public final Integer id;
	public final String name;
	public final BigDecimal stake;
	public final BigDecimal balance;

	public FitGroup(Integer id, String name, BigDecimal stake, BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.stake = stake;
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "FitGroup{" +
				"id=" + id +
				", name='" + name + '\'' +
				", stake=" + stake +
				", balance=" + balance +
				'}';
	}
}
