package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.analytics.dao.DeviceSearchDao;
import com.stan.analengine.model.Device;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AnalyticsService {

  private final DeviceSearchDao deviceSearchDao;

  public Set<Device> getVisitors(DeviceQueryDto queryDto) {
    return this.deviceSearchDao.findAllByCriteriaQuery(queryDto);
//    return null;
  }

  public Set<Device> getRecentVisitors(DeviceQueryDto queryDto) {
    return this.deviceSearchDao.getRecentlyActive(queryDto.getFrom(), queryDto.getTo());
//    return null;
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
