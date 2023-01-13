package org.beFit.v1.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

	@Configuration
	public class CloudinaryConfig {
		@Value("${cloudinary.cloudName}")
		private String cloudName;
		@Value("${cloudinary.apiKey}")
		private String apiKey;
		@Value("${cloudinary.apiSecret}")
		private String apiSecret;

		public String getCloudName() {
			return cloudName;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getApiSecret() {
			return apiSecret;
		}
	}
