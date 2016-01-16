package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.domain.BusStop;
import io.pivotal.domain.BusStopRepository;
import io.pivotal.domain.User;
import io.pivotal.domain.UserRepository;
import io.pivotal.errorHandling.UserAlreadyExistsException;
import io.pivotal.errorHandling.UserNotFoundException;
import io.pivotal.service.BusService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.HashSet;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pivotal on 1/14/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BusStopRepository busStopRepository;
    @Mock
    BusService busService;
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
        when(testUser.getStops()).thenReturn(
                new HashSet<>(Arrays.asList(
                        new BusStop("twelve a", "12_A"),
                        new BusStop("sixteen c", "16_C"),
                        new BusStop("another bus stop name", "12_A_J_56")
                )));

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

    @Test(expected = MissingServletRequestParameterException.class)
    public void testGetStopsWithNoUsername() throws Throwable {
        try {
            mockMvc.perform(get("/users/stops"));
        } finally {
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test
    public void testAddUser() throws Exception {
        when(userRepository.findByUsername("Test")).thenReturn(null);

        mockMvc.perform(post("/users/").contentType(MediaType.TEXT_PLAIN)
                .content("Test"))
                .andExpect(status().isOk());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUserAlreadyExists() throws Throwable {
        when(userRepository.findByUsername("Test")).thenReturn(testUser);
        try {
            mockMvc.perform(post("/users/").contentType(MediaType.TEXT_PLAIN)
                    .content("Test"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }
    }

    @Test
    public void testAddNewStop() throws Exception {
        when(userRepository.findByUsername("Test")).thenReturn(testUser);
        when(busStopRepository.findByApiId("12_B")).thenReturn(null);
        when(busService.getStopName("12_B")).thenReturn("twelve bee");

        mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                .content("{\"stopId\":\"12_B\"}"))
                .andExpect(status().isOk());
        Mockito.verify(userRepository, times(1)).save(testUser);
        Mockito.verify(busStopRepository, times(1)).save(any(BusStop.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNewStopInvalidJson() throws Throwable {
        try {
            mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"stoopid\":\"12_B\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            verifyNoMoreInteractions(busService);
            verifyNoMoreInteractions(busStopRepository);
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test
    public void testAddExistingStop() throws Exception {
        when(userRepository.findByUsername("Test")).thenReturn(testUser);
        BusStop busStop = new BusStop("Thirteen see", "13_C");
        when(busStopRepository.findByApiId("13_C")).thenReturn(busStop);

        mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                .content("{\"stopId\":\"13_C\"}"))
                .andExpect(status().isOk());
        Mockito.verify(userRepository, times(1)).save(testUser);
        Mockito.verify(busStopRepository, times(0)).save(any(BusStop.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testAddStopUserNotFound() throws Throwable {
        when(userRepository.findByUsername("Test")).thenReturn(null);
        try {
            mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"stopId\":\"12_B\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }
    }
}