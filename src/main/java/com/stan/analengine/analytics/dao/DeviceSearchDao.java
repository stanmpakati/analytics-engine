package com.stan.analengine.analytics.dao;

import com.stan.analengine.analytics.dto.*;
import com.stan.analengine.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class DeviceSearchDao {

  private final EntityManager em;

  public Set<Device> findDevicesByCriteriaQuery(DeviceQueryDto deviceQueryDto) {
    // initialize builder
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

    // initialize query
    CriteriaQuery<Device> criteriaQuery = criteriaBuilder.createQuery(Device.class);

    // initialize predicates list
    List<Predicate> predicates = new ArrayList<>();

    // Select from Device
    Root<Device> root = criteriaQuery.from(Device.class);

    if (deviceQueryDto.getBrowserName() != null) {
      Predicate browserNamePredicate = criteriaBuilder
          .like(root.get("browserName"), "%" + deviceQueryDto.getBrowserName() + "%");
      predicates.add(browserNamePredicate);
    }

    log.info("date from: {}", deviceQueryDto.getFrom());
    log.info("date to: {}", deviceQueryDto.getTo());
    Predicate datePredicate = criteriaBuilder
        .between(root.get("created"), deviceQueryDto.getFrom(), deviceQueryDto.getTo());
    predicates.add(datePredicate);

    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

    TypedQuery<Device> query = em.createQuery(criteriaQuery);
    return query.getResultStream().collect(Collectors.toSet());
  }
//
//  public Object findVisitors(Date startDate, Date endDate, RangeGroup rangeGroup) {
//    CriteriaBuilder cb = em.getCriteriaBuilder();
//
//    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
//
//    Root<BrowserEvent> browserEventRoot = query.from(BrowserEvent.class);
//    Join<BrowserEvent, Device> deviceJoin = browserEventRoot.join("device");
//
//    // group visitors into monthly intervals
//    Expression<Integer> month = cb.function("month", Integer.class, browserEventRoot.get("createdAt"));
//    Predicate predicate = cb.between(month, 1, 12);
//
//    query.where(predicate);
//    List<Object[]> results = em.createQuery(query).getResultList();
//    return results;
//  }
public List<Object[]> findVisitors(Date startDate, Date endDate, RangeGroup rangeGroup) {
  CriteriaBuilder cb = em.getCriteriaBuilder();

  CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

  Root<BrowserEvent> browserEventRoot = query.from(BrowserEvent.class);

  // group visitors into monthly intervals
  Expression<Integer> month = cb.function("month", Integer.class, browserEventRoot.get("createdAt"));
  Expression<Long> count = cb.count(browserEventRoot);
  Subquery<Long> subquery = query.subquery(Long.class);
  subquery.select(count).where(cb.between(month, 1, 12));
  Predicate predicate = cb.in(browserEventRoot.get("month"));

  query.where(predicate);
  query.groupBy(month);
  List<Object[]> results = em.createQuery(query).getResultList();
  return results;
}

  public Set<Device> getRecentlyActive(Date from, Date to) {
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

    // initialize query
    CriteriaQuery<BrowserEvent> criteriaQuery = criteriaBuilder.createQuery(BrowserEvent.class);

    Root<BrowserEvent> root = criteriaQuery.from(BrowserEvent.class);

    Predicate activeEvents = criteriaBuilder
        .between(root.get("created"), from, to);

    criteriaQuery.where(activeEvents);

    TypedQuery<BrowserEvent> query = em.createQuery(criteriaQuery);

    return query.getResultStream().map(BrowserEvent::getDevice).collect(Collectors.toSet());
  }

    public Object findAnalyticsOverview(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, PageEvent> pageEvent = browserEvent.join("pageEvents");

    // Todo factor for one date but no other one
    if (startDate != null && endDate != null) {
      query.where(cb.between(browserEvent.get("created"), startDate, endDate));
    }

    // select number of browser events, browser events and average active time on browser
    query.multiselect(
        cb.countDistinct(browserEvent),
        cb.countDistinct(browserEvent.get("device")),
        cb.count(browserEvent),
        cb.avg(pageEvent.get("activeTime")));

    List<Object[]> results = em.createQuery(query).getResultList();
//    return results;
    return results.stream().map(o -> new AnalyticsOverviewDto((Long) o[0], (Long) o[1], (Long) o[2], (Double) o[3]));
  }

  public List<PageViewsDto> findPageVisitCounts(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, PageEvent> pageEvent = browserEvent.join("pageEvents");

    // Todo factor for one date but no other one
    if (startDate != null && endDate != null) {
      query.where(cb.between(browserEvent.get("created"), startDate, endDate));
    }

    query.multiselect(pageEvent.get("pageName"), cb.count(pageEvent), cb.countDistinct(browserEvent.get("device")));

    query.groupBy(pageEvent.get("pageName"));

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream().map(o -> new PageViewsDto((String) o[0], (Long) o[1], (Long) o[2])).collect(Collectors.toList());
  }

  public List<PropertyDto> findReferrers(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, Device> device = browserEvent.join("device");

    query.multiselect(device.get("referrer"), cb.count(browserEvent));
    // Todo filter date
    if (startDate != null && endDate != null) {
      query.where(cb.between(browserEvent.get("created"), startDate, endDate));
    }
    query.groupBy(device.get("referrer"));

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream().map(o -> new PropertyDto((String) o[0], (Long) o[1])).collect(Collectors.toList());
  }

  public Object getLinkClicks(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<PageEvent> pageEvent = query.from(PageEvent.class);
    Join<PageEvent, LinkClick> linkClickJoin = pageEvent.join("linkClicks");

    if (startDate != null && endDate != null) {
      query.where(cb.between(pageEvent.get("created"), startDate, endDate));
    }

    query.multiselect(
        pageEvent.get("pageName"),
//        cb.countDistinct(pageEvent),
        cb.countDistinct(linkClickJoin),
        linkClickJoin.get("linkName"),
        linkClickJoin.get("linkDestination")
    );

    query.groupBy(
        pageEvent.get("pageName"),
        linkClickJoin.get("linkName"),
        linkClickJoin.get("linkDestination")
    );

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream().map(o -> new LinkClicksDto((String) o[0], Math.toIntExact((Long) o[1]), (String) o[2], (String) o[3]))
        .collect(Collectors.toList());
  }

//  public Object getButtonClicks() {
//    CriteriaBuilder cb = em.getCriteriaBuilder();
//
//    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
//
//    Root<PageEvent> pageEvent = query.from(PageEvent.class);
//
////    Join<PageEvent, LinkClick> linkClickJoin = pageEvent.join("linkClicks");
//    Join<PageEvent, ButtonClick> buttonClickJoin = pageEvent.join("buttonClicks");
//
//    query.multiselect(
//        pageEvent.get("pageName"),
////        cb.countDistinct(pageEvent),
////        cb.countDistinct(linkClickJoin),
////        linkClickJoin.get("linkName")
//        cb.countDistinct(buttonClickJoin),
//        buttonClickJoin.get("buttonName")
//    );
//
//    query.groupBy(
//        pageEvent.get("pageName"),
////        linkClickJoin.get("linkName")
//        buttonClickJoin.get("buttonName")
//    );
//
//    List<Object[]> results = em.createQuery(query).getResultList();
//    return results;
//  }


  public Object findButtonClicks(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<PageEvent> pageEvent = query.from(PageEvent.class);
    Join<PageEvent, ButtonClick> buttonClickJoin = pageEvent.join("buttonClicks");

    // Todo factor for one date but no other one
    if (startDate != null && endDate != null) {
      query.where(cb.between(pageEvent.get("created"), startDate, endDate));
    }

    query.multiselect(
        pageEvent.get("pageName"),
//        cb.count(pageEvent),
        cb.count(buttonClickJoin),
        buttonClickJoin.get("buttonName")
//        cb.count(buttonClickJoin)
        );

    query.groupBy(pageEvent.get("pageName"), buttonClickJoin.get("buttonName"));

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream()
        .map(o -> new ButtonClicksDto((String) o[0], Math.toIntExact((Long) o[1]),  (String) o[2]))
        .collect(Collectors.toList());
//    return results;
  }

  public List<PropertyDto> findDeviceProperty(Date startDate, Date endDate, String property) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, Device> device = browserEvent.join("device");

    query.multiselect(device.get(property), cb.countDistinct(device));

    // Todo filter date
    if (startDate != null && endDate != null) {
      query.where(cb.between(browserEvent.get("created"), startDate, endDate));
    }

    query.groupBy(device.get(property));

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream().map(o -> new PropertyDto(o[0], (Long) o[1])).collect(Collectors.toList());
  }

}
