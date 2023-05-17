package com.stan.analengine.analytics.dto;

import lombok.Data;

import java.util.Date;

@Data
public class VisitorsIntervalDto {
  private Integer visits;
  private Date time;
}
