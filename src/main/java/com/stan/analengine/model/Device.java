package com.stan.analengine.model;

import com.stan.analengine.model.types.DeviceType;
import com.stan.analengine.model.types.OsName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
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

  @Enumerated(value = EnumType.STRING)
  private OsName osName = OsName.UNIDENTIFIED;
  private String osVersion;

  @Enumerated(value = EnumType.STRING)
  private DeviceType deviceType;
  private String deviceModel;
  private String deviceVendor;
  private String deviceScreenResolution;
  private String deviceScreenSize;
  private String deviceScreenOrientation;
  private String deviceDescription;
  @CreationTimestamp
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  @CreationTimestamp
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private ZonedDateTime createdAt;
  @UpdateTimestamp
  @Column(nullable = false)
  private Date updated;
  private Date deleted;
  @Transient
  private Object loadVersion;

}
