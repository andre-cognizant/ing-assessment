package com.ing.assessment.mortgage.domain;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MortgageCheck(@NotNull BigDecimal income, int maturityPeriod, @NotNull BigDecimal loanValue,
		@NotNull BigDecimal homeValue) {
}