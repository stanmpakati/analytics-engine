package com.stan.analengine.capture.dto;

import com.stan.analengine.model.types.DeviceType;
import com.stan.analengine.model.types.OsName;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
public class DeviceDto {
  private Optional<UUID> deviceId;

  private UUID clientSessionId;
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
  private OsName OSName;
  private String OSVersion;
  private DeviceType deviceType;
  private String deviceModel;
  private String deviceVendor;
  private String deviceScreenResolution;
  private String deviceScreenSize;
  private String deviceScreenOrientation;
  private String deviceDescription;
}
