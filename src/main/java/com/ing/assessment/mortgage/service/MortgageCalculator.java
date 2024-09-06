package com.ing.assessment.mortgage.service;

import com.ing.assessment.rate.dto.InterestRate;
import com.ing.assessment.rate.repository.DataLoader;
import com.ing.assessment.mortgage.domain.MortgageCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MortgageCalculator {

    @Autowired
    private DataLoader dataLoader;

    public BigDecimal calculateMonthlyCosts(MortgageCheck mortgageCheck) {
        BigDecimal loanValue = mortgageCheck.loanValue();
        BigDecimal annualInterestRate = getInterestRate(mortgageCheck.maturityPeriod());
        
        int numberOfPayments = mortgageCheck.maturityPeriod() * 12;
        
        BigDecimal totalAmount = loanValue.add(loanValue.multiply(annualInterestRate.divide(new BigDecimal(100))).multiply(new BigDecimal(mortgageCheck.maturityPeriod())));
        
        BigDecimal monthlyPayment = totalAmount.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP);

        return monthlyPayment;
    }
    
    private BigDecimal getInterestRate(int maturityPeriod) {
        List<InterestRate> rates = dataLoader.getInterestRates();
        for (InterestRate rate : rates) {
            if (rate.getMaturityPeriod() == maturityPeriod) {
                return rate.getInterestRate();
            }
        }
        throw new RuntimeException("Interest rate not found for maturity period: " + maturityPeriod);
    }
}
