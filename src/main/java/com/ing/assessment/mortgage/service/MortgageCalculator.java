package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import com.ing.assessment.rate.dto.InterestRate;
import com.ing.assessment.rate.repository.DataLoader;
import com.ing.assessment.util.exception.IngAssessmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MortgageCalculator {

    @Autowired
    private DataLoader dataLoader;

    public BigDecimal calculateMonthlyCosts(MortgageCheck mortgageCheck) {
        BigDecimal loanValue = mortgageCheck.loanValue();
        BigDecimal annualInterestRate = getInterestRate(mortgageCheck.maturityPeriod());
        
        int numberOfPayments = mortgageCheck.maturityPeriod() * 12;
        BigDecimal totalDebt = calculateTotalDebt(mortgageCheck, loanValue, annualInterestRate);
        BigDecimal monthlyPayment = totalDebt.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP);

        return monthlyPayment;
    }

    private static BigDecimal calculateTotalDebt(MortgageCheck mortgageCheck, BigDecimal loanValue, BigDecimal annualInterestRate) {
        return loanValue.add(loanValue.multiply(annualInterestRate.divide(new BigDecimal(100)))
                .multiply(new BigDecimal(mortgageCheck.maturityPeriod())));
    }

    private BigDecimal getInterestRate(int maturityPeriod) {
        return dataLoader.getInterestRates().stream()
                .filter(rate -> rate.getMaturityPeriod() == maturityPeriod)
                .findFirst()
                .map(InterestRate::getInterestRate)
                .orElseThrow(() -> new IngAssessmentException("Interest rate not found for maturity period: " + maturityPeriod));
    }
}
