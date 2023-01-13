package org.beFit.v1.repositories;

import org.beFit.v1.repositories.entities.ImageEntity;

public interface ImageRepository {
	ImageEntity create(String title, String url, String publicId);

	ImageEntity get(Integer id);

	void delete(Integer id);
}
