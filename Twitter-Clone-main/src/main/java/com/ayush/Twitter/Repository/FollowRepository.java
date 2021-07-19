package com.ayush.Twitter.Repository;

import com.ayush.Twitter.Models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByFollowingIdAndUserId(int follow_id, int user_id);

    void deleteByFollowingId(int id);

    List<Follow> findAllByUserId(int id);
}
