package com.ing.assessment.mortgage.service.rules;

import com.ing.assessment.mortgage.domain.MortgageCheck;

public interface MortgageRule {
    boolean isFeasible(MortgageCheck mortgageCheck);
}
