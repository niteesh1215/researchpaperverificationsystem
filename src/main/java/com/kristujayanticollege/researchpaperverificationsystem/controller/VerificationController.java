package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;
import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.VerificationDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.verification.PeerReviewedVerificationService;
import com.kristujayanticollege.researchpaperverificationsystem.service.verification.ScopusVerificationService;
import com.kristujayanticollege.researchpaperverificationsystem.service.verification.WebOfScienceVerificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class VerificationController {

	@Autowired
	private ResearchDetailsRepositoryService researchDetailsRepositoryService;

	@Autowired
	private VerificationDetailsRepositoryService verificationRepositoryService;

	@Autowired
	WebClient.Builder webClientBuilder;

	@Value("${scopus.api.key.value}")
	private String apiKey;

	@Autowired
	private ObjectMapper objectMapper;

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

			ScopusVerificationService scopusVerificationService = new ScopusVerificationService();
			scopusVerificationService.setWebClientBuilder(webClientBuilder);
			scopusVerificationService.setObjectMapper(objectMapper);

			PeerReviewedVerificationService peerReviewedVerificationService = new PeerReviewedVerificationService();

			WebOfScienceVerificationService webOfScienceVerificationService = new WebOfScienceVerificationService();

			if (researchDetailsRows != null) {
				for (int i = 0; i < researchDetailsRows.size(); i++) {

					try {
						if ((researchDetailsRows.get(i).getIndexing() != null))
							if (researchDetailsRows.get(i).getIndexing().equalsIgnoreCase("SCOPUS")) {

								scopusVerificationService.setApiKey(apiKey);
								scopusVerificationService.setResearchDetailsRow(researchDetailsRows.get(i));
								scopusVerificationService.verify();
								scopusVerificationService.saveStatus(verificationRepositoryService,
										researchDetailsRepositoryService);

							} else if (researchDetailsRows.get(i).getIndexing()
									.equalsIgnoreCase("Refereed / Peer Reviewed")) {

								peerReviewedVerificationService.setResearchDetailsRow(researchDetailsRows.get(i));
								peerReviewedVerificationService.verify();
								System.out.println(peerReviewedVerificationService.foundUrls);
								peerReviewedVerificationService.saveStatus(verificationRepositoryService,
										researchDetailsRepositoryService);
							} else if (researchDetailsRows.get(i).getIndexing().equalsIgnoreCase("Web of Science")) {
								webOfScienceVerificationService.setResearchDetailsRow(researchDetailsRows.get(i));
								webOfScienceVerificationService.verify();
								webOfScienceVerificationService.saveStatus(verificationRepositoryService,
										researchDetailsRepositoryService);
							}

					} catch (Exception e) {
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>" + researchDetailsRows.get(i).getId());
					}
				}

			}

			map.put("status", "success");
			map.put("message", "verification successful");
			return map;
		} catch (Exception e) {
			System.out.println(e);
			map.clear();
			map.put("status", "error");
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

			PeerReviewedVerificationService peerReviewedVerificationService = new PeerReviewedVerificationService();

			WebOfScienceVerificationService webOfScienceVerificationService = new WebOfScienceVerificationService();

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
					} else if (row.getIndexing().equalsIgnoreCase("Refereed / Peer Reviewed")) {

						peerReviewedVerificationService.setResearchDetailsRow(row);
						peerReviewedVerificationService.verify();
						System.out.println(peerReviewedVerificationService.foundUrls);
						peerReviewedVerificationService.saveStatus(verificationRepositoryService,
								researchDetailsRepositoryService);
					} else if (row.getIndexing().equalsIgnoreCase("Web of Science")) {
						webOfScienceVerificationService.setResearchDetailsRow(row);
						webOfScienceVerificationService.verify();
						webOfScienceVerificationService.saveStatus(verificationRepositoryService,
								researchDetailsRepositoryService);
					}

			}

			map.put("status", "success");
			map.put("message", "verification successful");
			return map;
		} catch (Exception e) {
			map.clear();
			map.put("status", "error");
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
