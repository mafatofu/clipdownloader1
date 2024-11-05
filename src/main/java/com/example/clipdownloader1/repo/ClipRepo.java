package com.example.clipdownloader1.repo;

import com.example.clipdownloader1.entity.Clip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClipRepo extends JpaRepository<Clip, Long> {
    Optional<Clip> findByClipTitle(String clipTitle);
}
