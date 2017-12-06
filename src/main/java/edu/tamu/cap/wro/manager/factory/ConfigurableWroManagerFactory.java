package edu.tamu.cap.wro.manager.factory;

import java.util.Properties;

import org.springframework.core.io.support.ResourcePatternResolver;

import edu.tamu.cap.wro.resource.locator.SassClassPathUriLocator;
import edu.tamu.weaver.wro.manager.factory.CustomConfigurableWroManagerFactory;
import edu.tamu.weaver.wro.service.ThemeManagerService;
import ro.isdc.wro.model.resource.locator.ClasspathUriLocator;
import ro.isdc.wro.model.resource.locator.ServletContextUriLocator;
import ro.isdc.wro.model.resource.locator.UrlUriLocator;
import ro.isdc.wro.model.resource.locator.factory.SimpleUriLocatorFactory;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;

public class ConfigurableWroManagerFactory extends CustomConfigurableWroManagerFactory {
    
	private ResourcePatternResolver resourcePatternResolver;
//    private ThemeManagerService themeManagerService;

	public ConfigurableWroManagerFactory(Properties props, ThemeManagerService themeManagerService, ResourcePatternResolver resourcePatternResolver) {
		super(props, themeManagerService);
		this.resourcePatternResolver = resourcePatternResolver;
//		this.themeManagerService = themeManagerService;
	}
	
//	@Override
//	protected void contributePostProcessors(Map<String, ResourcePostProcessor> map) {
//		map.put("vireoPostProcessor", new VireoPostProcessor(themeManagerService));
//	}
	
	protected UriLocatorFactory newUriLocatorFactory() {
		System.out.println("******** LOAD THE LOCATORS");
		return new SimpleUriLocatorFactory().addLocator(new SassClassPathUriLocator(resourcePatternResolver)).addLocator(new ServletContextUriLocator()).addLocator(new ClasspathUriLocator()).addLocator(new UrlUriLocator());
	}
	
}
