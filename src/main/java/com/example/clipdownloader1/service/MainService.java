package com.example.clipdownloader1.service;

import com.example.clipdownloader1.repo.MainRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {
    private final MainRepo mainRepo;
}
