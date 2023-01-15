package org.beFit.v1.api;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.beFit.v1.api.models.FitGroupInput;
import org.beFit.v1.core.FitGroupService;
import org.beFit.v1.core.models.FitGroup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/groups")
@SecurityRequirement(name = "bearerAuth")
public class FitGroupController {

	private FitGroupService fitGroupService;

	public FitGroupController(FitGroupService fitGroupService) {
		this.fitGroupService = fitGroupService;
	}

	@PostMapping
	public FitGroup createFitGroup(@RequestBody FitGroupInput fitGroupInput) {
		return fitGroupService.createFitGroup(fitGroupInput.name, fitGroupInput.stake,
				fitGroupInput.end, fitGroupInput.tax);
	}

	@GetMapping(value = "/{id}")
	public FitGroup getFitGroup(@PathVariable("id") Integer id) {
		return fitGroupService.getFitGroup(id);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("@securityValidation.isAccountAdmin(#authToken) == true")
	public void deleteFitGroup(@PathVariable("id") Integer id,
							   @RequestHeader("Authorization") String authToken) {
		fitGroupService.deleteFitGroup(id);
	}
}
