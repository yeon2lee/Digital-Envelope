package com.project.webcode.repository;

import com.project.webcode.domain.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
    @Query("select e from Envelope e left join e.receiver m where m.id = :receiverId")
    public Envelope findByReceiver(@Param("receiverId") Long receiverId);
}
