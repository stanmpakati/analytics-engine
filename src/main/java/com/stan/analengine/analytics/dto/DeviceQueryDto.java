package com.stan.analengine.analytics.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceQueryDto {

  private Boolean isNewUser;
  private String os;
  private String browserName;
  private Date from = new Date(1);
  private Date to = new Date();
}
