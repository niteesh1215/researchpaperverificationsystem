package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;
import com.kristujayanticollege.researchpaperverificationsystem.model.Upload;
import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.service.ColumnMapRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.ScopusVerificationService;
import com.kristujayanticollege.researchpaperverificationsystem.service.TransactionalService;
import com.kristujayanticollege.researchpaperverificationsystem.service.UploadRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.VerificationDetailsRepositoryService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

	@Autowired
	private ResearchDetailsRepositoryService researchDetailsRepositoryService;

	@Autowired
	private ColumnMapRepositoryService columnMapRepositoryService;

	@Autowired
	private UploadRepositoryService uploadRepositoryService;

	@Autowired
	private VerificationDetailsRepositoryService verificationRepositoryService;

	@Autowired
	WebClient.Builder webClientBuilder;

	@Value("${scopus.api.key.value}")
	private String apiKey;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping(path = "/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "view/upload";
	}

	@GetMapping(path = { "/upload", "/" })
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view/upload");
		return modelAndView;
	}

	@GetMapping(path = "/modify-column")
	public ModelAndView modifyColumnView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view/column-mapping");
		return modelAndView;
	}

	@GetMapping(path = "/upload-list")
	public ModelAndView uploadListView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view/file-upload-list");
		return modelAndView;
	}

	@PostMapping(path = "/save")
	public ResponseEntity<HashMap<String, String>> save(@RequestBody String json) {
		HashMap<String, String> map = new HashMap<>();
		try {
			TransactionalService ts = new TransactionalService();
			ts.addRsearchDetails(json, uploadRepositoryService, researchDetailsRepositoryService,
					columnMapRepositoryService);
			map.put("status", "success");
			map.put("message", "Successfuly added");
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			map.clear();
			map.put("error", "error");
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

	@PostMapping(path = "delete-rows")
	public @ResponseBody HashMap<String, String> deleteResearchDetailsRow(@RequestBody List<Long> ids) {
		HashMap<String, String> map = new HashMap<>();
		try {

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
			map.clear();
			map.put("error", "error");
			map.put("message", "An unknown error occurred");
			return map;
		}
	}

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
			map.put("message", "Successfuly deleted");
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			map.clear();
			map.put("error", "error");
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
			map.put("error", "error");
			map.put("message", "An unknown error occurred");
			return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.NOT_FOUND);
		}
	}

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
			map.put("error", "error");
			map.put("message", "An unknown error occurred");
			return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/verify-file/{id}")
	public @ResponseBody HashMap<String, String> verify(@PathVariable Long id) {

		System.out.println(id);
		HashMap<String, String> map = new HashMap<>();

		try {
			List<Map<String, Object>> researchDetailsList = researchDetailsRepositoryService
					.getResearchDetailsByGroupId(id);

			if (researchDetailsList.isEmpty()) {
				map.put("status", "not_found");
				map.put("message", "id not found");
				return map;
			}

			List<ResearchDetailsRow> researchDetailsRows = objectMapper.convertValue(researchDetailsList,
					new TypeReference<List<ResearchDetailsRow>>() {
					});

			ScopusVerificationService service = new ScopusVerificationService();
			service.setWebClientBuilder(webClientBuilder);
			service.setObjectMapper(objectMapper);

			if (researchDetailsRows != null) {
				for (int i = 0; i < researchDetailsRows.size(); i++) {

					if (researchDetailsRows.get(i).getIndexing() != null)
						if (researchDetailsRows.get(i).getIndexing().equalsIgnoreCase("SCOPUS")) {
							try {
								service.setApiKey(apiKey);
								service.setResearchDetailsRow(researchDetailsRows.get(i));
								service.verify();
								service.saveStatus(verificationRepositoryService, researchDetailsRepositoryService);
							} catch (Exception e) {
								break;
							}
						}
				}

			}

			map.put("status", "success");
			map.put("message", "verification successful");
			return map;
		} catch (Exception e) {
			System.out.println(e);
			map.clear();
			map.put("error", "error");
			map.put("message", "An unknown error occurred");
			return map;
		}

	}

	@PostMapping(path = "/verify-rows")
	public @ResponseBody HashMap<String, String> verifyRow(@RequestBody List<Long> ids) {

		HashMap<String, String> map = new HashMap<>();

		// TODO: add for different indexing
		try {

			if (ids == null || ids.isEmpty()) {
				map.put("status", "invalid_request");
				map.put("message", "empty list");
				return map;
			}

			ScopusVerificationService service = new ScopusVerificationService();
			service.setWebClientBuilder(webClientBuilder);
			service.setObjectMapper(objectMapper);

			for (int i = 0; i < ids.size(); i++) {
				Map<String, Object> researchDetails = researchDetailsRepositoryService
						.getResearchDetailsById(ids.get(i));

				if (researchDetails == null)
					continue;
				ResearchDetailsRow row = objectMapper.convertValue(researchDetails, ResearchDetailsRow.class);

				if (row.getIndexing() != null)
					if (row.getIndexing().equalsIgnoreCase("SCOPUS")) {
						service.setApiKey(apiKey);
						service.setResearchDetailsRow(row);
						service.verify();
						service.saveStatus(verificationRepositoryService, researchDetailsRepositoryService);
					} else
						Thread.sleep(5000);

			}

			map.put("status", "success");
			map.put("message", "verification successful");
			return map;
		} catch (Exception e) {
			map.clear();
			map.put("error", "error");
			map.put("message", "An unknown error occurred");
			return map;
		}

	}

	@GetMapping(path = "verification-details/{id}")
	public @ResponseBody VerificationDetails getVerificationDetailsById(@PathVariable Long id) {
		Optional<VerificationDetails> details = verificationRepositoryService.getVerificationDetailsById(id);
		if (details.isPresent())
			return details.get();
		else
			return new VerificationDetails();
	}

}
