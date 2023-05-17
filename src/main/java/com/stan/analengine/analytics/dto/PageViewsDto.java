package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageViewsDto {
  private String pageName;
  private Long visits;
  private Long uniqueVisits;
}
