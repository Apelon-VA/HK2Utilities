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

import javax.inject.Named;
import org.jvnet.hk2.annotations.Service;

/**
 * TestService
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */

@Service @Named
public class TestService implements TestInterface
{
	int localId = 1;
	static int staticId = 1;
	
	@Override
	public void reset()
	{
		localId = 1;
		staticId = 1;
	}
	
	@Override
	public int getInstanceId()
	{
		return localId++;
	}
	
	@Override
	public int getStaticId()
	{
		return staticId++;
	}
}
