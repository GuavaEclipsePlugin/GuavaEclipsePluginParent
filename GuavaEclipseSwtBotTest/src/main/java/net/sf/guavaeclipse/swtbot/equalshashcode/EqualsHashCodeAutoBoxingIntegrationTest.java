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
package net.sf.guavaeclipse.swtbot.equalshashcode;

import static net.sf.guavaeclipse.swtbot.MenuSelection.EQUALS_HASHCODE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
public class EqualsHashCodeAutoBoxingIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
    selectExplicitBoxing();
    deselectDontUseObjectsForPrimitives();
  }
  
  @AfterClass
  public static void changePreferencesAfterClass() throws Exception {
    selectSmartSuper();
    selectAutoboxing();
    selectDontUseObjectsForPrimitives();
  }

  @Test
  public void createEqualsHashCode_explicitBoxing() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForAutoBoxing(EQUALS_HASHCODE, "EXPLICIT_BOXING");

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_explicitBoxing.txt";
    logEditorResults(fileName, EqualsHashCodeAutoBoxingIntegrationTest.class, "createEqualsHashCode_explicitBoxing()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCode_autoBoxingSuppressWarning() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForAutoBoxing(EQUALS_HASHCODE, "AUTOBOXING_SUPRESS_WARNINGS");

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_SupressWarningsBoxing.txt";
    logEditorResults(fileName, EqualsHashCodeAutoBoxingIntegrationTest.class, "createEqualsHashCode_explicitBoxing()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
