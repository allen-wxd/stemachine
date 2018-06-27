package com.windows.library;

import java.util.Locale;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.windows.library.impl.FileOperatorimpl;
import com.windows.library.impl.Iniimpl;
import com.windows.library.impl.Packimpl;
import com.windows.library.impl.Registryimpl;
import com.windows.library.impl.Utilimpl;
import com.windows.library.resource.IconCache;
import com.windows.library.resource.Progressdlg;
import com.windows.library.resource.ZLWindowsLibrary;

public class Activator implements BundleActivator {

	private static BundleContext context;
    private ServiceRegistration<?> IUtil;
    private ServiceRegistration<?> IPacking;
    private ServiceRegistration<?> IFileOperator;
    private ServiceRegistration<?> IRegistry;
    private ServiceRegistration<?> IiniOperator;
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 *///IPacking  Packimpl
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		IUtil = context.registerService( 
		        "com.windows.library.service.IUtil", 
		        new Utilimpl(), 
		        null);
        
		IPacking = context.registerService( 
		        "com.windows.library.service.IPacking", 
		        new Packimpl(), 
		        null);
        
		IFileOperator = context.registerService( 
		        "com.windows.library.service.IFileOperator", 
		        new FileOperatorimpl(), 
		        null);
        
		IRegistry = context.registerService( 
		        "com.windows.library.service.IRegistry", 
		        new Registryimpl(), 
		        null);
		        
		IiniOperator = context.registerService( 
		        "com.windows.library.service.IiniOperator", 
		        new Iniimpl(), 
		        null);
		
		IconCache.Hinstance();
		Progressdlg.hideDialog();
		new ZLWindowsLibrary();
		Locale locale1 = new Locale("zh", "CN");
		Locale.setDefault(locale1);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		IUtil.unregister();
		IPacking.unregister();
		IFileOperator.unregister();
		IRegistry.unregister();
		IiniOperator.unregister();
		if(IconCache.Icon!=null)
			IconCache.Icon.freeResources();
		Activator.context = null;
	}
}
