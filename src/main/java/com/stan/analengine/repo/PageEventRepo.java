package com.stan.analengine.repo;

import com.stan.analengine.model.PageEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageEventRepo extends JpaRepository<PageEvent, Long> {
}
