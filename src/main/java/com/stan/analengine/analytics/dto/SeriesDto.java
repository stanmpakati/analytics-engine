package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriesDto {
  private String name;
  private Integer value;

}
