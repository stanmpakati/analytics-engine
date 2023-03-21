package com.stan.analengine.capture;

import com.stan.analengine.capture.dto.DeviceDto;
import com.stan.analengine.capture.dto.PageEventDto;
import com.stan.analengine.model.Device;
import com.stan.analengine.model.BrowserEvent;
import com.stan.analengine.response.ResponseHandler;
import com.stan.analengine.response.ResponseTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/capture")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@AllArgsConstructor
public class CaptureController {

  private final CaptureServiceImpl captureService;

  @PostMapping("/hit")
  @CrossOrigin
  public ResponseTemplate<UUID> initialLoad(@RequestBody DeviceDto browserData) {
//    @CookieValue("deviceId") String deviceId,
//    log.info("Cookie Value: {}", deviceId);
    log.info("Browser data: {}", browserData);

    return ResponseHandler.generateOkResponse(this.captureService.initialLoad(browserData));
  }

//  @PostMapping("/event")
//  public BrowserEvent recordSession(@RequestBody BrowserEventDto browserEvent) {
//    log.info("Browser data: {}", browserEvent);
//    return this.captureService.captureBrowserEvents(browserEvent);
//  }

  @PostMapping("/page-event")
  public void capturePageEvents(@RequestBody PageEventDto pageEventDto) {
    log.info("page data: {}", pageEventDto);
    this.captureService.recordPageEvents(pageEventDto);
  }

  @GetMapping("/devices")
  public List<Device> getDevices() {
    return this.captureService.getDevices();
  }

  @GetMapping("/events")
  public List<BrowserEvent> getDeviceEvents() {
    return this.captureService.getDeviceEvents();
  }
}
