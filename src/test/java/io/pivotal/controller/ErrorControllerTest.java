package io.pivotal.controller;

import com.google.gson.JsonSyntaxException;
import io.pivotal.TestUtilities;
import io.pivotal.errorHandling.ErrorController;
import io.pivotal.errorHandling.ErrorPathConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import retrofit.RetrofitError;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pivotal on 1/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ErrorControllerTest {

    @InjectMocks
    ErrorController subject;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void testGenericError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/GenericError.json")));
    }

    @Test
    public void testRuntimeError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_RUNTIME_ERROR_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/GenericError.json")));
    }

    @Test
    public void testNoQueryParamsError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_NO_PARAMS_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/WeatherNoParamError.json")));
    }

    @Test
    public void testOutOfRangeQueryParamsError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_PARAM_OUT_OF_RANGE_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/WeatherParamOutOfRangeError.json")));
    }

    @Test
    public void testRetrofitFailure() throws Exception {
        RetrofitError e = RetrofitError.unexpectedError("http://example.com/", new RuntimeException("nope!"));
        String expected = TestUtilities.jsonFileToString("src/test/resources/output/RetrofitError.json");
        assertEquals(expected, subject.errorRetrofitConfig(e));

    }

    @Test
    public void testStopNotFoundError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_STOP_NOT_FOUND_PATH))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopNotFoundError.json")));
    }

    @Test
    public void testUserNotFoundError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_USER_NOT_FOUND_PATH))
                .andExpect(status().isNotFound())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/UserNotFoundError.json")));
    }

    @Test
    public void testUserAlreadyExists() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.USER_ALREADY_EXISTS_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/UserAlreadyExistsError.json")));
    }

    @Test
    public void testBadJsonError() throws Exception {
        JsonSyntaxException e = new JsonSyntaxException("yo");
        String expected = TestUtilities.jsonFileToString("src/test/resources/output/BadJsonError.json");
        assertEquals(expected, subject.badJson(e));
    }
}
