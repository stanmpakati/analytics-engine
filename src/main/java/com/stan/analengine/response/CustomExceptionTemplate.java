package com.stan.analengine.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CustomExceptionTemplate {
  private String code;
  private String message;
  private List<Object> errors;
}
