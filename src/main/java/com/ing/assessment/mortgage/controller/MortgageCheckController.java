package com.ing.assessment.mortgage.controller;

import com.ing.assessment.mortgage.domain.MortgageCheck;
import com.ing.assessment.mortgage.domain.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.service.MortgageCalculator;
import com.ing.assessment.mortgage.service.MortgageRuleProcessor;
import com.ing.assessment.util.exception.IngAssessmentException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


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
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@Valid @RequestBody MortgageCheck mortgageCheck) {
        try {
            boolean feasible = mortgageRuleProcessor.isMortgageFeasible(mortgageCheck);
            BigDecimal monthlyCosts = mortgageCalculator.calculateMonthlyCosts(mortgageCheck);//I could have put a condition to only calculate if isfeasible is true, but I wasn't sure if I should.
            MortgageCheckResponse response = new MortgageCheckResponse(feasible, monthlyCosts);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IngAssessmentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}