package org.beFit.v1.core.imageservice;

import com.cloudinary.Cloudinary;
import org.beFit.v1.dto.ImageUploadPayloadDTO;
import org.beFit.v1.core.Mappers;
import org.beFit.v1.core.models.Image;
import org.beFit.v1.repositories.ImageRepository;
import org.beFit.v1.repositories.entities.ImageEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class CloudinaryService implements ImageService {
	private static final String TEMP_FILE = "temp-file";
	private static final String URL = "url";
	private static final String PUBLIC_ID = "public_id";

	private final Cloudinary cloudinary;
	private final ImageRepository imageRepository;

	public CloudinaryService(Cloudinary cloudinary, ImageRepository imageRepository) {
		this.cloudinary = cloudinary;
		this.imageRepository = imageRepository;
	}

	@Override
	public Image upload(ImageUploadPayloadDTO imageUploadDTO, String folderName) {

		File tempFile = null;
		try {
			tempFile = File.createTempFile(TEMP_FILE, imageUploadDTO.getMultipartFile().getOriginalFilename());
			imageUploadDTO.getMultipartFile().transferTo(tempFile);
			Map<String, String> options = null;
			if (folderName != null){
				options = Map.of(
						"folder", folderName
				);
			}

			@SuppressWarnings("unchecked")
			Map<String, String> uploadResult = cloudinary.
					uploader().
					upload(tempFile, options);

			String url = uploadResult.getOrDefault(URL,
					"https://i.pinimg.com/originals/c5/21/64/c52164749f7460c1ededf8992cd9a6ec.jpg");
			String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

			ImageEntity imageEntity = imageRepository.create(imageUploadDTO.getTitle(), url, publicId);

			return Mappers.fromImageEntity(imageEntity);
			//map image entity to image and return image
		} catch (IOException e) {
			throw new RuntimeException(
					String.format("Cloudinary error: %s", e.getMessage()));
		} finally {
			if (tempFile != null) {
				cleanUp(tempFile.toPath());
			}
		}
	}

	@Override
	public Image getImage(Integer id) {
		return Mappers.fromImageEntity(imageRepository.get(id));
	}

	@Override
	public boolean delete(Integer id) {
		ImageEntity imageEntity = imageRepository.get(id);
		if (imageEntity != null) {
			try {
				//delete in cloudinary
				this.cloudinary.uploader().destroy(imageEntity.getPublicId(), Map.of());
				//delete ind database
				imageRepository.delete(imageEntity.getId());
			} catch (IOException e) {
				throw new RuntimeException(String.format("Cloudinary error: %s", e.getMessage()));
			}
		}
		return false;
	}

	private void cleanUp(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new RuntimeException(
					String.format("Cloudinary error: %s", e.getMessage()));
		}
	}
}

