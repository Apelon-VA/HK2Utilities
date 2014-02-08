/**
 * Copyright Notice
 *
 * This is a work of the U.S. Government and is not subject to copyright 
 * protection in the United States. Foreign copyrights may apply.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.va.oia.HK2Utilities;

import java.util.HashSet;
import javax.inject.Singleton;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.PerThread;
import org.glassfish.hk2.api.Proxiable;
import org.glassfish.hk2.api.Unproxiable;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassInfo
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
public class ClassInfo
{
	private String className_;
	private HashSet<String> annotations_ = new HashSet<>();
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	protected ClassInfo(String className)
	{
		className_ = className;
		log.debug("Found annotated class {}", className);
	}
	
	protected void addAnnotation(String annotation)
	{
		annotations_.add(annotation);
		log.debug("Added annotation {} to {}", annotation, className_);
	}
	
	public String getName()
	{
		return className_;
	}
	
	public boolean hasAnnotation(String annotation)
	{
		return annotations_.contains(annotation);
	}
	
	public boolean isService()
	{
		return hasAnnotation(Service.class.getName());
	}
	
	public boolean isContract()
	{
		return hasAnnotation(Contract.class.getName());
	}
	
	public boolean isProxyable()
	{
		return hasAnnotation(Proxiable.class.getName());
	}
	
	public boolean isUnproxyable()
	{
		return hasAnnotation(Unproxiable.class.getName());
	}
	
	public String getScope()
	{
		if (hasAnnotation(Singleton.class.getName()))
		{
			return Singleton.class.getName();
		}
		else if (hasAnnotation(PerLookup.class.getName()))
		{
			return PerLookup.class.getName();
		}
		else if (hasAnnotation(PerThread.class.getName()))
		{
			return PerThread.class.getName();
		}
		return null;
	}
}
