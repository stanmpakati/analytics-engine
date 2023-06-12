package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.*;
import com.stan.analengine.model.Device;
import com.stan.analengine.user.User;
import com.stan.analengine.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/analytics")
@AllArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  private final UserRepository userRepository;


//  @PostMapping("/login")
//  public User login(@RequestBody User user) {
//    User foundUser = userRepository.findByUsername(user.getUsername());
//    if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
//      return foundUser;
//    } else {
//      throw new RuntimeException("Password or Username incorrect");
//    }
//  }

  @GetMapping("/all")
  public String hello() {
    return "All users";
  }

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

  @GetMapping("/")
  public Object getAnalyticsOverView(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
      ) {
    // convert startDate and endDate to Date type
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    if (startDate == null) startDate = new Date(1);
    if (endDate == null) endDate = new Date();

    return this.analyticsService.getAnalyticsOverview(startDate, endDate);
  }
  @GetMapping("/pageviews")
  public List<PageViewsDto> getPageViews(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.getPageViews(startDate, endDate, RangeGroup.MONTHLY);
  }

  @GetMapping("referrer")
  public List<PropertyDto> findReferrers(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.findReferrers(startDate, endDate);
  }

  @GetMapping("/device-type")
  public List<PropertyDto> findDeviceType(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.findDeviceType(startDate, endDate);
  }

  @GetMapping("/os")
  public List<PropertyDto> findOS(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.findOS(startDate, endDate);
  }

  @GetMapping("/browser")
  public List<PropertyDto> findBrowser(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.findBrowsers(startDate, endDate);
  }

  @GetMapping("/clicks")
  public List<PropertyDto> findClicks(
      @RequestParam(value = "startDate", required = false) String startDateIso,
      @RequestParam(value = "endDate", required = false) String endDateIso
  ) {
    Date startDate = Date.from( Instant.parse(startDateIso));
    Date endDate = Date.from(Instant.parse(endDateIso));

    return this.analyticsService.findClicks(startDate, endDate);
  }

}
