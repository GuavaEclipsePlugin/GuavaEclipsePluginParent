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
package net.sf.guavaeclipse.swtbot;

import static org.hamcrest.CoreMatchers.is;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractSwtBotIntegrationTest {

  protected static SWTWorkbenchBot bot;

  @BeforeClass
  public static void beforeClass() throws Exception {
    bot = new SWTWorkbenchBot();
    closeWelcomeView();
  }

  @AfterClass
  public static void tearDown() {
    bot.sleep(2000);
  }

  public static void sleep() {
    sleep(1500);
  }

  public static void sleep(long sleep) {
    bot.sleep(sleep);
  }

  public static void closeWelcomeView() {
    try {
      SWTBotView viewByTitle = bot.viewByTitle("Welcome");
      viewByTitle.close();
      bot.waitUntil(Conditions.waitForShell(is(viewByTitle.bot().activeShell().widget)));
    } catch (WidgetNotFoundException e) {
      // do nothing because welcome view is already closed...
    }
  }

  protected static void selectUseAlwaysSuper() {
    SWTBotMenu menu = bot.menu("Window").click();
    menu.menu("Preferences").click();

    SWTBotShell shell = bot.shell("Preferences");
    shell.activate();
    sleep();
    bot.tree().getTreeItem("Guava Preference").select();
    sleep();

    SWTBotRadio radio = bot.radio("Use super class Methods (toString(), equals() and hashCode())");
    radio.setFocus();
    radio.click();
    sleep();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectNoSuper() {
    SWTBotMenu menu = bot.menu("Window").click();
    menu.menu("Preferences").click();

    SWTBotShell shell = bot.shell("Preferences");
    shell.activate();
    sleep();
    bot.tree().getTreeItem("Guava Preference").select();
    sleep();

    SWTBotRadio radio =
        bot.radio("Don't use super class Methods (toString(), equals() and hashCode())");
    radio.setFocus();
    radio.click();
    sleep();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectSmartSuper() {
    SWTBotMenu menu = bot.menu("Window").click();
    menu.menu("Preferences").click();

    SWTBotShell shell = bot.shell("Preferences");
    shell.activate();
    sleep();
    bot.tree().getTreeItem("Guava Preference").select();
    sleep();

    SWTBotRadio radio =
        bot.radio("Use super class Methods (Only if superclass is not \"java.lang.Object\")");
    radio.setFocus();
    radio.click();
    sleep();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  public void createJavaProjectIfNotExists(String projectName) {
    try {
      bot.menu("Window").menu("Open Perspective").menu("Java").click();
    } catch (WidgetNotFoundException e) {
      // do nothing java perspective is already open
    }
    try {
      bot.tree().getTreeItem(projectName).select();
    } catch (WidgetNotFoundException e) {
      try {
        bot.tree().setFocus();
        bot.menu("File").menu("New").menu("Java Project").click();
        bot.textWithLabel("&Project name:").setText(projectName);
        bot.button("Next >").click();
        try {
          bot.tabItem("&Libraries").activate();
          bot.button("Add Variable...").click();
          bot.button("Extend...").click();
          bot.tree().getTreeItem("plugins").expand()
              .getNode("com.google.guava_15.0.0.v201403281430.jar").select();
          bot.button("OK").click();
        } catch (WidgetNotFoundException e2) {
          // ignore
          bot.button("Cancel").click();
          bot.button("Cancel").click();
        }
        bot.button("Finish").click();
        sleep();
        bot.tree().getTreeItem(projectName).select();
      } catch (WidgetNotFoundException e1) {
        e1.printStackTrace();
      }
    }
  }

  public String readFile(String fileName) throws IOException, URISyntaxException {
    BufferedReader br =
        new BufferedReader(new InputStreamReader(this.getClass().getClassLoader()
            .getResourceAsStream(fileName)));
    try {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      return sb.toString();
    } finally {
      br.close();
    }
  }

  public void deleteClassIfExists(String className) {
    try {
      bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
      SWTBotTreeItem select =
          bot.tree().getTreeItem("SampleJavaProject").getNode("src")
              .getNode("net.sf.guavaeclipse.test").getNode(className + ".java").select();
      select.contextMenu("Delete").click();
      bot.waitUntil(Conditions.shellIsActive("Delete"));
      bot.button("OK").click();
      bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
      SWTBotTreeItem select2 =
          bot.tree().getTreeItem("SampleJavaProject").getNode("src")
              .getNode("net.sf.guavaeclipse.test").select();
      if (select2.isSelected()) {
        return;
      } else {
        select2.select();
        return;
      }
      // bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    } catch (WidgetNotFoundException e) {
      // do nothing java perspective is already open
    }
  }

  public void executePluginMethod(SWTBotEclipseEditor cutEditor, String menuName) {
    SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
    contextMenu.setFocus();
    contextMenu.menu(menuName).click();
    bot.button("Select All").click();
    bot.button("OK").click();
    sleep();
    cutEditor.save();
  }

  public SWTBotEclipseEditor setClassContent(String inputFileName, int lineToSetCursor)
      throws IOException, URISyntaxException {
    SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
    cutEditor.setText(readFile("input/Input_" + inputFileName + ".txt"));
    cutEditor.save();
    cutEditor.selectLine(lineToSetCursor);
    return cutEditor;
  }

  public void createClass(String className) {
    this.createClass("net.sf.guavaeclipse.test", className);
  }

  public void createClass(String packageName, String className) {
    this.createClass(packageName, className, null, null);
  }

  public void createClassWithSuperClass(String className, String superClass) {
    this.createClass("net.sf.guavaeclipse.test", className, superClass, null);
  }

  public void createClassWithInterface(String className, String interfaceName) {
    this.createClass("net.sf.guavaeclipse.test", className, null, interfaceName);
  }

  public void createClassWithSuperClassAndInterface(String className, String superClass,
      String interfaceName) {
    this.createClass("net.sf.guavaeclipse.test", className, superClass, interfaceName);
  }
  public void createClass(String packageName, String className, String superClass,
      String interfaceName) {
    bot.menu("New").menu("Class").click();
    bot.textWithLabel("Pac&kage:").setText(packageName);
    bot.textWithLabel("Na&me:").setText(className);
    if (superClass != null && !superClass.isEmpty()) {
      bot.textWithLabel("&Superclass:").setText(superClass);
    }
    if (interfaceName != null && !interfaceName.isEmpty()) {
      bot.button("Add...").click();
      bot.text().setText(interfaceName);
      sleep();
      bot.button("OK").click();
    }
    bot.button("Finish").click();
    sleep();
  }

  public void createInterface(String interfaceName) {
    this.createInterface("net.sf.guavaeclipse.test", interfaceName);
  }

  public void createInterface(String packageName, String interfaceName) {
    bot.menu("New").menu("Interface").click();
    bot.textWithLabel("Pac&kage:").setText(packageName);
    bot.textWithLabel("Na&me:").setText(interfaceName);
    bot.button("Finish").click();
    sleep();
  }

  protected SWTBotEclipseEditor executeTestForSampleSimple(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple", 9);
    executePluginMethod(cutEditor, menuSelection.getMenuString());
    return cutEditor;
  }

  protected SWTBotEclipseEditor executeTestForExtendedClass(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("ExtendedSimpleClass");
    createClassWithSuperClass("ExtendedSimpleClass", "net.sf.guavaeclipse.test.SampleSimple");

    SWTBotEclipseEditor cutEditor = setClassContent("ExtendedSimpleClass", 7);
    executePluginMethod(cutEditor, menuSelection.getMenuString());
    return cutEditor;
  }

  protected SWTBotEclipseEditor executeTestForInterface(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("InterfaceSample");
    createInterface("InterfaceSample");
    SWTBotEclipseEditor cutEditor = setClassContent("InterfaceSample", 1);

    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("SampleImplementsInterface");
    createClassWithInterface("SampleImplementsInterface",
        "net.sf.guavaeclipse.test.InterfaceSample");

    cutEditor = setClassContent("SampleImplementsInterface", 11);
    executePluginMethod(cutEditor, menuSelection.getMenuString());
    return cutEditor;
  }

  protected SWTBotEclipseEditor executeTestForSuperClassAndInterface(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("SampleExtendedAndInterface");
    createClassWithSuperClassAndInterface("SampleExtendedAndInterface",
        "net.sf.guavaeclipse.test.SampleSimple", "net.sf.guavaeclipse.test.InterfaceSample");

    SWTBotEclipseEditor cutEditor = setClassContent("SampleExtendedAndInterface", 11);
    executePluginMethod(cutEditor, menuSelection.getMenuString());
    return cutEditor;
  }



}
