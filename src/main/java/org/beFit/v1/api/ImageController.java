package org.beFit.v1.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.beFit.v1.core.imageservice.CloudinaryService;
import org.beFit.v1.core.models.Image;
import org.beFit.v1.dto.ImageUploadPayloadDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/images")
@SecurityRequirement(name = "bearerAuth")
public class ImageController {

	CloudinaryService cloudinaryService;

	public ImageController(CloudinaryService cloudinaryService) {
		this.cloudinaryService = cloudinaryService;
	}

	@PostMapping(value = "/{folderName}")
	public Image upload(@RequestBody ImageUploadPayloadDTO imageUploadDTO,
						@PathVariable("folderName") String folderName) {
		return cloudinaryService.upload(imageUploadDTO, folderName);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Integer id) {
		cloudinaryService.delete(id);
	}
}
