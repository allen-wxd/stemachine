package com.wistron.web.registry;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;

import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.DispatcherServlet;

public class DispatcherServletRegistry implements ApplicationContextAware, BundleContextAware {

	String urlPattern;
	
	String servletName;
	
	BundleContext bundleContext;

	ApplicationContext applicationContext;
	
	DispatcherServlet dispatcherServlet;
	
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
	
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	public void setDispatcherServlet(DispatcherServlet dispatcherServlet) {
		this.dispatcherServlet = dispatcherServlet;
	}
	
	public void init() {
		
		ServiceReference<?> serviceReference = bundleContext.getServiceReference(HttpService.class.getName());
        HttpService httpService = (HttpService) bundleContext.getService(serviceReference);
		HttpContext commonContext = httpService.createDefaultHttpContext();
		
	    
        Dictionary<String, String> initparams = new Hashtable<String, String>();
        initparams.put("load-on-startup", "1");
        initparams.put("servlet-name", servletName);
        try {
            httpService.registerServlet(urlPattern, dispatcherServlet, initparams, commonContext);
        }
        catch (ServletException e) {
            e.printStackTrace();
        } catch (NamespaceException e)
        {
            e.printStackTrace();
        }
	}
}
