package com.ing.assessment.mortgage.service.rules;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import org.springframework.stereotype.Service;

@Service
public class HomeValueRule implements MortgageRule {

    @Override
    public boolean isFeasible(MortgageCheck mortgageCheck) {
        return mortgageCheck.loanValue().compareTo(mortgageCheck.homeValue()) <= 0;
    }
}
