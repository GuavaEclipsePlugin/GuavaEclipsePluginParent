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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
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

    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple", 9);
    executePluginMethod(cutEditor, "Generate hashCode() and equals()");

    String editorText = cutEditor.getText();
    String expectedText = readFile("equalsHashCodeResults/Expected_SuperEqualsHashCode.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForExtendedClass() throws Exception {

    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("ExtendedSimpleClass");
    bot.menu("New").menu("Class").click();
    bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
    bot.textWithLabel("Na&me:").setText("ExtendedSimpleClass");
    bot.textWithLabel("&Superclass:").setText("net.sf.guavaeclipse.test.SampleSimple");
    bot.button("Finish").click();
    sleep();

    SWTBotEclipseEditor cutEditor = setClassContent("ExtendedSimpleClass", 7);
    executePluginMethod(cutEditor, "Generate hashCode() and equals()");

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsHashCodeForExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClass() throws Exception {

    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("InterfaceSample");
    bot.menu("New").menu("Interface").click();
    bot.textWithLabel("Na&me:").setText("InterfaceSample");
    bot.button("Finish").click();
    sleep();
    SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
    cutEditor.setText(readFile("input/Input_InterfaceSample.txt"));
    cutEditor.save();

    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("SampleImplementsInterface");
    bot.menu("New").menu("Class").click();
    bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
    bot.textWithLabel("Na&me:").setText("SampleImplementsInterface");
    bot.button("Add...").click();
    bot.text().setText("net.sf.guavaeclipse.test.InterfaceSample");
    sleep();
    bot.button("OK").click();
    // sleep();
    bot.button("Finish").click();
    sleep();

    cutEditor = setClassContent("SampleImplementsInterface", 11);
    executePluginMethod(cutEditor, "Generate hashCode() and equals()");

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_SuperEqualsHashCodeForInterfaceClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClassAndExtendedClass() throws Exception {
    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("SampleExtendedAndInterface");
    bot.menu("New").menu("Class").click();
    bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
    bot.textWithLabel("Na&me:").setText("SampleExtendedAndInterface");
    bot.textWithLabel("&Superclass:").setText("net.sf.guavaeclipse.test.SampleSimple");
    bot.button("Add...").click();
    bot.text().setText("net.sf.guavaeclipse.test.InterfaceSample");
    sleep();
    bot.button("OK").click();
    // sleep();
    bot.button("Finish").click();
    sleep();

    SWTBotEclipseEditor cutEditor = setClassContent("SampleExtendedAndInterface", 11);
    executePluginMethod(cutEditor, "Generate hashCode() and equals()");

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceAndExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

}
