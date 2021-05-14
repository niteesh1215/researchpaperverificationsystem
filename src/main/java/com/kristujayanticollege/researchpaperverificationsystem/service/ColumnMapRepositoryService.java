package com.kristujayanticollege.researchpaperverificationsystem.service;

import java.util.List;

import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.repository.ColumnMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnMapRepositoryService {
    @Autowired
    private ColumnMapRepository columnMapRepository;

    public List<ColumnMap> getAllMapping() {
        return columnMapRepository.findAll();
    }

    public void addNewMapping(ColumnMap columnMap) {
        columnMapRepository.save(columnMap);
    }

    public void deleteMappingById(Long id) {
        columnMapRepository.deleteById(id);
    }
}
