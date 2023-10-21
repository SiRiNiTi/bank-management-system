package com.example.bank.controllers;

import com.example.bank.models.dtos.ClientDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    @Sql({"classpath:TruncateAllTables.sql", "classpath:Add3Accounts.sql"})
    public void getAllAccounts() throws Exception {
        mvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].accountNum", is(1)))
                .andExpect(jsonPath("$[0].currentBalance", is(50000)))
                .andExpect(jsonPath("$[0].client.pin", is(1234)))
                .andExpect(jsonPath("$[1].accountNum", is(2)))
                .andExpect(jsonPath("$[1].currentBalance", is(4000)))
                .andExpect(jsonPath("$[1].client.pin", is(1234)))
                .andExpect(jsonPath("$[2].accountNum", is(3)))
                .andExpect(jsonPath("$[2].currentBalance", is(10000)))
                .andExpect(jsonPath("$[2].client.pin", is(4444)));
    }

    @Test
    @Sql("classpath:TruncateAllTables.sql")
    public void createAccount_givenNewPin_thenAccountCreatedSuccessful() throws Exception {
        ClientDto clientModel = new ClientDto();
        clientModel.setName("Fedor Fedorov");
        clientModel.setPin(1111);
        mvc.perform(post("/account")
                .contentType("application/json")
                .accept("application/json")
                .content(mapper.writeValueAsString(clientModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNum", is(1)));
        mvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountNum", is(1)))
                .andExpect(jsonPath("$[0].currentBalance", is(0)))
                .andExpect(jsonPath("$[0].client.pin", is(clientModel.getPin())));
    }

    @Test
    @Sql({"classpath:TruncateAllTables.sql", "classpath:Add3Accounts.sql"})
    public void createAccount_givenExistedPin_thenAccountCreatedSuccessful() throws Exception {
        ClientDto clientModel = new ClientDto();
        clientModel.setName("Fedor Fedorov");
        clientModel.setPin(4444);
        mvc.perform(post("/account")
                .contentType("application/json")
                .accept("application/json")
                .content(mapper.writeValueAsString(clientModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNum", is(4)));
        mvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].accountNum", is(4)))
                .andExpect(jsonPath("$[3].currentBalance", is(0)))
                .andExpect(jsonPath("$[3].client.pin", is(clientModel.getPin())));
    }

    @Test
    @Sql("classpath:TruncateAllTables.sql")
    public void createAccount_givenInvalidPin_thenThrowError() throws Exception {
        ClientDto clientModel = new ClientDto();
        clientModel.setName("Fedor Fedorov");
        clientModel.setPin(8);
        mvc.perform(post("/account")
                .contentType("application/json")
                .accept("application/json")
                .content(mapper.writeValueAsString(clientModel)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("{pin=must be greater than or equal to 1000}")));
    }
}