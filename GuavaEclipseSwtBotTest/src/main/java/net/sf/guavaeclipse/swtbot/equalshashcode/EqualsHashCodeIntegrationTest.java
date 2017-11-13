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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EqualsHashCodeIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
  }

  @Test
  public void createEqualsHashCode() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "createEqualsHashCode()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForExtendedClass(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeForExtendedClass.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForExtendedClass()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceClass.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForInterfaceClass()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }


  @Test
  public void createEqualsHashCodeForInterfaceClassAndExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSuperClassAndInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceAndExtendedClass.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "createEqualsHashCodeForInterfaceClassAndExtendedClass()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void replaceEqualsAndHashCode() throws Exception {

    SWTBotMenu menu = bot.menu("Navigate").click();
    menu.menu("Open Type...").click();
    bot.waitUntil(Conditions.shellIsActive("Open Type"));
    bot.text().setText("SampleSimple");
    bot.button("OK").click();
    sleep();
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple_Overwrite_EqualsHashCode", 9);
    sleep(1000);
    cutEditor.pressShortcut(SWT.CTRL | SWT.SHIFT, '5');

    bot.waitUntil(Conditions.shellIsActive("Duplicate Methods"));

    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        SWTBotShell shell = bot.shell("Duplicate Methods");
        SWTBot currentBot = shell.bot();
        assertNotNull(currentBot.label("hashCode() and equals() already present. Replace both?"));
        currentBot.button("Yes").click();
        shell.close();
        bot.waitUntil(Conditions.shellCloses(shell));
      }
    });
    cutEditor.setFocus();
    processDialog(cutEditor, EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_Overwrite.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "replaceEqualsAndHashCode()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void replaceEqualsAndHashCodeAgain() throws Exception {

    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple_Overwrite_EqualsHashCode", 9);
    cutEditor.pressShortcut(SWT.CTRL | SWT.SHIFT, '5');
    // sleep(2000);
    // SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
    // contextMenu.setFocus();
    // contextMenu.menu(EQUALS_HASHCODE.getMenuString()).click();

    assertNotNull(bot.label("hashCode() and equals() already present. Replace both?"));
    bot.button("No").click();
    assertNotNull(bot.label("equals() already present. Replace it?"));
    bot.button("Yes").click();
    assertNotNull(bot.label("hashCode() already present. Replace it?"));
    bot.button("Yes").click();
    processDialog(cutEditor, EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_Overwrite.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "replaceEqualsAndHashCodeAgain()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void replaceOnlyEquals() throws Exception {

    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple_Overwrite_EqualsHashCode", 9);
    cutEditor.pressShortcut(SWT.CTRL | SWT.SHIFT, '5');

    assertNotNull(bot.label("hashCode() and equals() already present. Replace both?"));
    bot.button("No").click();
    assertNotNull(bot.label("equals() already present. Replace it?"));
    bot.button("Yes").click();
    assertNotNull(bot.label("hashCode() already present. Replace it?"));
    bot.button("No").click();
    processDialog(cutEditor, EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_OverwriteOnlyEquals.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "replaceOnlyEquals()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void replaceOnlyHashCode() throws Exception {

    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple_Overwrite_EqualsHashCode", 9);
    cutEditor.pressShortcut(SWT.CTRL | SWT.SHIFT, '5');

    assertNotNull(bot.label("hashCode() and equals() already present. Replace both?"));
    bot.button("No").click();
    assertNotNull(bot.label("equals() already present. Replace it?"));
    bot.button("No").click();
    assertNotNull(bot.label("hashCode() already present. Replace it?"));
    bot.button("Yes").click();
    processDialog(cutEditor, EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCode_OverwriteOnlyHashCode.txt";
    logEditorResults(fileName, EqualsHashCodeIntegrationTest.class, "replaceOnlyHashCode()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
