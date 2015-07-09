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
import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SuperEqualsHashCodeIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectUseAlwaysSuper();
  }

  @Test
  public void createEqualsHashCode() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_SuperEqualsHashCode.txt";
    logEditorResults(fileName, SuperEqualsHashCodeIntegrationTest.class, "createEqualsHashCode()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForExtendedClass(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeForExtendedClass.txt";
    logEditorResults(fileName, SuperEqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForExtendedClass()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_SuperEqualsHashCodeForInterfaceClass.txt";
    logEditorResults(fileName, SuperEqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForInterfaceClass()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClassAndExtendedClass() throws Exception {
    SWTBotEclipseEditor cutEditor = executeTestForSuperClassAndInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceAndExtendedClass.txt";
    logEditorResults(fileName, SuperEqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForInterfaceClassAndExtendedClass()", editorText);
    String expectedText =
        readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
