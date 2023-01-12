package org.beFit.v1.repositories.entities;

public class ImageEntity {
	private Integer id;
	private String title;
	private String url;
	private String publicId;

	public ImageEntity(Integer id, String title, String url, String publicId) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.publicId = publicId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
}
