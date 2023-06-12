package com.stan.analengine.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsOverviewDto {
  private Long visits;
  private Long uniqueVisitors;
  private Long devices;
  private Double averageVisitDuration;
}
