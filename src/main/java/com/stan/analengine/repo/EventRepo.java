package com.stan.analengine.repo;

import com.stan.analengine.model.BrowserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepo extends JpaRepository<BrowserEvent, UUID>, QueryByExampleExecutor<BrowserEvent> {
  Optional<BrowserEvent> findByClientSessionId(UUID clientSessionId);

}
