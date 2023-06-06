package com.project.webcode.repository;

import com.project.webcode.domain.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
}
