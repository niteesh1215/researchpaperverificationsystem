package com.kristujayanticollege.researchpaperverificationsystem.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.kristujayanticollege.researchpaperverificationsystem.model.Upload;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class TransactionalService {
    @Transactional
    public void addRsearchDetails(Map<String, Object> researchDetailsRows,
            UploadRepositoryService uploadRepositoryService,
            ResearchDetailsRepositoryService researchDetailsRepositoryService,
            ColumnMapRepositoryService columnMapRepositoryService) throws ParseException, java.text.ParseException {
        @SuppressWarnings("unchecked")
        Upload upload = uploadRepositoryService.save((Map<String, Object>) researchDetailsRows.get("fileDetails"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> excelData = (List<Map<String, Object>>) researchDetailsRows.get("excelData");
        researchDetailsRepositoryService.addReseachDetails(excelData, upload.getId(),
                columnMapRepositoryService.getAllMapping());

    }
}
