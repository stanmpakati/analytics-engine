package com.stan.analengine.analytics.dao;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.model.BrowserEvent;
import com.stan.analengine.model.Device;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
}
