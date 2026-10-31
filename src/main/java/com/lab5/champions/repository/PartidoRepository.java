package com.lab5.champions.repository;
import com.lab5.champions.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PartidoRepository extends JpaRepository<Partido,Long> { boolean existsByExternalId(String externalId); }
