package com.stan.analengine.capture.dto;

import com.stan.analengine.model.LinkClick;
import lombok.Data;

import java.util.Date;

@Data
public class LinkClickDto {
  private String linkName;
  private String linkDestination;
  private Date clickTime;

  public static LinkClick toLinkClick(LinkClickDto linkClickDto) {
    return new LinkClick(linkClickDto.getLinkName(), linkClickDto.getClickTime(), linkClickDto.getLinkDestination());
  }
}
