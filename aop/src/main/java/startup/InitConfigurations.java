package startup;



import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@Configuration
public class InitConfigurations implements WebApplicationInitializer {

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Skipped other initialisations

        servletContext.setInitParameter("spring.profiles.active", "mySql");
    }

}
