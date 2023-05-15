package com.stan.analengine.capture.dto;

import com.stan.analengine.model.ClickLocation;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class PageEventDto {
  private UUID clientSessionId;
  private String pageName;
  private Date startTime;
  private Date endTime;
  private Integer activeTime;
  private Integer idleTime;
  private List<ClickLocation> clickLocations;
  private Set<ButtonClickDto> buttonClicks;
  private Integer buttonClickCount;
  private List<LinkClickDto> linkClicks;
  private Integer linkClickCount;
  private Integer scrollCount;
  private Integer mouseMovementCount;
}

