package com.stan.analengine.repo;

import com.stan.analengine.model.ButtonClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ButtonClickRepo extends JpaRepository<ButtonClick, Long> {
}
