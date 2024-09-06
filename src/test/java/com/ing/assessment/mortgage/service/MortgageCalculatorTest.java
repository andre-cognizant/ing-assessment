package com.ing.assessment.mortgage.service;

import com.ing.assessment.rate.dto.InterestRate;
import com.ing.assessment.rate.repository.DataLoader;
import com.ing.assessment.mortgage.domain.MortgageCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MortgageCalculatorTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    private MortgageCalculator mortgageCalculator;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(dataLoader.getInterestRates()).thenReturn(Arrays.asList(
                new InterestRate(10, new BigDecimal("5"), now),
                new InterestRate(20, new BigDecimal("6"), now)
        ));
    }

    @ParameterizedTest
    @CsvSource({
            "50000, 10, 200000, 300000, 2500.00",
            "50000, 20, 200000, 300000, 1833.33",
            "50000, 10, 150000, 300000, 1875.00"
    })
    void testCalculateMonthlyCosts(String income, int maturityPeriod, String loanValue, String homeValue, String expectedMonthlyCosts) {
        MortgageCheck mortgageCheck = new MortgageCheck(
                new BigDecimal(income),
                maturityPeriod,
                new BigDecimal(loanValue),
                new BigDecimal(homeValue)
        );

        BigDecimal actualMonthlyCosts = mortgageCalculator.calculateMonthlyCosts(mortgageCheck);
        assertEquals(new BigDecimal(expectedMonthlyCosts), actualMonthlyCosts);
    }

    @Test
    void testCalculateMonthlyCosts_WithZeroLoanValue() {
        MortgageCheck mortgageCheck = new MortgageCheck(
                new BigDecimal("50000"),
                10,
                BigDecimal.ZERO,
                new BigDecimal("300000")
        );

        BigDecimal actualMonthlyCosts = mortgageCalculator.calculateMonthlyCosts(mortgageCheck);
        assertEquals(BigDecimal.ZERO.setScale(2), actualMonthlyCosts);
    }

    @Test
    void testCalculateMonthlyCosts_WithUnsupportedMaturityPeriod() {
        MortgageCheck mortgageCheck = new MortgageCheck(
                new BigDecimal("50000"),
                15,
                new BigDecimal("200000"),
                new BigDecimal("300000")
        );

        assertThrows(RuntimeException.class, () -> mortgageCalculator.calculateMonthlyCosts(mortgageCheck));
    }

}