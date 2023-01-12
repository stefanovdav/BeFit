package org.beFit.v1.api.models;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadPayloadDTO {
	private MultipartFile multipartFile;
	private String title;

	public ImageUploadPayloadDTO(MultipartFile multipartFile, String title) {
		this.multipartFile = multipartFile;
		this.title = title;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
