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

import java.io.IOException;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.infomas.annotation.AnnotationDetector;

/**
 * HK2RuntimeInitializer
 * 
 * HK2 currently doesn't provide any mechanism to configure itself via annotations at runtime - instead, 
 * it is done via a tool that runs at build time which creates 'inhabitant' files.  But having those files
 * around is problematic at best, and makes debugging in Eclipse or other environments difficult, since it 
 * relies on having the maven-tool generated file.
 * 
 * This utility alleviates those issues by allowing HK2 to be configured at runtime.
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
public class HK2RuntimeInitializer
{
	static Logger log = LoggerFactory.getLogger(HK2RuntimeInitializer.class);
	
	/**
	 * Scan the requested packages on the classpath for HK2 'Service' and 'Contract' annotated classes.
	 * Load the metadata for those classes into the HK2 Service Locator.
	 * 
	 * This implementation should support all Annotations that are supported by HK2 - however - if you are using 
	 * HK2 older than 2.3.0 - note that it is impacted by this bug:  https://java.net/jira/browse/HK2-187
	 * 
	 * For an implementation that is not impacted by that bug, see {@link HK2RuntimeInitializerCustom}
	 * 
	 * @see org.glassfish.hk2.api.ServiceLocatorFactory#create(String)
	 * @see ServiceLocatorUtilities#createAndPopulateServiceLocator(String)
	 * 
	 * @param serviceLocatorName - The name of the ServiceLocator to find (or create if it doesn't yet exist)  
	 * @param readInhabitantFiles - Read and process inhabitant files before doing the classpath scan.  Annotated items
	 * found during the scan will override items found in the inhabitant files, if they collide.  
	 * @param packageNames -- The set of package names to scan recursively - for example - new String[]{"org.foo", "com.bar"}
	 * If not provided, the entire classpath is scanned 
	 * @return - The created ServiceLocator (but in practice, you can lookup this ServiceLocator by doing:
	 * <pre>
	 * {@code
	 * ServiceLocatorFactory.getInstance().create("SomeName");
	 * }
	 * </pre>
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ServiceLocator init(String serviceLocatorName, boolean readInhabitantFiles, String ... packageNames) throws IOException, ClassNotFoundException 
	{
		AnnotatedClasses ac = new AnnotatedClasses();
		
		@SuppressWarnings("unchecked")
		AnnotationDetector cf = new AnnotationDetector(new AnnotationReporter(ac, new Class[]{Service.class}));
		if (packageNames == null || packageNames.length == 0)
		{
			cf.detect();
		}
		else
		{
			cf.detect(packageNames);
		}
		
		ServiceLocator locator = null;
		
		if (readInhabitantFiles)
		{
			locator = ServiceLocatorUtilities.createAndPopulateServiceLocator(serviceLocatorName);
		}
		else
		{
			ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
			locator = factory.create(serviceLocatorName);
		}

		for (ActiveDescriptor<?> ad : ServiceLocatorUtilities.addClasses(locator, ac.getAnnotatedClasses()))
		{
			log.debug("Added " + ad.toString());
		}
		
		return locator;
	}
}
