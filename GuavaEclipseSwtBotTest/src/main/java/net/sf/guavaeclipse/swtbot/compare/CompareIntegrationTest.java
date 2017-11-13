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
package net.sf.guavaeclipse.swtbot.compare;

import static net.sf.guavaeclipse.swtbot.MenuSelection.COMPARE_TO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompareIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectMultiCommentsForCompareTo();
  }

  @Test
  public void createCompareTo() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_CompareTo.txt";
    logEditorResults(fileName, CompareIntegrationTest.class, "createCompareTo()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }


  @Test
  public void createCompareToWithArrays() throws Exception {

    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_SampleSimpleArray.txt";
    logEditorResults(fileName, CompareIntegrationTest.class, "createCompareToWithArrays()",
        editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));

  }


  @Test
  public void createCompareToWithObjects() throws Exception {

    deleteClassIfExists("SampleBean");
    createClass("SampleBean");
    SWTBotEclipseEditor cutEditor = setClassContent("ComparableObjectsClass", 10);
    executePluginMethod(cutEditor, COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_ComparableObjectsClass.txt";
    logEditorResults(fileName, CompareIntegrationTest.class, "createCompareToWithObjects()",
        editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));

  }
  
  @Test
  public void createCompareToComplex() throws Exception {

    deleteClassIfExists("ClassWithComparable");
    createClass("ClassWithComparable");
    setClassContent("ClassWithComparable", 1);

    deleteClassIfExists("ClassWithoutComparable");
    createClass("ClassWithoutComparable");
    setClassContent("ClassWithoutComparable", 1);

    deleteClassIfExists("SampleEnum");
    createClass("SampleEnum");
    setClassContent("SampleEnum", 1);
    
    deleteClassIfExists("InterfaceWithComparable");
    createClass("InterfaceWithComparable");
    setClassContent("InterfaceWithComparable", 1);

    deleteClassIfExists("InterfaceWithoutComparable");
    createClass("InterfaceWithoutComparable");
    setClassContent("InterfaceWithoutComparable", 1);

    deleteClassIfExists("ClassExtendsSomeComparable");
    createClass("ClassExtendsSomeComparable");
    setClassContent("ClassExtendsSomeComparable", 1);

    deleteClassIfExists("ClassExtendSomeNonComparable");
    createClass("ClassExtendSomeNonComparable");
    setClassContent("ClassExtendSomeNonComparable", 1);

    deleteClassIfExists("AbstractClassWithComparable");
    createClass("AbstractClassWithComparable");
    setClassContent("AbstractClassWithComparable", 1);

    deleteClassIfExists("AbstractClassWithoutComparable");
    createClass("AbstractClassWithoutComparable");
    setClassContent("AbstractClassWithoutComparable", 1);

    deleteClassIfExists("ComplexCompareTest");
    createClass("ComplexCompareTest");
    SWTBotEclipseEditor cutEditor = setClassContent("ComplexCompareTest", 29);
    
    executePluginMethod(cutEditor, COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_ComplexCompareTest.txt";
    logEditorResults(fileName, CompareIntegrationTest.class, "createCompareToComplex()",
        editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }
}
