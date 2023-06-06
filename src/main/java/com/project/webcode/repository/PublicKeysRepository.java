package com.project.webcode.repository;

import com.project.webcode.domain.Member;
import com.project.webcode.domain.PublicKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicKeysRepository extends JpaRepository<PublicKeys, Long> {
    @Query("select m from PublicKeys p left join fetch p.member m")
    List<Member> findMemberFetchJoin();

    @Query("select p.publicKey from PublicKeys p left join p.member m where m.name = :name")
    byte[] findPublicKey(@Param("name") String name);
}
