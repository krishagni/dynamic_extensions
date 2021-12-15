package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateIntervalNode extends ExpressionNode implements Serializable {

	private static final long serialVersionUID = 1775518489015543756L;
	
	private int years, months, days;
	
	@Override
	public DataType getType() {
		return DataType.DATE_INTERVAL;
	}
	
	@Override
	public DateIntervalNode copy() {
		DateIntervalNode copy = new DateIntervalNode();
		super.copy(this, copy);
		copy.setYears(years);
		copy.setMonths(months);
		copy.setDays(days);
		return copy;
	}
	
	@Override
	public String[] getFormNames() {
		return new String[0];
	}	
	
	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + days;
		result = prime * result + months;
		result = prime * result + years;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		DateIntervalNode other = (DateIntervalNode) obj;
		return days == other.days && months == other.months && years == other.years;
	}

	@Override
	public boolean isPhi() {
		return false;
	}

	@Override
	public Set<FieldNode> getFields() {
		return Collections.emptySet(); 
	}
}
