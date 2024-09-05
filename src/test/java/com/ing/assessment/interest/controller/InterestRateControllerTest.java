package com.ing.assessment.interest.controller;

import com.ing.assessment.interest.dto.InterestRate;
import com.ing.assessment.interest.repository.DataLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterestRateControllerTest {

    @Mock
    private DataLoader dataLoader;

    @InjectMocks
    private InterestRateController interestRateController;

    @Test
    void testGetInterestRates_Null() {
        when(dataLoader.getInterestRates()).thenReturn(null);
        ResponseEntity<List<InterestRate>> response = interestRateController.getInterestRates();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetInterestRates_Empty() {
        when(dataLoader.getInterestRates()).thenReturn(new ArrayList<>());
        ResponseEntity<List<InterestRate>> response = interestRateController.getInterestRates();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetInterestRates_NotEmpty() {
        List<InterestRate> interestRates = new ArrayList<>();
        interestRates.add(new InterestRate(1, BigDecimal.valueOf(1.0), LocalDateTime.now()));
        when(dataLoader.getInterestRates()).thenReturn(interestRates);
        ResponseEntity<List<InterestRate>> response = interestRateController.getInterestRates();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(interestRates, response.getBody());
    }
}