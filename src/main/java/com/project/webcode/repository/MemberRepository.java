package com.project.webcode.repository;

import com.project.webcode.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByName(String name);
}
