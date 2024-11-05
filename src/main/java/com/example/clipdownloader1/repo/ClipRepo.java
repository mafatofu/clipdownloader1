package com.example.clipdownloader1.repo;

import com.example.clipdownloader1.entity.Clip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClipRepo extends JpaRepository<Clip, Long> {
    Optional<Clip> findByClipTitle(String clipTitle);
    /**클립명과 멤버id로 검색*/
    Optional<Clip> findByClipTitleAndMemberId(String clipTitle, Long memberId);
    Optional<Clip> findByOriginalUrl(String originalUrl);
    boolean existsByClipTitle(String clipTitle);
    boolean existsByOriginalUrl(String originalUrl);
}
