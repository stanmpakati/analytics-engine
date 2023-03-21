package com.stan.analengine.response;

import lombok.Data;

@Data
public class ResponseTemplate<T> {
  private String status;
  private String message;
  private T data;
}
