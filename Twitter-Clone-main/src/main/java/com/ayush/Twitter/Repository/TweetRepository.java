package com.ayush.Twitter.Repository;

import com.ayush.Twitter.Models.Image;
import com.ayush.Twitter.Models.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweets, Integer> {
    List<Tweets> findAllByAuthorIdInOrderByIdDesc(List<Integer> id);
}
