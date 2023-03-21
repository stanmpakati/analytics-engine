package com.stan.analengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class LinkClick {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  private String linkName;
  private String linkDestination;
  private Date clickTime;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "link_clicks")
  @JsonBackReference
  private PageEvent browserEvent;

  public LinkClick(String linkName, Date clickTime, String linkDestination) {
    this.linkName = linkName;
    this.clickTime = clickTime;
    this.linkDestination = linkDestination;
  }
}
