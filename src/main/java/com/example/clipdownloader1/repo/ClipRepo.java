package com.example.clipdownloader1.repo;

import com.example.clipdownloader1.entity.Clip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClipRepo extends JpaRepository<Clip, Long> {
}
