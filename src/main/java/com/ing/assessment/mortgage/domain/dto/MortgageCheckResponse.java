package com.ing.assessment.mortgage.domain.dto;

import java.math.BigDecimal;

public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCosts) {
}