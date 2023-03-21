package com.stan.analengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stan.analengine.model.BrowserEvent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ButtonClick {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  private String buttonName;
  private Date clickTime;


  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "button_clicks")
  @JsonBackReference
  private PageEvent browserEvent;

  public ButtonClick(String buttonName, Date clickTime) {
    this.buttonName = buttonName;
    this.clickTime = clickTime;
  }
}
