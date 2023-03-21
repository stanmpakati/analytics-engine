package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.model.Device;
import com.stan.analengine.analytics.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/analytics")
@AllArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @GetMapping("/search")
  Set<Device> getVisitors() {
    DeviceQueryDto deviceQueryDto = new DeviceQueryDto();
    return this.analyticsService.getVisitors(deviceQueryDto);
  }
}
