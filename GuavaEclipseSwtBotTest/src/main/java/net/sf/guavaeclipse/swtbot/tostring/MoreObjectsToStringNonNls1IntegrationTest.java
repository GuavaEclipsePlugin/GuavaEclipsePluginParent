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
public class MoreObjectsToStringNonNls1IntegrationTest extends AbstractSwtBotIntegrationTest {


  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
  }

  @AfterClass
  public static void changePreferencesBack() throws Exception {
    selectNonNls1DoNothing();
  }
  
  @Test
  public void createToStringMethod_NonNls1Comment() throws IOException, URISyntaxException {
    selectNonNls1Comment();
    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(TO_STRING);

    String editorText = cutEditor.getText();
    String fileName = "toStringResults/MoreObjectsExpected_ToStringNonNls1Comment.txt";
    logEditorResults(fileName, MoreObjectsToStringNonNls1IntegrationTest.class, "createToStringMethod()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createToStringMethod_NonNls1SupressWarning() throws IOException, URISyntaxException {
    selectNonNls1SupressWarning();
    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(TO_STRING);

    String editorText = cutEditor.getText();
    String fileName = "toStringResults/MoreObjectsExpected_ToStringNonNls1SupressWarning.txt";
    logEditorResults(fileName, MoreObjectsToStringNonNls1IntegrationTest.class, "createToStringMethod()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
