/*

 */

package com.windows.library.resource;
import java.util.*;

abstract public class ZLResource 
{
	public final String Name;
	
	public static ZLResource resource(String key) 
	{
		ZLTreeResource.buildTree();
		if (ZLTreeResource.ourRoot == null) 
		{
			return ZLMissingResource.Instance;
		}
		return ZLTreeResource.ourRoot.getResource(key);
	}

	protected ZLResource(String name) 
	{
		Name = name;
	}
	public static String getLanguage() {
		return Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry();
	}

	abstract public boolean hasValue();
	abstract public String getValue();
	abstract public ZLResource getResource(String key);
}
