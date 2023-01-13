package org.beFit.v1.core.imageservice;

import org.beFit.v1.dto.ImageUploadPayloadDTO;
import org.beFit.v1.core.models.Image;

public interface ImageService {
	Image upload(ImageUploadPayloadDTO imageUploadDTO, String folderName);
	boolean delete(Integer id);

	Image getImage(Integer id);
}

