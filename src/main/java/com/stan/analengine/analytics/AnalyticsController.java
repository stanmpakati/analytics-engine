package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.*;
import com.stan.analengine.model.Device;
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

  @GetMapping("/health")
  public String getHealth() {
    return "Running";
  }

  @GetMapping("/visitors")
  public Object getVisitors(
      @RequestParam(value = "start_date", required = false) Date startDate,
      @RequestParam(value = "end_date", required = false) Date endDate,
      @RequestParam(value = "group_by", required = false) RangeGroup rangeGroup
  ) {
    // todo: make required
    if (startDate == null) startDate = new Date(1);
    if (endDate == null) endDate = new Date();

    return this.analyticsService.getVisitors(startDate, endDate, rangeGroup);
  }

  @GetMapping("/pageviews")
  public List<PageViewsDto> getPageViews(
      @RequestParam(value = "start_at", required = false) Date startDate,
      @RequestParam(value = "end_at", required = false) Date endDate
      ) {
    if (startDate == null) startDate = new Date(1);
    if (endDate == null) endDate = new Date();

    return this.analyticsService.getPageViews(startDate, endDate, RangeGroup.MONTHLY);
  }

  @GetMapping("referrer")
  public List<PropertyDto> findReferrers(
      @RequestParam(value = "start_date", required = false) Date startDate,
      @RequestParam(value = "end_date", required = false) Date endDate
  ) {
    return this.analyticsService.findReferrers(startDate, endDate);
  }

  @GetMapping("/device-type")
  public List<PropertyDto> findDeviceType(
      @RequestParam(value = "start_date", required = false) Date startDate,
      @RequestParam(value = "end_date", required = false) Date endDate
  ) {
    return this.analyticsService.findDeviceType(startDate, endDate);
  }

  @GetMapping("/os")
  public List<PropertyDto> findOS(
      @RequestParam(value = "start_date", required = false) Date startDate,
      @RequestParam(value = "end_date", required = false) Date endDate
  ) {
    return this.analyticsService.findOS(startDate, endDate);
  }
}
