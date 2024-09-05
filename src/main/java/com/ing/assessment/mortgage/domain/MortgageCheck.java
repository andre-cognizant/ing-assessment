package com.ing.assessment.mortgage.domain;

import java.math.BigDecimal;

public record MortgageCheck(
        BigDecimal income,
        int maturityPeriod,
        BigDecimal loanValue,
        BigDecimal homeValue
) { }