/*
 * Copyright 2014
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.guavaeclipse.swtbot.tostring;

import static net.sf.guavaeclipse.swtbot.MenuSelection.TO_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToStringSkipNullValuesIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
    selectSkipNullValues();
  }

  @AfterClass
  public static void changePreferencesBack() throws Exception {
    deselectSkipNullValues();
  }
  
  @Test
  public void createToStringMethod() throws IOException, URISyntaxException {
    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(TO_STRING);

    String editorText = cutEditor.getText();
    String fileName = "toStringResults/Expected_ToStringSkipNull.txt";
    logEditorResults(fileName, ToStringSkipNullValuesIntegrationTest.class, "createToStringMethod()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }


}
