package com.wistron.web.registry;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ResourceRegistry implements ApplicationContextAware,
		BundleContextAware {

	Map resMapping;
	
	BundleContext bundleContext;

	ApplicationContext applicationContext;
	
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setResMapping(Map resMapping) {
		this.resMapping = resMapping;
	}
	
	public void init() {
		ServiceReference serviceReference = bundleContext.getServiceReference(HttpService.class.getName());
        HttpService httpService = (HttpService) bundleContext.getService(serviceReference);
		HttpContext commonContext = httpService.createDefaultHttpContext();
       
		try {
			Set keySet = resMapping.keySet();
			Iterator<String> it = keySet.iterator();
			while(it.hasNext()){
				String key = it.next();
				String value = (String) resMapping.get(key);
				httpService.registerResources(key, value, commonContext);
			}
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}
}
