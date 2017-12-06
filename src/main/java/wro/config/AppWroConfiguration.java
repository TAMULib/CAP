package wro.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;

import edu.tamu.cap.wro.manager.factory.ConfigurableWroManagerFactory;
import edu.tamu.weaver.wro.service.ThemeManagerService;
import ro.isdc.wro.config.jmx.ConfigConstants;
import ro.isdc.wro.http.ConfigurableWroFilter;
import ro.isdc.wro.http.handler.factory.SimpleRequestHandlerFactory;
import ro.isdc.wro.model.resource.processor.factory.ConfigurableProcessorsFactory;
import wro4j.http.handler.CustomRequestHandler;

@Configuration
public class AppWroConfiguration {
    private static final String[] OTHER_WRO_PROP = new String[] { ConfigurableProcessorsFactory.PARAM_PRE_PROCESSORS, ConfigurableProcessorsFactory.PARAM_POST_PROCESSORS };
    
    @Autowired
    @Lazy
    private ThemeManagerService themeManagerService;
    
    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @Bean
    public FilterRegistrationBean webResourceOptimizer(Environment env) {
        FilterRegistrationBean fr = new FilterRegistrationBean();
        ConfigurableWroFilter filter = new ConfigurableWroFilter();
        Properties props = buildWroProperties(env);
        filter.setProperties(props);
        filter.setWroManagerFactory(new ConfigurableWroManagerFactory(props, themeManagerService,resourcePatternResolver));
        filter.setRequestHandlerFactory(new SimpleRequestHandlerFactory().addHandler(new CustomRequestHandler()));
        filter.setProperties(props);
        fr.setFilter(filter);
        fr.addUrlPatterns("/wro/*");
        
        return fr;
    }
    
    private Properties buildWroProperties(Environment env) {
        Properties prop = new Properties();
        for (ConfigConstants c : ConfigConstants.values()) {
            addProperty(env, prop, c.name());
        }
        for (String name : OTHER_WRO_PROP) {
            addProperty(env, prop, name);
        }
        addProperty(env,prop,"uriLocators");
        return prop;
    }

    private void addProperty(Environment env, Properties to, String name) {
        String value = env.getProperty("wro." + name);
        if (value != null) {
            to.put(name, value);
        }
}
}
