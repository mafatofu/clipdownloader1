package com.example.clipdownloader1.repo;

import com.example.clipdownloader1.entity.Clip;
import com.example.clipdownloader1.entity.DownloadClipLog;
import com.example.clipdownloader1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DownloadClipLogRepo extends JpaRepository<DownloadClipLog, Long> {
    Optional<DownloadClipLog> findByClipAndMember(Clip clip, Member member);
}
