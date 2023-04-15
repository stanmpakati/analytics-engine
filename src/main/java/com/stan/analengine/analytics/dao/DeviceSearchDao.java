package com.stan.analengine.analytics.dao;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.analytics.dto.PageViewsDto;
import com.stan.analengine.model.BrowserEvent;
import com.stan.analengine.model.Device;
import com.stan.analengine.model.PageEvent;
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

  public Set<Device> findAllByCriteriaQuery(DeviceQueryDto deviceQueryDto) {
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

//  public void getPageVisits(Date from, Date to) {
//    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//
//    CriteriaQuery<PageEvent> criteriaQuery = criteriaBuilder.createQuery(PageEvent.class);
//
//    List<Predicate> predicates = new ArrayList<>();
//
//    Root<PageEvent> root = criteriaQuery.from(PageEvent.class);
//
//    Predicate browserNamePredicate = criteriaBuilder
//        .like(root.get("pageName"), "%" + deviceQueryDto.getBrowserName() + "%");
//    predicates.add(browserNamePredicate);
//
//    Predicate activeEvents = criteriaBuilder
//        .between(root.get("created"), from, to);
//
//    criteriaQuery.where(activeEvents);
//
//    TypedQuery<PageEvent> query = em.createQuery(criteriaQuery);
//
//  }

  public List<PageViewsDto> findPageVisitCounts() {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, PageEvent> pageEvent = browserEvent.join("pageEvents");

    query.multiselect(pageEvent.get("pageName"), cb.count(pageEvent), cb.countDistinct(browserEvent.get("device")));
    query.groupBy(pageEvent.get("pageName"));

    List<Object[]> results = em.createQuery(query).getResultList();
    return results.stream().map(o -> new PageViewsDto((String) o[0], (Long) o[1], (Long) o[2])).collect(Collectors.toList());
  }

  public List<Object[]> findPageVisitCounts(Date startDate, Date endDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
    Root<BrowserEvent> browserEvent = query.from(BrowserEvent.class);
    Join<BrowserEvent, PageEvent> pageEvent = browserEvent.join("pageEvents");
    query.multiselect(pageEvent.get("pageName"), cb.count(browserEvent), cb.countDistinct(browserEvent.get("device")));
    query.where(cb.between(browserEvent.get("created"), startDate, endDate));
    query.groupBy(pageEvent.get("pageName"));
    return em.createQuery(query).getResultList();
  }
}
