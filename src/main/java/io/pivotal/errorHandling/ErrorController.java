package io.pivotal.errorHandling;

import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import retrofit.RetrofitError;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {
    @Override
    public String getErrorPath() {
        return ErrorPathConstants.ERROR_PATH;
    }

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(ErrorPathConstants.ERROR_PATH)
    public
    @ResponseBody
    String unknownError() {
        return new ErrorPresenter(ErrorMessages.UNKNOWN.getErrorMessage()).toJson();
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(ErrorPathConstants.ERROR_RUNTIME_ERROR_PATH)
    public
    @ResponseBody
    String runtimeException(RuntimeException e) {
        logger.error("Unhandled exception", e);
        return new ErrorPresenter(ErrorMessages.UNKNOWN.getErrorMessage()).toJson();
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(ErrorPathConstants.ERROR_NO_PARAMS_PATH)
    public
    @ResponseBody
    String errorNoParams() {
        return new ErrorPresenter(ErrorMessages.MISSING_PARAM.getErrorMessage()).toJson();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(ErrorPathConstants.ERROR_PARAM_OUT_OF_RANGE_PATH)
    public
    @ResponseBody
    String errorOutOfRangeQueryParams() {
        return new ErrorPresenter(ErrorMessages.PARAM_OUT_OF_RANGE.getErrorMessage()).toJson();
    }

    @ExceptionHandler({RetrofitError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(ErrorPathConstants.ERROR_RETROFIT_CONFIG_PATH)
    public
    @ResponseBody
    String errorRetrofitConfig(RetrofitError e) {
        String message = "Error making service call to URL: " + e.getUrl() + " " + e.getMessage();
        logger.error(message, e);
        return new ErrorPresenter(ErrorMessages.RETROFIT.getErrorMessage()).toJson();
    }

    @ExceptionHandler({StopNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(ErrorPathConstants.ERROR_STOP_NOT_FOUND_PATH)
    public
    @ResponseBody
    String errorStopNotFound() {
        return new ErrorPresenter(ErrorMessages.STOP_NOT_FOUND.getErrorMessage()).toJson();
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(ErrorPathConstants.ERROR_USER_NOT_FOUND_PATH)
    public
    @ResponseBody
    String errorUserNotFound() {
        return new ErrorPresenter(ErrorMessages.USER_NOT_FOUND.getErrorMessage()).toJson();
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(ErrorPathConstants.USER_ALREADY_EXISTS_PATH)
    public
    @ResponseBody
    String userAlreadyExists() {
        return new ErrorPresenter(ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage()).toJson();
    }

    @ExceptionHandler({JsonSyntaxException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(ErrorPathConstants.JSON_SYNTAX_ERROR_PATH)
    public
    @ResponseBody
    String badJson(JsonSyntaxException e) {
        logger.error("JSON syntax exception", e);
        return new ErrorPresenter(ErrorMessages.BAD_JSON.getErrorMessage()).toJson();
    }
}
