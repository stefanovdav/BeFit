package org.beFit.v1.core.models;

import java.math.BigDecimal;
import java.sql.Date;

public class FitGroup {
	public final Integer id;
	public final String name;
	public final BigDecimal stake;
	public final Integer taxPercent;
	public final Date startDate;
	public final Date endDate;
	public final Integer participants;


	public FitGroup(Integer id, String name, BigDecimal stake, Integer taxPercent, Date startDate, Date endDate, Integer participants) {
		this.id = id;
		this.name = name;
		this.stake = stake;
		this.taxPercent = taxPercent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "FitGroup{" +
				"id=" + id +
				", name='" + name + '\'' +
				", stake=" + stake +
				", taxPercent=" + taxPercent +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", participants=" + participants +
				'}';
	}
}
