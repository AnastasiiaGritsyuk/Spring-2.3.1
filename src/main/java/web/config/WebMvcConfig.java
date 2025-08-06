package web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // В Spring 6.1.8 используйте:
        configurer
                .setPathMatcher(new CaseSensitivePathMatcher())
                .setUrlPathHelper(new CaseSensitiveUrlPathHelper());
    }

    private static class CaseSensitivePathMatcher extends AntPathMatcher {
        public CaseSensitivePathMatcher() {
            super.setCaseSensitive(true);
        }
    }

    private static class CaseSensitiveUrlPathHelper extends UrlPathHelper {
        public CaseSensitiveUrlPathHelper() {
            super.setAlwaysUseFullPath(true);
            super.setUrlDecode(false);
        }
    }
}