package com.kristujayanticollege.researchpaperverificationsystem.service.repository;

import java.util.Optional;

import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.repository.VerificationDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationDetailsRepositoryService {

    
    @Autowired
    private VerificationDetailsRepository verificationDetailsRepository;

    public VerificationDetails save(VerificationDetails verificationDetails) {
        return verificationDetailsRepository.save(verificationDetails);
    }

    public Optional<VerificationDetails> getVerificationDetailsById(Long id) {
        return verificationDetailsRepository.findById(id);
    }

}
