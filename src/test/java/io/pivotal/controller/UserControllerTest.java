package io.pivotal.controller;

import com.google.gson.JsonSyntaxException;
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
import org.mockito.ArgumentMatcher;
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
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(testUser);
        mockMvc.perform(get("/users/stops?username=Test"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopsForTestUser.json")));
    }

    @Test
    public void testGetUsers() throws Exception {
        List<User> userList = Arrays.asList(
                new User("Alice"),
                new User("Bob"),
                new User("Charlene")
        );
        when(userRepository.findAll()).thenReturn(userList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/GetAllUsers.json")));
        verify(userRepository).findAll();
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetStopsWithUsernameNotFound() throws Throwable {
        try {
            when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(null);
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
        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(null);

        mockMvc.perform(post("/users/").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Test\"}"))
                .andExpect(status().isOk());
        Mockito.verify(userRepository, times(1)).save(argThat(new isUserWithUserName("Test")));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUserAlreadyExists() throws Throwable {
        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(testUser);
        try {
            mockMvc.perform(post("/users/").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"Test\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserMissingUsername() throws Throwable {
        try {
            mockMvc.perform(post("/users/").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"stoopid\":\"12_B\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test(expected = JsonSyntaxException.class)
    public void testAddUserInvalidJson() throws Throwable {
        try {
            mockMvc.perform(post("/users/").contentType(MediaType.APPLICATION_JSON)
                    .content("\"stoopid\":\"12_B\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test
    public void testAddNewStop() throws Exception {
        String expectedApiId = "12_B";
        String expectedName = "twelve bee";
        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(testUser);
        when(busStopRepository.findByApiId(expectedApiId)).thenReturn(null);
        when(busService.getStopName(expectedApiId)).thenReturn(expectedName);

        mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                .content("{\"stopId\":\"12_B\"}"))
                .andExpect(status().isOk());
        Mockito.verify(busService, times(1)).getStopName(expectedApiId);
        Mockito.verify(userRepository, times(1)).save(testUser);
        Mockito.verify(busStopRepository, times(1)).save(argThat(
                new isStopWithApiIdAndName(expectedApiId, expectedName)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNewStopMissingStopId() throws Throwable {
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

    @Test(expected = JsonSyntaxException.class)
    public void testAddNewStopInvalidJson() throws Throwable {
        try {
            mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"stoopid\":\"12_B\""));
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
        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(testUser);
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
        when(userRepository.findByUsernameIgnoreCase("Test")).thenReturn(null);
        try {
            mockMvc.perform(post("/users/Test/stops").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"stopId\":\"12_B\"}"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            Mockito.verify(userRepository, times(0)).save(any(User.class));
        }
    }

    private class isUserWithUserName extends ArgumentMatcher<User> {
        private final String expectedUsername;

        public isUserWithUserName(String username) {
            expectedUsername = username;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof User)) {
                return false;
            }
            return ((User) argument).getUsername().equals(expectedUsername);
        }
    }

    private class isStopWithApiIdAndName extends ArgumentMatcher<BusStop> {
        private final String expectedApiId;
        private final String expectedName;

        public isStopWithApiIdAndName(String expectedApiId, String expectedName) {
            this.expectedApiId = expectedApiId;
            this.expectedName = expectedName;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof BusStop)) {
                return false;
            }
            return ((BusStop) argument).getApiId().equals(expectedApiId) &&
                    ((BusStop) argument).getName().equals(expectedName);
        }
    }
}