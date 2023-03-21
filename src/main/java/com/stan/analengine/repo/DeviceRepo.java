package com.stan.analengine.repo;

import com.stan.analengine.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepo extends JpaRepository<Device, UUID> {
  Optional<Device> findByDeviceId(UUID deviceId);
}
