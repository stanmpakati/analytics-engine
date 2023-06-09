package com.stan.analengine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "browser_event")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BrowserEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(unique = true, updatable = false)
  private UUID clientSessionId;

  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false)
  @JsonBackReference
  private Device device;
  @OneToMany(cascade = CascadeType.ALL)
  private List<PageEvent> pageEvents;
  @CreationTimestamp
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  @CreationTimestamp
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private Date updated;

  private Date deleted;

  @Transient
  private Object loadVersion;

  public BrowserEvent(Device device, UUID clientSessionId) {
    this.device = device;
    this.clientSessionId = clientSessionId;
  }

  public BrowserEvent(Device device1, Date date) {
  }


  public void addToPageEvent(PageEvent pageEvent) {
    pageEvent.setBrowserEvent(this);
    this.pageEvents.add(pageEvent);
  }


}
