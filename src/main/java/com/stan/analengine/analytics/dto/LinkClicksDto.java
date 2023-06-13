package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkClicksDto {
  private String page;
  private Integer clickCount;
  private String linkName;
  private String linkDestination;
}
