package com.stan.analengine.response;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Map<String, Object> res = new HashMap<>();
    List<Map<String, String>> errors = new ArrayList<>();
    List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();

    objectErrors.forEach((error) -> {
      Map<String, String> map = new HashMap<>();
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();

      map.put(fieldName, errorMessage);
      errors.add(map);
    });

    res.put("message", "Invalid Fields");
    res.put("code", "400");
    res.put("errors", errors);

    return res;
  }

//  @ResponseStatus(HttpStatus.FORBIDDEN)
//  @ExceptionHandler(AccessDeniedException.class)
//  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
//    return CustomExceptionTemplate.builder()
//        .code(res.getStatus().toString())
//        .message(res.getMessage())
//        .errors(Collections.singletonList(res.getReason().toString()))
//        .build();
//  }

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<CustomExceptionTemplate> invalidFormatException(final InvalidFormatException e) {
    return error(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CustomExceptionTemplate> HttpMessageNotReadableException(final HttpMessageNotReadableException e) {
    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
        .message("Un-understandable Data")
        .code("400")
        .errors(Collections.singletonList(e.getMessage()))
        .build();
    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<CustomExceptionTemplate> illegalStateException(final IllegalStateException e) {
    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
        .message(e.getMessage())
        .code("500")
        .errors(Collections.singletonList(e.getMessage()))
        .build();
    return new ResponseEntity<>(exceptionTemplate, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<CustomExceptionTemplate> dataIntegrityViolationException(final DataIntegrityViolationException e) {
    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
        .message(e.getCause().getCause().getMessage())
        .code("400")
        .errors(Collections.singletonList(e.getMessage()))
        .build();
    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
  }

//  @ExceptionHandler(OutOfScopeException.class)
//  public ResponseEntity<CustomExceptionTemplate> outOfScopeException(final OutOfScopeException e) {
//    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
//        .message(e.getMessage())
//        .code("400")
//        .errors(Collections.singletonList(e.getMessage()))
//        .build();
//    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
//  }
//
//  @ExceptionHandler(MissingFieldException.class)
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  public ResponseEntity<CustomExceptionTemplate> missingFieldException(final MissingFieldException e) {
//    String missingField = e.getFieldName();
//    String message = String.format("%s parameter is missing", missingField);
//
//    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
//        .message(message)
//        .code("400")
//        .errors(Collections.singletonList(e.getMessage()))
//        .build();
//    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
//  }
//
//  @ExceptionHandler(WebClientRequestException.class)
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  public ResponseEntity<CustomExceptionTemplate> missingFieldException(final WebClientRequestException e) {
//    e.printStackTrace();
//
//    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
//        .message(e.getMessage())
//        .code("500")
//        .errors(Collections.singletonList(e.getMessage()))
//        .build();
//    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
//  }
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<CustomExceptionTemplate> handleException(HttpServletResponse res) throws IOException {
//    logger.error("Unknown error------------\n{}", new Gson().toJson(res));
////    res.sendError(HttpStatus.BAD_REQUEST.value(), "");
//    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
//        .message("The Developer didn't handle this error in the best way :-)")
//        .code("400")
//        .errors(Collections.singletonList(res))
//        .build();
//    return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
//  }

  private ResponseEntity<CustomExceptionTemplate> error(final Exception exception, final HttpStatus httpStatus) {
    final String message = Optional.ofNullable(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    CustomExceptionTemplate exceptionTemplate = CustomExceptionTemplate.builder()
        .message("This other error")
        .code("400")
        .build();
    return new ResponseEntity<>(exceptionTemplate, httpStatus);
  }


//  @ExceptionHandler(Exception.class)
//  public void handleException(HttpServletResponse res) throws IOException {
//    res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
//  }
}