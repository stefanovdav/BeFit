package org.beFit.v1.core.imageservice;

import org.beFit.v1.api.models.ImageUploadPayloadDTO;
import org.beFit.v1.core.models.Image;

public interface ImageService {
	Image upload(ImageUploadPayloadDTO imageUploadDTO, String folderName);
	Image upload(ImageUploadPayloadDTO imageUploadDTO);
	boolean delete(Image imageEntity);
}

