package web.config;

import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        servletContext.setInitParameter("org.apache.catalina.webresources.CACHE", "false");
        servletContext.setInitParameter("org.apache.catalina.resources.CACHE", "false");

        JarScanner jarScanner = new DisabledJarScanner();
        servletContext.setAttribute("org.apache.tomcat.JarScanner", jarScanner);
    }

    public static class DisabledJarScanner implements JarScanner {
        @Override
        public void scan(@NonNull JarScanType scanType, @NonNull ServletContext context, @NonNull JarScannerCallback callback) {
        }
        @Override
        public JarScanFilter getJarScanFilter() {
            return null;
        }
        @Override
        public void setJarScanFilter(JarScanFilter jarScanFilter) {}
    }

    @Override
    @NonNull
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    @NonNull
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    @NonNull
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return new Filter[]{
                encodingFilter,
                new HiddenHttpMethodFilter(),
        };
    }
}