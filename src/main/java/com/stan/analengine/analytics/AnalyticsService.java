package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.*;
import com.stan.analengine.analytics.dao.DeviceSearchDao;
import com.stan.analengine.model.Device;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AnalyticsService {

  private final DeviceSearchDao deviceSearchDao;

  public Object getVisitors(Date startDate, Date endDate, RangeGroup rangeGroup) {
    return this.deviceSearchDao.findVisitors(startDate, endDate, rangeGroup);
//    return null;
  }

  public Set<Device> getRecentVisitors(DeviceQueryDto queryDto) {
    return this.deviceSearchDao.getRecentlyActive(queryDto.getFrom(), queryDto.getTo());
//    return null;
  }

  public List<PageViewsDto> getPageViews(Date startAt, Date endAt) {

    return this.deviceSearchDao.findPageVisitCounts(null, null);
  }

//  public Object chatGPTQuery(Date startDate, Date endDate, RangeGroup rangeGroup) {
//    return this.deviceSearchDao.findPageVisitCounts(startDate, endDate);
//  }

  public List<PropertyDto> findReferrers(Date startDate, Date endDate) {
    return this.deviceSearchDao.findReferrers(startDate, endDate);
  }

  public List<PropertyDto> findDeviceType(Date startDate, Date endDate) {
    return this.deviceSearchDao.findDeviceProperty(startDate, endDate, "deviceType");
  }

  public List<PropertyDto> findOS(Date startDate, Date endDate) {
    return this.deviceSearchDao.findDeviceProperty(startDate, endDate, "osName");
  }

//  public Specification<Device> getSpecFromDatesAndExample(
//      LocalDateTime from, LocalDateTime to, Example<Device> example) {
//
//    return (Specification<Device>) (root, query, builder) -> {
//      final List<Predicate> predicates = new ArrayList<>();
//
//      if (from != null) {
//        predicates.add(builder.greaterThan(root.get("created"), from));
//      }
//      if (to != null) {
//        predicates.add(builder.lessThan(root.get("created"), to));
//      }
//      predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
//
//      return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//    };
}
