package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyDto<T> {
  public T name;
  public Long count;
}
