package com.kristujayanticollege.researchpaperverificationsystem.service.other;

import java.util.List;
import java.util.Map;

import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(initialDelay = 86400000, fixedDelay = 86400000)
	public void formatColumns() {

		try {
			ResearchDetailsRepositoryService researchDetailsRepositoryService = new ResearchDetailsRepositoryService();

			List<Map<String, Object>> unformattedRows = researchDetailsRepositoryService.getUnformattedRows();

			List<Map<String, Object>> formattedRows = new FormatString().format(unformattedRows);

			researchDetailsRepositoryService.saveFormattedRows(formattedRows);

			log.info("formatting task completed successfuly");
		} catch (Exception e) {
			log.info("formatting task failed :"+e);
		}
	}
}