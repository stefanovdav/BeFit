package org.beFit.v1.repositories;

import org.beFit.v1.api.models.ImageUploadPayloadDTO;
import org.beFit.v1.repositories.entities.ImageEntity;

public interface ImageRepository {
	ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO, String folderName);

	ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO);

	boolean delete(ImageEntity imageEntity);
}
