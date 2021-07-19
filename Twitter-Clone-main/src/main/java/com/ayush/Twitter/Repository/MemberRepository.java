package com.ayush.Twitter.Repository;

import com.ayush.Twitter.Models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);

    Member findByEmail(String email);

    Member findById(int id);
}
