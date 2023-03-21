package com.stan.analengine.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
  public static <T> ResponseTemplate<T> generateResponse(String message, HttpStatus status, T responseObj) {
    ResponseTemplate<T> res = new ResponseTemplate<>();
    res.setStatus(String.valueOf(status.value()));
    res.setMessage(message);
    res.setData(responseObj);
    return res;
  }

  public static <T> ResponseTemplate<T> generateOkResponse(T responseObj) {
    ResponseTemplate<T> res = new ResponseTemplate<>();
    res.setStatus("200");
    res.setMessage("Success");
    res.setData(responseObj);
    return res;
  }

  public static ResponseEntity<CustomExceptionTemplate> generateErrorResponse(CustomExceptionTemplate responseObj) {
    HttpStatus status = HttpStatus.OK;

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("message", "Success");
    map.put("status", status.value());
    map.put("data", responseObj);

    return new ResponseEntity(map,status);
  }
}
