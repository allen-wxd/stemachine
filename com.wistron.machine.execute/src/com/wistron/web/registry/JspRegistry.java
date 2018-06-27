package com.wistron.web.registry;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;

import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class JspRegistry implements ApplicationContextAware,
		BundleContextAware {


	BundleContext bundleContext;

	ApplicationContext appContext;
	
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		this.appContext = appContext;
	}
	
	public void init() {
		
		ServiceReference serviceReference = bundleContext.getServiceReference(HttpService.class.getName());
        HttpService httpService = (HttpService) bundleContext.getService(serviceReference);
		HttpContext commonContext = httpService.createDefaultHttpContext();
       
		
		Dictionary initparams = new Hashtable<String, String>();
		
		JspServlet jspServlet = new JspServlet(bundleContext.getBundle(), "/WebContent/jsp", "/jsp");
		try {
			httpService.registerServlet("/jsp", jspServlet, initparams, commonContext);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}
}
