package com.kristujayanticollege.researchpaperverificationsystem.service;

import javax.transaction.Transactional;

import com.kristujayanticollege.researchpaperverificationsystem.model.Upload;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class TransactionalService {
    @Transactional
    public void addRsearchDetails(String json, UploadRepositoryService uploadRepositoryService,
            ResearchDetailsRepositoryService researchDetailsRepositoryService,
            ColumnMapRepositoryService columnMapRepositoryService) throws ParseException, java.text.ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject researchDetails = (JSONObject) obj;
        Upload upload = uploadRepositoryService.save((JSONObject) researchDetails.get("fileDetails"));
        researchDetailsRepositoryService.addReseachDetails((JSONArray) researchDetails.get("excelData"), upload.getId(),
                columnMapRepositoryService.getAllMapping());

    }
}
