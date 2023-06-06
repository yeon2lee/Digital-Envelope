package com.project.webcode.repository;

import com.project.webcode.domain.PublicKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicKeysRepository extends JpaRepository<PublicKeys, Long> {
}
