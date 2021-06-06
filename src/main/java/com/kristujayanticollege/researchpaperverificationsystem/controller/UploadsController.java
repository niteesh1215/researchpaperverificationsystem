package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.HashMap;

import com.kristujayanticollege.researchpaperverificationsystem.model.Upload;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.UploadRepositoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadsController {

    @Autowired
	private UploadRepositoryService uploadRepositoryService;
    
    @GetMapping(path = "/get-uploads")
	public @ResponseBody Iterable<Upload> getUploads() {
		return uploadRepositoryService.getAllUploads();
	}

	@GetMapping("/delete-upload/{id}")
	public ResponseEntity<HashMap<String, String>> deleteUpload(@PathVariable Long id) {
		HashMap<String, String> map = new HashMap<>();
		try {
			uploadRepositoryService.deleteUploadById(id);
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
