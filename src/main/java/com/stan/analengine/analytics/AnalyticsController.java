package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.analytics.dto.PageViewsDto;
import com.stan.analengine.model.Device;
import com.stan.analengine.analytics.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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

  @GetMapping("/pageviews")
  List<PageViewsDto> getPageViews(
      @RequestParam("start_at") Date startAt,
      @RequestParam("end_at") Date endAt
      ) {
    if (startAt == null) startAt = new Date(1);
    if (endAt == null) endAt = new Date();

    return this.analyticsService.getPageViews(startAt, endAt);
  }

  @GetMapping("/gpt")
  Object chatGPTQuery() {
    return this.analyticsService.chatGPTQuery();
  }
}
