package com.stan.analengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;
  private String pageName;
  private Date startTime;
  private Date endTime;
  private Integer activeTime;
  private Integer idleTime;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<ClickLocation> clickLocations = new ArrayList<>();
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<ButtonClick> buttonClicks = new HashSet<>();
  private Integer buttonClickCount;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<LinkClick> linkClicks = new HashSet<>();
  private Integer linkClickCount;
  private Integer scrollCount;
  private Integer mouseMovementCount;
  @ManyToOne
  @JsonBackReference
  private BrowserEvent browserEvent;

  @CreationTimestamp
  @Column(nullable = false)
  private ZonedDateTime created = ZonedDateTime.now();

  @UpdateTimestamp
  @Column(nullable = false)
  private ZonedDateTime updated;

  private ZonedDateTime deleted;

  @Transient
  private Object loadVersion;


  public void addToButtonClicks(ButtonClick buttonClick) {
    buttonClick.setBrowserEvent(this);
    this.buttonClicks.add(buttonClick);
  }

  public void addToLinkClicks(LinkClick linkClick) {
    linkClick.setBrowserEvent(this);
    this.linkClicks.add(linkClick);
  }

  public void addToClickLocations(ClickLocation clickLocation) {
//    clickLocation.setBrowserEvent(this);
    this.clickLocations.add(clickLocation);
  }
}
