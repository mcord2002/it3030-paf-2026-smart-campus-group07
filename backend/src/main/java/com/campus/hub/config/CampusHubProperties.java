package com.campus.hub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "campus-hub")
public class CampusHubProperties {

	/**
	 * Directory for uploaded ticket evidence files.
	 */
	private String uploadDir = "./uploads";

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
