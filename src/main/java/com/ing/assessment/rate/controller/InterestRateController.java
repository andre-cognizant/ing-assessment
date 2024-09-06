package com.ing.assessment.rate.controller;

import com.ing.assessment.rate.dto.InterestRate;
import com.ing.assessment.rate.repository.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interest-rates")
public class InterestRateController {

    @Autowired
    private DataLoader dataLoader;

    @GetMapping
    public ResponseEntity<List<InterestRate>> getInterestRates() {
        List<InterestRate> interestRates = dataLoader.getInterestRates();
        if (interestRates == null || interestRates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(interestRates, HttpStatus.OK);
    }
}
