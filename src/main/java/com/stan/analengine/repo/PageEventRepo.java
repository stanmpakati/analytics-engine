package com.stan.analengine.repo;

import com.stan.analengine.model.PageEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageEventRepo extends JpaRepository<PageEvent, Long> {
//  public interface PageEventRepository extends JpaRepository<PageEvent, Long> {
//    @Query("SELECT p.pageName, COUNT(p) FROM PageEvent p GROUP BY p.pageName, p.device")
//    List<Object[]> findPageVisitsByDevice();
//  }
//
//  @Query("SELECT p.pageName, COUNT(b) as visitCount, COUNT(DISTINCT b.device) as uniqueDeviceCount FROM PageEvent p JOIN BrowserEvent b ON p.id = b.pageEvent.id GROUP BY p.pageName")
//  List<Object[]> findPageVisitCounts();
}
