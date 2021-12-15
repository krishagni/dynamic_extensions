package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class SkipLogic implements Serializable {
	private static final long serialVersionUID = 8304778004821290621L;

	private Long id;

	private Set<SkipRule> skipRules = new LinkedHashSet<SkipRule>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Set<SkipRule> getSkipRules() {
		return skipRules;
	}

	public void setSkipRules(Set<SkipRule> skipRules) {
		this.skipRules = skipRules;
	}
}
