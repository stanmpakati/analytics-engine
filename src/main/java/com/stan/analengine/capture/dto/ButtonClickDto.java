package com.stan.analengine.capture.dto;
import com.stan.analengine.model.ButtonClick;
import com.stan.analengine.model.LinkClick;
import lombok.Data;

import java.util.Date;

@Data
public class ButtonClickDto {
  private String buttonName;
  private Date clickTime;

  public static ButtonClick toButtonClick(ButtonClickDto buttonClickDto) {
    return new ButtonClick(buttonClickDto.getButtonName(), buttonClickDto.getClickTime());
  }
}
