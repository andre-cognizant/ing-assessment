package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import com.ing.assessment.mortgage.service.rules.MortgageRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MortgageRuleProcessor {

	private final List<MortgageRule> rules;

	public MortgageRuleProcessor(List<MortgageRule> rules) {
		this.rules = rules;
	}

	public boolean isMortgageFeasible(MortgageCheck mortgageCheck) {
		for (MortgageRule rule : rules) {
			if (!rule.isFeasible(mortgageCheck)) {
				return false;
			}
		}
		return true;
	}

}
