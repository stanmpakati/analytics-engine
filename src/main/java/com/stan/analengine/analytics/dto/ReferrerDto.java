package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReferrerDto {
  public String name;
  public Long count;
}
