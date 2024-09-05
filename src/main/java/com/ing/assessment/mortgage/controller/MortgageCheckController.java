package com.ing.assessment.mortgage.controller;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import com.ing.assessment.mortgage.domain.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.service.MortgageCalculator;
import com.ing.assessment.mortgage.service.MortgageRuleProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;


@RestController
@RequestMapping("/mortgage-check")
public class MortgageCheckController {

    private final MortgageRuleProcessor mortgageRuleProcessor;
    private final MortgageCalculator mortgageCalculator;

    public MortgageCheckController(MortgageRuleProcessor mortgageRuleProcessor, MortgageCalculator mortgageCalculator) {
        this.mortgageRuleProcessor = mortgageRuleProcessor;
        this.mortgageCalculator = mortgageCalculator;
    }

    @PostMapping
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@RequestBody MortgageCheck mortgageCheck) {
        checkInput(mortgageCheck);
        boolean feasible = mortgageRuleProcessor.isMortgageFeasible(mortgageCheck);
        BigDecimal monthlyCosts = mortgageCalculator.calculateMonthlyCosts(mortgageCheck);//I could have put a condition to only calculate if isfeasible is true, but I wasn't sure if I should.
        MortgageCheckResponse response = new MortgageCheckResponse(feasible, monthlyCosts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static void checkInput(MortgageCheck mortgageCheck) {
        Objects.requireNonNull(mortgageCheck, "MortgageCheck cannot be null");
        Objects.requireNonNull(mortgageCheck.income(), "Income cannot be null");
        Objects.requireNonNull(mortgageCheck.loanValue(), "LoanValue cannot be null");
        Objects.requireNonNull(mortgageCheck.homeValue(), "HomeValue cannot be null");
    }

}