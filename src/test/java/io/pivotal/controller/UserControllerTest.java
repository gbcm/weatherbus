package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.domain.User;
import io.pivotal.domain.UserRepository;
import io.pivotal.errorHandling.UserNotFoundException;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pivotal on 1/14/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    UserRepository userRepository;
    @Mock
    User testUser;
    @InjectMocks
    UserController subject;
    private MockMvc mockMvc;

    @Mock
    HandlerExceptionResolver handlerExceptionResolver;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testGetStops() throws Exception {
        when(testUser.getStopIds()).thenReturn(
                new HashSet<>(Arrays.asList("12_A", "16_C", "12_A_J_56")));

        when(userRepository.findByUsername("Test")).thenReturn(testUser);
        mockMvc.perform(get("/users/stops?username=Test"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopsForTestUser.json")));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetStopsWithUsernameNotFound() throws Throwable {
        try {
            when(userRepository.findByUsername("Test")).thenReturn(null);
            mockMvc.perform(get("/users/stops?username=Test"));
        } catch (NestedServletException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testGetStopsWithNoUsername() throws Exception {
//        when(testUser.getStopIds()).thenReturn(
//                new HashSet<>(Arrays.asList("12_A", "16_C", "12_A_J_56")));
//
//        when(userRepository.findByUsername("Test")).thenReturn(testUser);
//        mockMvc.perform(get("/users/stops?username=Test"))
//                .andExpect(status().isOk())
//                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
//                        "src/test/resources/output/StopsForTestUser.json")));
    }

    @Test
    public void testAddUser() throws Exception {

    }

    @Test
    public void testAddStop() throws Exception {

    }
}