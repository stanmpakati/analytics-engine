package com.stan.analengine.capture;


import com.stan.analengine.capture.dto.ButtonClickDto;
import com.stan.analengine.capture.dto.DeviceDto;
import com.stan.analengine.capture.dto.LinkClickDto;
import com.stan.analengine.capture.dto.PageEventDto;
import com.stan.analengine.model.BrowserEvent;
import com.stan.analengine.model.Device;
import com.stan.analengine.model.PageEvent;
import com.stan.analengine.repo.*;
import com.stan.analengine.response.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CaptureServiceImpl {
  private final ButtonClickRepo buttonClickRepo;

  private final LinkClickRepo linkClickRepo;

  private final DeviceRepo captureRepo;

  private final EventRepo eventRepo;

  private final PageEventRepo pageEventRepo;

  public UUID initialLoad(DeviceDto browserData) {
    if (browserData.getDeviceId().isPresent()) {
      Optional<Device> device = captureRepo.findById(browserData.getDeviceId().get());
      if (device.isPresent()) {
        this.captureBrowserEvents(device.get(), browserData.getClientSessionId());
        return device.get().getDeviceId();
      } else {
        // Todo invalidate token
      }
    }

    Device device = mapBrowserDtoToEntity(browserData);
    Device savedData = this.captureRepo.save(device);
    log.info("saved: {}", savedData);
    log.info("return deviceId: {}", savedData.getDeviceId());

    this.captureBrowserEvents(savedData, browserData.getClientSessionId());
    return savedData.getDeviceId();
  }

  public List<Device> getDevices() {
    log.info("devices: ", this.captureRepo.findAll());
    return this.captureRepo.findAll();
  }

//  @Transient
  public BrowserEvent captureBrowserEvents(Device device, UUID clientSessionId) {
    log.info("received event: {}", clientSessionId);

    Optional<BrowserEvent> existingBrowserEvent = this.eventRepo.findByClientSessionId(clientSessionId);
    if (existingBrowserEvent.isPresent()) {
      return existingBrowserEvent.get();
    }

    BrowserEvent convertedEvent = new BrowserEvent(device, clientSessionId);

    if (convertedEvent.getDevice() == null) {
      log.info("device is null");
      throw new ResourceNotFoundException("Device not found");
    }

    Optional<BrowserEvent> eventOptional = this.getBrowserEventFromSessionId(clientSessionId);
    if (eventOptional.isPresent()) {
      BrowserEvent browserEvent = eventOptional.get();
//      browserEvent.getButtonClicks().addAll(convertedEvent.getButtonClicks());
//      browserEvent.getLinkClicks().addAll(convertedEvent.getLinkClicks());
//      this.eventRepo.save(browserEvent);
      convertedEvent.setId(browserEvent.getId());
      convertedEvent.setCreated(browserEvent.getCreated());
      convertedEvent.setUpdated(browserEvent.getUpdated());
    }

    BrowserEvent savedEvent = this.eventRepo.save(convertedEvent);
    log.info("saved event: {}", savedEvent);
    return savedEvent;
  }

  public void recordPageEvents(PageEventDto pageEventDto) {
    BrowserEvent browserEvent = this.getBrowserEventFromSessionId(pageEventDto.getClientSessionId())
        .orElseThrow(() -> new ResourceNotFoundException("Event Not Found"));

    PageEvent pageEvent = this.mapPageEventDtoToEntity(pageEventDto);

    PageEvent updatingPage = browserEvent.getPageEvents()
        .stream()
        .filter(pageEvent1 -> Objects.equals(pageEvent1.getPageName(), pageEventDto.getPageName()))
        .findFirst()
        .orElse(null);

    if (updatingPage != null) {
      updatingPage.setButtonClickCount(pageEvent.getButtonClickCount());
//      updatingPage.setButtonClicks(pageEvent.getButtonClicks());
      updatingPage.setLinkClickCount(pageEvent.getLinkClickCount());
      updatingPage.setLinkClicks(pageEvent.getLinkClicks());
//      updatingPage.setClickLocations(pageEvent.getClickLocations() );
      updatingPage.setActiveTime(pageEvent.getActiveTime());
      updatingPage.setEndTime(pageEvent.getEndTime());
      updatingPage.setIdleTime(pageEvent.getIdleTime());
      updatingPage.setMouseMovementCount(pageEvent.getMouseMovementCount());
      updatingPage.setScrollCount(pageEvent.getScrollCount());
//      updatingPage.setBrowserEvent(browserEvent);

      log.info("Im reaching line 115");

      pageEvent.getButtonClicks().stream()
          .forEach(updatingPage::addToButtonClicks);

      log.info("Im reaching line 118");

      pageEvent.getLinkClicks().stream()
          .forEach(updatingPage::addToLinkClicks);

      log.info("Im reaching line 121");

//      updatingPage.getClickLocations().addAll(pageEventDto.getClickLocations());
      pageEvent.getClickLocations().stream()
          .forEach(updatingPage::addToClickLocations);

      log.info("Im reaching line 120");

//      browserEvent.addToPageEvent(updatingPage);
      pageEventRepo.save(updatingPage);
    } else {
      pageEvent.setCreated(new Date());
      browserEvent.addToPageEvent(pageEvent);
      this.eventRepo.save(browserEvent);
    }

//    this.eventRepo.save(browserEvent);
  }

  public List<BrowserEvent> getDeviceEvents() {
    return this.eventRepo.findAll();
  }

  public Optional<BrowserEvent> getBrowserEventFromSessionId(UUID sessionId) {
    return this.eventRepo.findByClientSessionId(sessionId);
  }

//  private BrowserEvent mapEventDtoToEventEntity(BrowserEventDto dto) {
//    Device device = this.captureRepo.findByDeviceId(dto.getDeviceId()).orElse(null);

//    Set<ButtonClickDto> buttonClicks = dto.getButtonClicks().stream().map(buttonClickRepo::save).collect(Collectors.toSet());
//    List<LinkClickDto> linkClicks = dto.getLinkClicks().stream().map(linkClickRepo::save).toList();

//    Set<ButtonClick> buttonClicks = dto.getButtonClicks().stream()
//        .map(ButtonClickDto::toButtonClick)
//        .map(BrowserEvent::addToButtonClicks)
//        .collect(Collectors.toSet());
//
//    Set<LinkClick> linkClicks = dto.getLinkClicks().stream()
//        .map(LinkClickDto::toLinkClick)
//        .collect(Collectors.toSet());

//    BrowserEvent event = BrowserEvent.builder()
//        .clientSessionId(dto.getClientSessionId())
//        .device(device)
//        .pageEvents(new ArrayList<>())
//        .build();

//    dto.getPageEvents().forEach(pageEventDto -> {
//
//    });


//    return event;
//  }

  private PageEvent mapPageEventDtoToEntity(PageEventDto dto) {
    PageEvent pageEvent = PageEvent.builder()
        .pageName(dto.getPageName())
        .startTime(dto.getStartTime())
        .endTime(dto.getEndTime())
        .activeTime(dto.getActiveTime())
        .idleTime(dto.getIdleTime())
        .clickLocations(dto.getClickLocations())
        .buttonClicks(new HashSet<>())
        .buttonClickCount(dto.getButtonClickCount())
        .linkClicks(new HashSet<>())
        .linkClickCount(dto.getLinkClickCount())
        .scrollCount(dto.getScrollCount())
        .mouseMovementCount(dto.getMouseMovementCount())
        .build();

    dto.getButtonClicks().stream()
        .map(ButtonClickDto::toButtonClick)
        .forEach(pageEvent::addToButtonClicks);

    dto.getLinkClicks().stream()
        .map(LinkClickDto::toLinkClick)
        .forEach(pageEvent::addToLinkClicks);

    return pageEvent;
  }

  private Device mapBrowserDtoToEntity(DeviceDto dto) {
    // Todo: user generated id
    return Device.builder()
        .browserVersion(dto.getBrowserVersion())
        .browserName(dto.getBrowserName())
        .cookiesEnabled(dto.getCookiesEnabled())
        .deviceModel(dto.getDeviceModel())
        .deviceDescription(dto.getDeviceDescription())
        .deviceType(dto.getDeviceType())
        .deviceVendor(dto.getDeviceVendor())
        .doNotTrack(dto.getDoNotTrack())
        .isNewUser(dto.getIsNewUser())
        .language(dto.getLanguage())
        .languages(dto.getLanguages())
        .deviceScreenSize(dto.getDeviceScreenSize())
        .networkDownLink(dto.getNetworkDownLink())
        .networkType(dto.getNetworkType())
        .osName(dto.getOsName())
        .osVersion(dto.getOsVersion())
        .deviceScreenOrientation(dto.getDeviceScreenOrientation())
        .deviceScreenResolution(dto.getDeviceScreenResolution())
        .referrer(dto.getReferrer())
        .timezone(dto.getTimezone())
        .timeRegion(dto.getTimeRegion())
        .userAgent(dto.getUserAgent())
        .build();
  }

}
