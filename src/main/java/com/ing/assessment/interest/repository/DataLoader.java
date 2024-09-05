package com.ing.assessment.interest.repository;

import com.ing.assessment.interest.dto.InterestRate;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader {

    private List<InterestRate> interestRates = new ArrayList<>();

    @PostConstruct
    public void loadData() {
        interestRates = generateInterestRates();
    }

    public List<InterestRate> getInterestRates() {
        return interestRates;
    }

    // Generate 40 mocked interest rates at startup
    private List<InterestRate> generateInterestRates() {
        final BigDecimal INITIAL_RATE = new BigDecimal("5.000");
        final int ONE_YEAR = 1;
        final int FORTY_YEARS = 40;
        BigDecimal interestRate = INITIAL_RATE;

        for (int i = ONE_YEAR; i <= FORTY_YEARS; i++) {
            interestRate = interestRate.subtract(new BigDecimal("0.010"));
            BigDecimal formattedInterestRate = interestRate.setScale(3, RoundingMode.HALF_UP);
            LocalDateTime lastUpdate = LocalDateTime.now().minusDays(new Random().nextInt(1, 31));
            interestRates.add(new InterestRate(i, formattedInterestRate, lastUpdate));
        }

        return interestRates;
    }
}
