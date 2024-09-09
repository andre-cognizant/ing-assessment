package com.ing.assessment.mortgage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.assessment.mortgage.domain.MortgageCheck;
import com.ing.assessment.mortgage.domain.dto.MortgageCheckResponse;
import com.ing.assessment.rate.dto.InterestRate;
import com.ing.assessment.rate.repository.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageCheckControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DataLoader dataLoader;

	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.name}")
	private String password;

	@BeforeEach
	void setUp() {
		dataLoader.getInterestRates().clear();
		dataLoader.getInterestRates()
			.addAll(Arrays.asList(new InterestRate(10, new BigDecimal("2.5"), LocalDateTime.now()),
					new InterestRate(20, new BigDecimal("3.0"), LocalDateTime.now()),
					new InterestRate(30, new BigDecimal("3.5"), LocalDateTime.now())));
	}

	@Test
	void testMortgageCheckFeasible() throws Exception {
		MortgageCheck mortgageCheck = new MortgageCheck(new BigDecimal("100000"), 30, new BigDecimal("300000"),
				new BigDecimal("400000"));

		MvcResult result = mockMvc
			.perform(post("/mortgage-check").with(httpBasic(username, password))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mortgageCheck)))
			.andExpect(status().isOk())
			.andReturn();

		MortgageCheckResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
				MortgageCheckResponse.class);

		assertTrue(response.feasible());
		assertNotNull(response.monthlyCosts());
		assertTrue(response.monthlyCosts().compareTo(BigDecimal.ZERO) > 0);
	}

	@Test
	void testMortgageCheckNotFeasibleDueToHomeValue() throws Exception {
		MortgageCheck mortgageCheck = new MortgageCheck(new BigDecimal("100000"), 30, new BigDecimal("400000"),
				new BigDecimal("300000"));

		MvcResult result = mockMvc
			.perform(post("/mortgage-check").with(httpBasic(username, password))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mortgageCheck)))
			.andExpect(status().isOk())
			.andReturn();

		MortgageCheckResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
				MortgageCheckResponse.class);

		assertFalse(response.feasible());
		assertNotNull(response.monthlyCosts());
	}

	@Test
	void testMortgageCheckNotFeasibleDueToIncomeRatio() throws Exception {
		MortgageCheck mortgageCheck = new MortgageCheck(new BigDecimal("50000"), 30, new BigDecimal("300000"),
				new BigDecimal("400000"));

		MvcResult result = mockMvc
			.perform(post("/mortgage-check").with(httpBasic(username, password))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mortgageCheck)))
			.andExpect(status().isOk())
			.andReturn();

		MortgageCheckResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
				MortgageCheckResponse.class);

		assertFalse(response.feasible());
		assertNotNull(response.monthlyCosts());
	}

	@Test
	void testMortgageCheckWithInvalidMaturityPeriod() throws Exception {
		MortgageCheck mortgageCheck = new MortgageCheck(new BigDecimal("100000"), 15, new BigDecimal("300000"),
				new BigDecimal("400000"));

		mockMvc
			.perform(post("/mortgage-check").with(httpBasic(username, password))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mortgageCheck)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testUnauthorizedAccess() throws Exception {
		MortgageCheck mortgageCheck = new MortgageCheck(new BigDecimal("100000"), 30, new BigDecimal("300000"),
				new BigDecimal("400000"));

		mockMvc
			.perform(post("/mortgage-check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mortgageCheck)))
			.andExpect(status().isUnauthorized());
	}

}