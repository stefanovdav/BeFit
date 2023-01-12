package org.beFit.v1.repositories.mariadb;

import org.beFit.v1.api.models.ImageUploadPayloadDTO;
import org.beFit.v1.repositories.ImageRepository;
import org.beFit.v1.repositories.entities.ImageEntity;

public class MariaDbImageRepository implements ImageRepository {
	@Override
	public ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO, String folderName) {
		return null;
	}

	@Override
	public ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO) {
		return null;
	}

	@Override
	public boolean delete(ImageEntity imageEntity) {
		return false;
	}
}
