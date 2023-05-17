package com.stan.analengine.analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class VisitorsDto {
  private List<VisitorsIntervalDto> visitors;
  private List<VisitorsIntervalDto> uniqueVisitors;
}
