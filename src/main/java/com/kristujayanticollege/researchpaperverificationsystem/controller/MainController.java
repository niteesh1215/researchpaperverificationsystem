package com.kristujayanticollege.researchpaperverificationsystem.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kristujayanticollege.researchpaperverificationsystem.model.User;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.UploadRepositoryService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController implements ErrorController {

	@Autowired
	private ResearchDetailsRepositoryService researchDetailsRepositoryService;

	@Autowired
	private UploadRepositoryService uploadRepositoryService;

	@Autowired
	WebClient.Builder webClientBuilder;

	// @Autowired
	// UserController userController;

	@GetMapping(path = "/login")
	public ModelAndView loginView(HttpServletRequest request) {
		// User user = new User();
		// user.setActive(true);
		// user.setUserName("niteesh");
		// user.setPassword("niteesh");
		// user.setRoles("admin");

		// userController.addNewUser(user);

		try {
			String referrer = request.getHeader("Referer");
			if (referrer != null)
				request.getSession().setAttribute("url_prior_login", referrer);
		} catch (Exception e) {
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view/login");
		return modelAndView;
	}

	@GetMapping(path = "/")
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view/dashboard");
		return modelAndView;
	}

	@GetMapping(path = { "/upload" })
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

	@PostMapping(path = "/dashboard-details")
	public @ResponseBody Map<String, Object> dashboardDetails(@RequestBody Map<String, Object> filters) {
		Map<String, Object> map = new HashMap<String, Object>();
		// researchDetailsRepositoryService.getGroupByIndexingCount();

		try {

			if (filters == null)
				researchDetailsRepositoryService.setFilters(null);
			else if (filters.isEmpty())
				researchDetailsRepositoryService.setFilters(null);
			else
				researchDetailsRepositoryService.setFilters(filters);

			map.put("indexing_count_list", researchDetailsRepositoryService.getGroupByIndexingCount());
			map.put("department_count_list", researchDetailsRepositoryService.getGroupByDepartmentCount());
			map.put("verification_status_count_list",
					researchDetailsRepositoryService.getGroupByVerificationStatusCount());
			map.put("uploads_count", uploadRepositoryService.getTotalUploads());
			map.put("total_entries", researchDetailsRepositoryService.getCount());
			map.put("year_wise_count", researchDetailsRepositoryService.getGroupByYearWiseCount());
			map.put("indexing_wise_verification_status_count",
					researchDetailsRepositoryService.getGroupByVerificationStatusAndIndexingCount());
			map.put("year", researchDetailsRepositoryService.getDistinctYears());
			map.put("status", "success");

		} catch (Exception e) {

			System.out.println(e);

			map.clear();
			map.put("status", "error");
			map.put("message", "An unknown error occurred");
		}

		// resetting the filters for next request
		researchDetailsRepositoryService.setFilters(null);

		return map;
	}

	@RequestMapping(value = "/error")
	public String error() {
		return "An error occurred";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
