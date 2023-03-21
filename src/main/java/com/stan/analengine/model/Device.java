package com.stan.analengine.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "browser_data", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Device {
  @Id
  @GeneratedValue
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID deviceId;
  private String userAgent;
  private Double networkDownLink;
  private String networkType;
  private Boolean cookiesEnabled;
  private String doNotTrack;
  private String language;
  private List<String> languages;
  private Boolean isNewUser;
  private String referrer;
  private String timeRegion;
  private String timezone;
  private String browserName;
  private String browserVersion;
  private String OSName;
  private String OSVersion;
  private String deviceType;
  private String deviceModel;
  private String deviceVendor;
  private String deviceScreenResolution;
  private String deviceScreenSize;
  private String deviceScreenOrientation;
  private String deviceDescription;
  @CreationTimestamp
  @Column(nullable = false)
  private Date created;
  @UpdateTimestamp
  @Column(nullable = false)
  private Date updated;
  private Date deleted;
  @Transient
  private Object loadVersion;

}
