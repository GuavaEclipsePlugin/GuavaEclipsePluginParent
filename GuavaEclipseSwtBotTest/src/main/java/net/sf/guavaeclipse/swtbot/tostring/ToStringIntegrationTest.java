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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToStringIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
  }

  @Test
  public void createToStringMethod() throws IOException, URISyntaxException {
    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(TO_STRING);

    String editorText = cutEditor.getText();
    String expectedText = readFile("toStringResults/Expected_ToString.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createtoStringForExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForExtendedClass(TO_STRING);

    String editorText = cutEditor.getText();
    String expectedText = readFile("toStringResults/Expected_ToStringForExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createtoStringForInterfaceClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForInterface(TO_STRING);

    String editorText = cutEditor.getText();
    String expectedText = readFile("toStringResults/Expected_ToStringForInterfaceClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createtoStringForInterfaceClassAndExtendedClass() throws Exception {
    SWTBotEclipseEditor cutEditor = executeTestForSuperClassAndInterface(TO_STRING);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("toStringResults/Expected_ToStringForInterfaceAndExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void replaceToString() throws Exception {

    SWTBotMenu menu = bot.menu("Navigate").click();
    menu.menu("Open Type...").click();
    bot.waitUntil(Conditions.shellIsActive("Open Type"));
    bot.text().setText("SampleSimple");
    bot.button("OK").click();
    sleep();

    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple_Overwrite_ToString", 9);
    cutEditor.pressShortcut(SWT.CTRL | SWT.SHIFT, '4');

    assertNotNull(bot.label("toString() method already present. Replace it?"));
    bot.button("Yes").click();
    sleep();
    processDialog(cutEditor, TO_STRING);

    String editorText = cutEditor.getText();
    String expectedText = readFile("toStringResults/Expected_ToString_Overwrite.txt");
    assertThat(editorText, is(expectedText));
  }


}
