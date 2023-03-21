package com.stan.analengine.repo;

import com.stan.analengine.model.LinkClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkClickRepo extends JpaRepository<LinkClick, Long> {
}
