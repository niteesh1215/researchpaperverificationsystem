package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.HashMap;

import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ColumnMapRepositoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class ColumnMappingController {
    
	@Autowired
	private ColumnMapRepositoryService columnMapRepositoryService;
    
    @GetMapping(path = "/columns-mapping")
	public @ResponseBody Iterable<ColumnMap> columnsMapping() {
		return columnMapRepositoryService.getAllMapping();
	}

	@PostMapping(path = "/add-new-column-mapping")
	public ResponseEntity<HashMap<String, String>> addNewMapping(@RequestBody ColumnMap columnMap) {
		HashMap<String, String> map = new HashMap<>();
		try {
			columnMapRepositoryService.addNewMapping(columnMap);
			map.put("status", "success");
			map.put("message", "Successfuly added");
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			map.clear();
			map.put("status", "error");
			if (e.toString().contains("UNIQUE")) {
				map.put("message", "column/mapped name exists");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
			} else {
				map.put("message", "An unknown error occurred");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
			}
		}
	}

	@GetMapping("/delete-column-mapping/{id}")
	public ResponseEntity<HashMap<String, String>> deleteColumnMapping(@PathVariable Long id) {
		HashMap<String, String> map = new HashMap<>();
		try {
			columnMapRepositoryService.deleteMappingById(id);
			map.put("status", "success");
			map.put("message", "Successfuly deleted");
			return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.clear();
			map.put("status", "error");
			map.put("message", "An unknown error occurred");
			return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.NOT_FOUND);
		}
	}
}
