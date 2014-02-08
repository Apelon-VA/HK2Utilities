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
package gov.va.oia.HK2Utilities.test.services;

import javax.inject.Inject;
import org.glassfish.hk2.api.PerLookup;
import org.jvnet.hk2.annotations.Service;

/**
 * TestService
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
@PerLookup
@Service
public class TestService3 implements TestInterface
{
	private static int staticId = 0;
	private int localId = 3;
	
	@Inject
	private TestService2 two;
	
	@Override
	public void reset()
	{
		localId = 3;
		staticId = 3;
	}
	
	@Override
	public int getInstanceId()
	{
		return localId++;
	}
	
	public int getTwoId()
	{
		return two.getInstanceId();
	}
	
	@Override
	public int getStaticId()
	{
		return staticId++;
	}
}
