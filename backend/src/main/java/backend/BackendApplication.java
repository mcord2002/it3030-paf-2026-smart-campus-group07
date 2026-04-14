package backend;

import com.campus.hub.CampusHubApplication;
import org.springframework.boot.SpringApplication;

/**
 * Compatibility entrypoint for older IDE run configurations.
 * Prefer running {@link com.campus.hub.CampusHubApplication}.
 */
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusHubApplication.class, args);
	}
}

