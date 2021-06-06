package com.kristujayanticollege.researchpaperverificationsystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kristujayanticollege.researchpaperverificationsystem.service.other.TransactionalService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ColumnMapRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.UploadRepositoryService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class ResearchDetailsController {

    @Autowired
    private ResearchDetailsRepositoryService researchDetailsRepositoryService;

    @Autowired
    private ColumnMapRepositoryService columnMapRepositoryService;

    @Autowired
    private UploadRepositoryService uploadRepositoryService;

    @PostMapping(path = "/save")
    public ResponseEntity<HashMap<String, String>> save(@RequestBody Map<String, Object> researchDetailsRows) {
        HashMap<String, String> map = new HashMap<>();
        try {
            TransactionalService ts = new TransactionalService();
            ts.addRsearchDetails(researchDetailsRows, uploadRepositoryService, researchDetailsRepositoryService,
                    columnMapRepositoryService);
            map.put("status", "success");
            map.put("message", "Successfuly added");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            map.clear();
            map.put("status", "error");
            if (e.toString().contains("UNIQUE")) {
                map.put("message", "file name exists");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            } else {
                map.put("message", "An unknown error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
        }
    }

    @GetMapping(path = "get-research-details/{groupId}")
    public @ResponseBody List<Map<String, Object>> getResearchDetailsByGroupId(@PathVariable Long groupId) {
        try {
            return researchDetailsRepositoryService.getResearchDetailsByGroupId(groupId);
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<Map<String, Object>>();
        }
    }

    @PostMapping(path = "/add-new-row")
    public @ResponseBody HashMap<String, String> addNewRow(@RequestBody Map<String, Object> newRow) {
        HashMap<String, String> map = new HashMap<>();
        try {
            researchDetailsRepositoryService.addNewResearchDetailsRow(newRow,
                    columnMapRepositoryService.getAllMapping());
            map.put("status", "success");
            map.put("message", "Successfuly deleted");
            return map;
        } catch (Exception e) {
            System.out.println(e);
            map.clear();
            map.put("status", "error");
            map.put("message", "An unknown error occurred");
            return map;
        }
    }

    @PostMapping(path = "delete-rows")
    public @ResponseBody HashMap<String, String> deleteResearchDetailsRow(@RequestBody List<Long> ids) {
        HashMap<String, String> map = new HashMap<>();
        try {
            System.out.println(ids);
            if (ids == null || ids.isEmpty()) {
                map.put("status", "invalid_request");
                map.put("message", "empty list");
                return map;
            }

            for (int i = 0; i < ids.size(); i++) {
                researchDetailsRepositoryService.deleteResearchDetailsById(ids.get(i));
            }

            map.put("status", "success");
            map.put("message", "Successfuly deleted");
            return map;
        } catch (Exception e) {
            System.out.println(e);
            map.clear();
            map.put("status", "error");
            map.put("message", "An unknown error occurred");
            return map;
        }
    }

    @PostMapping(path = "/update-row")
    public @ResponseBody HashMap<String, String> updateRow(@RequestBody Map<String, Object> editedRow) {
        HashMap<String, String> map = new HashMap<>();
        try {
            researchDetailsRepositoryService.updateResearchDetailsRow(editedRow,
                    columnMapRepositoryService.getAllMapping());
            map.put("status", "success");
            map.put("message", "Successfuly deleted");
            return map;
        } catch (Exception e) {
            System.out.println(e);
            map.clear();
            map.put("status", "error");
            map.put("message", "An unknown error occurred");
            return map;
        }
    }
}
