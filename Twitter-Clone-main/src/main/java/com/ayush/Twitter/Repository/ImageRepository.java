package com.ayush.Twitter.Repository;

import com.ayush.Twitter.Models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    long countByMember_Id(int id);

    Image findByMember_Id(int id);

    boolean existsByMember_Id(int id);
}
