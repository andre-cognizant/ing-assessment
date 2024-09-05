package com.ing.assessment.mortgage.service.rules;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IncomeRatioRule implements MortgageRule {

    private static final BigDecimal MAX_INCOME_RATIO = new BigDecimal(4);

    @Override
    public boolean isFeasible(MortgageCheck mortgageCheck) {
        BigDecimal loanToIncomeRatio = mortgageCheck.loanValue().divide(mortgageCheck.income(), 2, RoundingMode.HALF_UP);
        return loanToIncomeRatio.compareTo(MAX_INCOME_RATIO) <= 0;
    }
}
