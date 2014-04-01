/*
 * Copyright 2004-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
*/

package com.eviware.soapui.impl.wsdl;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.model.workspace.WorkspaceFactory;

public class WorkspaceTestCase
{
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter( WorkspaceTestCase.class );
	}

	@Test
	public void testWorkspaceImpl() throws Exception
	{
		Workspace workspace = WorkspaceFactory.getInstance().openWorkspace(
				System.getProperty( "user.home", "." ) + File.separatorChar + SoapUI.DEFAULT_WORKSPACE_FILE, null );

		for( int c = 0; c < workspace.getProjectCount(); c++ )
		{
			assertNotNull( workspace.getProjectAt( c ).getName() );
		}
	}
}
