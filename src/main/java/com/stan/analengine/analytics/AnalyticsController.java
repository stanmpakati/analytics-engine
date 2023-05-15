package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.analytics.dto.PageViewsDto;
import com.stan.analengine.analytics.dto.RangeGroup;
import com.stan.analengine.model.Device;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
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
  Object chatGPTQuery(
      @RequestParam(value = "start_date", required = false) ZonedDateTime startDate,
      @RequestParam(value = "end_date", required = false) ZonedDateTime endDate,
      @RequestParam(value = "group_by", required = false) RangeGroup rangeGroup
      ) {
//    Date startDate = new Date();
//    Instant startInstant = date.toInstant();
//    ZoneId zoneId = ZoneId.of("America/Los_Angeles");
//    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
//
//
//    if (startDate == null) startDate = new ZonedDateTime(1);
//    if (endDate == null) endDate = new ZonedDateTime();
    if (rangeGroup == null) rangeGroup = RangeGroup.HOURLY;

    return this.analyticsService.chatGPTQuery(startDate, endDate, rangeGroup);
  }

  @GetMapping("referrer")
  Object findReferrers(
      @RequestParam(value = "start_date", required = false) ZonedDateTime startDate,
      @RequestParam(value = "end_date", required = false) ZonedDateTime endDate
  ) {
    return this.analyticsService.findReferrers(startDate, endDate);
  }
}
