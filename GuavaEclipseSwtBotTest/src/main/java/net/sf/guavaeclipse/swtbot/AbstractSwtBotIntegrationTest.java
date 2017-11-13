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

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.ContextMenuHelper;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractSwtBotIntegrationTest {

  private final Logger logger = Logger.getLogger(AbstractSwtBotIntegrationTest.class);

  protected static SWTWorkbenchBot bot;

  @BeforeClass
  public static void beforeClass() throws Exception {
    bot = new SWTWorkbenchBot();
    closeWelcomeView();
    prepareWorkSpace();
  }

   @AfterClass
   public static void tearDown() {
       SWTBotMenu menu = bot.menu("File").click();
       SWTBotMenu saveAll = menu.menu("Save All");
       if (saveAll.isEnabled()) {
         saveAll.click();
       }
   }

  public static void prepareWorkSpace() {

    SWTBotMenu menu = bot.menu("Window").click();
    menu.menu("Preferences").click();

    
    SWTBotShell shell = bot.shell("Preferences");
    shell.activate();
    waitForPreferencesShell();
    
    try {
    bot.toolbarButtonWithTooltip("Oomph preference recorder enabled - Push to disable").click();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    
    try {
//    SWTBot bot = shell.bot();
    bot.tree(0).getTreeItem("General").expand().select();
    waitForPreferencesShell();
    bot.tree().getTreeItem("General").getNode("Error Reporting").select();
    waitForPreferencesShell();
    bot.comboBox().setSelection("Never send");
    waitForPreferencesShell();
    bot.button("Apply").click();
    waitForPreferencesShell();
    } catch (Throwable t) {
      t.printStackTrace();
    }


    try {
      bot.tree().getTreeItem("Java").expand().select();
      waitForPreferencesShell();
      bot.tree().getTreeItem("Java").getNode("Code Style").expand().select();
      waitForPreferencesShell();
      bot.tree().getTreeItem("Java").getNode("Code Style").getNode("Formatter").select();
      if (!"Eclipse [built-in] 120".equals(bot.comboBox().selection())) {
        bot.comboBox().setSelection("Eclipse [built-in]");
        bot.button("Edit...").click();
        waitForPreferencesShell();
        bot.textWithLabel("&Profile name:").setText("Eclipse [built-in] 120");
        waitForPreferencesShell();
        bot.tabItem("Line Wrappin&g").activate();
        waitForPreferencesShell();
        bot.textWithLabel("Max&imum line width:").setText("120");
        waitForPreferencesShell();
        
        bot.button("OK").click();
        bot.button("Apply").click();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
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

  private static void waitForPreferencesShell() {
    bot.waitUntil(Conditions.shellIsActive("Preferences"));
  }

  protected static void selectUseAlwaysSuper() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotRadio radio = bot.radio("Use super class Methods (toString(), equals() and hashCode())");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  private static SWTBotShell openGuavaPreferences() {
    
//    disableOomph();
    
    SWTBotMenu menu = bot.menu("Window").click();
    menu.menu("Preferences").click();

    SWTBotShell shell = bot.shell("Preferences");
    shell.activate();
    waitForPreferencesShell();
    bot.tree().getTreeItem("Guava Preference").select();
    waitForPreferencesShell();
    return shell;
  }

  protected static void selectNoSuper() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotRadio radio =
        bot.radio("Don't use super class Methods (toString(), equals() and hashCode())");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectSmartSuper() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotRadio radio =
        bot.radio("Use super class Methods (Only if superclass is not \"java.lang.Object\")");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();

    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectMoreObjects() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotCheckBox checkBox =
        bot.checkBox("Use MoreObjects in toString Method (requires guava 18.0)");
    checkBox.setFocus();
    if (!checkBox.isChecked()) {
      checkBox.click();
    }
    waitForPreferencesShell();

    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void deselectMoreObjects() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotCheckBox checkBox =
        bot.checkBox("Use MoreObjects in toString Method (requires guava 18.0)");
    checkBox.setFocus();
    if (checkBox.isChecked()) {
      checkBox.click();
    }
    waitForPreferencesShell();

    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectUseJava7Objects() {
    SWTBotShell shell = openEqualsHashCodePreferences();

    SWTBotCheckBox checkBox =
        bot.checkBox("Use java.util.Objects in equals/hashCode Methods (requires JDK 1.7 or higher)");
    checkBox.setFocus();
    if (!checkBox.isChecked()) {
      checkBox.click();
    }
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void deselectUseJava7Objects() {
    SWTBotShell shell = openEqualsHashCodePreferences();

    SWTBotCheckBox checkBox =
        bot.checkBox("Use java.util.Objects in equals/hashCode Methods (requires JDK 1.7 or higher)");
    checkBox.setFocus();
    if (checkBox.isChecked()) {
      checkBox.click();
    }
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectUseGetter() {
    SWTBotShell shell = openEqualsHashCodePreferences();

    SWTBotRadio radio = bot.radio("use getter methods");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectUseField() {
    SWTBotShell shell = openEqualsHashCodePreferences();

    SWTBotRadio radio = bot.radio("use fields");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectArraysDeepHashCode() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotRadio radio = bot.radio("use always java.util.Arrays.deep Utility Methods");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectObjectsHashCode() {
    SWTBotShell shell = openGuavaPreferences();

    SWTBotRadio radio = bot.radio("use always com.google.common.base.Objects Utility Methods");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectSmartHashCode() {
    SWTBotShell shell = openGuavaPreferences();
    SWTBotRadio radio = bot.radio("Use java.util.Arrays.deep Utility methods only when necessary");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  private static SWTBotShell openEqualsHashCodePreferences() {
    SWTBotShell shell = openGuavaPreferences();
    bot.tree().getTreeItem("Guava Preference").expand().getNode("Equals/HashCode Methods Preferences").select();
    waitForPreferencesShell();
    return shell;
  }

  protected static void selectMultiCommentsForCompareTo() {
    SWTBotShell shell = openCompareToPreferences();
    SWTBotRadio radio = bot.radio("for every non-comparable field a seperate comment");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectOneCommentForCompareTo() {
    SWTBotShell shell = openCompareToPreferences();
    SWTBotRadio radio = bot.radio("only one comment at beginning of method");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  protected static void selectNoCommentForCompareTo() {
    SWTBotShell shell = openCompareToPreferences();
    SWTBotRadio radio = bot.radio("no comments at all (not recommended)");
    radio.setFocus();
    radio.click();
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  private static SWTBotShell openCompareToPreferences() {
    SWTBotShell shell = openGuavaPreferences();
    bot.tree().getTreeItem("Guava Preference").expand().getNode("CompareTo Method Preference").select();
    waitForPreferencesShell();
    return shell;
  }
  
  protected static void setCompareToCommentTaskTag(String taskTag) {
    SWTBotShell shell = openCompareToPreferences();
    SWTBotText text = bot.textWithLabel("Task Tag prefix for compareTo comments");
    text.setFocus();
    text.setText(taskTag);
    waitForPreferencesShell();
    bot.button("OK").click();
    bot.waitUntil(Conditions.shellCloses(shell));
  }

  public void createJavaProjectIfNotExists(String projectName) {
    if (!"Java".equals(bot.activePerspective().getLabel())) {
      try {
        bot.menu("Window").menu("Open Perspective").menu("Java").click();
      } catch (WidgetNotFoundException e) {
        // do nothing java perspective is already open
      }
    }
    try {
      SWTBotTree tree = bot.tree();
      boolean projectExists = false;
      if (tree.hasItems()) {
        SWTBotTreeItem[] allItems = tree.getAllItems();
        for (int i = 0; i < allItems.length; i++) {
          SWTBotTreeItem item = allItems[i];
          if (projectName.equals(item.getText())) {
            projectExists = true;
          }
        }
      }
      if (!projectExists) {
        createProject(projectName);
      }
    } catch (WidgetNotFoundException e) {
      logger.error("Error during creation Project " + projectName, e);
    }
  }

  private void createProject(String projectName) {
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
      logger.error("Error during creation Project " + projectName, e1);
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

  public void executePluginMethod(final SWTBotEclipseEditor cutEditor,
      final MenuSelection menuSelection) {
    SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
    contextMenu.setFocus();
    contextMenu.menu(menuSelection.getMenuString()).click();

    // final SWTBotShell shell =
    // bot.shell("Generate hashCode() and equals() for '" + getClassName(cutEditor)
    // + "' class");
    // shell.activate();
    processDialog(cutEditor, menuSelection);
  }

  protected void processDialog(final SWTBotEclipseEditor cutEditor,
      final MenuSelection menuSelection) {
    bot.waitUntil(Conditions.shellIsActive("Generate " + getMenuSelectionShellName(menuSelection)
        + " for '" + getClassName(cutEditor) + "' class"));

    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        SWTBotShell shell =
            bot.shell("Generate " + getMenuSelectionShellName(menuSelection) + " for '"
                + getClassName(cutEditor) + "' class");
        SWTBot currentBot = shell.bot();
        currentBot.button("Select All").click();
        currentBot.button("OK").click();
        shell.close();
        bot.waitUntil(Conditions.shellCloses(shell));
      }
    });
    cutEditor.save();
  }

  public String getMenuSelectionShellName(MenuSelection menuSelection) {
    if (MenuSelection.TO_STRING == menuSelection) {
      return "toString()";
    } else if (MenuSelection.COMPARE_TO == menuSelection) {
      return "compareTo()";
    } else if (MenuSelection.EQUALS_HASHCODE == menuSelection) {
      return "hashCode() and equals()";
    } else {
      logger.error("Unknown MENU_SELECTION - this is an ERROR");
      return "unknown - this is an ERROR";
    }
  }

  private String getClassName(SWTBotEclipseEditor cutEditor) {
    return cutEditor.getTitle().substring(0, cutEditor.getTitle().length() - 5);
  }

  public SWTBotEclipseEditor setClassContent(final String inputFileName, final int lineToSetCursor)
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

  public void createClass(final String packageName, final String className,
      final String superClass, final String interfaceName) {
    bot.menu("New").menu("Class").click();
    bot.waitUntil(Conditions.shellIsActive("New Java Class"));

    // SWTBotShell shell = bot.shell("New Java Class");
    // SWTBot currentBot = shell.bot();
    bot.textWithLabel("Pac&kage:").setText(packageName);
    bot.textWithLabel("Na&me:").setText(className);
    if (superClass != null && !superClass.isEmpty()) {
      bot.textWithLabel("&Superclass:").setText(superClass);
    }
    if (interfaceName != null && !interfaceName.isEmpty()) {
      bot.button("Add...").click();
      // bot.waitUntil(Conditions.shellIsActive("Implemented Interfaces Selection"));
      // Implemented Interface Selection
      bot.text().setText(interfaceName);
      final SWTBotTable tableWithLabel = bot.tableWithLabel("Matching items:");
      bot.waitUntil(new DefaultCondition() {

        @Override
        public boolean test() throws Exception {
          if (tableWithLabel.getTableItem("InterfaceSample - net.sf.guavaeclipse.test").isVisible()) {
            tableWithLabel.getTableItem("InterfaceSample - net.sf.guavaeclipse.test").select();
            return true;
          }
          return false;
        }

        @Override
        public String getFailureMessage() {
          return interfaceName + " could not be found";
        }
      });
      SWTBotButton button = bot.button("OK");
      button.click();
    }
    bot.button("Finish").click();
    bot.waitUntil(Conditions.shellCloses(bot.shell("New Java Class")));
  }

  public void createInterface(String interfaceName) {
    this.createInterface("net.sf.guavaeclipse.test", interfaceName);
  }

  public void createInterface(final String packageName, final String interfaceName) {
    bot.menu("New").menu("Interface").click();
    bot.waitUntil(Conditions.shellIsActive("New Java Interface"));

    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        SWTBotShell shell = bot.shell("New Java Interface");
        SWTBot currentBot = shell.bot();
        currentBot.textWithLabel("Pac&kage:").setText(packageName);
        currentBot.textWithLabel("Na&me:").setText(interfaceName);
        currentBot.button("Finish").click();
        shell.close();
        bot.waitUntil(Conditions.shellCloses(shell));
      }
    });
    // sleep();
  }

  protected SWTBotEclipseEditor executeTestForSampleSimple(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple", 9);
    executePluginMethod(cutEditor, menuSelection);
    return cutEditor;
  }

  protected SWTBotEclipseEditor executeTestForExtendedClass(MenuSelection menuSelection)
      throws IOException, URISyntaxException {
    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("ExtendedSimpleClass");
    createClassWithSuperClass("ExtendedSimpleClass", "net.sf.guavaeclipse.test.SampleSimple");

    SWTBotEclipseEditor cutEditor = setClassContent("ExtendedSimpleClass", 7);
    executePluginMethod(cutEditor, menuSelection);
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
    executePluginMethod(cutEditor, menuSelection);
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
    executePluginMethod(cutEditor, menuSelection);
    return cutEditor;
  }

  protected void generateGetter(final SWTBotEclipseEditor cutEditor, final int lineNrwithGetter,
      final String fieldName) {
    cutEditor.selectLine(lineNrwithGetter);
    // SWTBotMenu contextMenu = cutEditor.contextMenu("Source");
    // contextMenu.setFocus();
    // contextMenu.menu("Generate Getters and Setters...").click();

    new SWTBotMenu(ContextMenuHelper.contextMenu(bot.tree(), "Source",
        "Generate Getters and Setters...")).click();
    bot.waitUntil(Conditions.shellIsActive("Generate Getters and Setters"));

    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        SWTBotShell shell = bot.shell("Generate Getters and Setters");
        // shell.activate();
        SWTBot currentBot = shell.bot();
        currentBot.tree().setFocus();
        currentBot.tree().getTreeItem(fieldName).select();
        currentBot.tree().getTreeItem(fieldName).expand();
        SWTBotTreeItem select =
            currentBot.tree().getTreeItem(fieldName).getNode(getGetterMethodName(fieldName))
                .select();
        select.check();
        currentBot.comboBox().setSelection("After '" + fieldName + "'");
        currentBot.button("OK").click();
        shell.close();
        bot.waitUntil(Conditions.shellCloses(shell));
      }
    });

  }

  private String getGetterMethodName(String field) {
    StringBuilder bu = new StringBuilder("get");
    bu.append(field.substring(0, 1).toUpperCase());
    bu.append(field.substring(1, field.length()));
    bu.append("()");
    return bu.toString();
  }

  @SuppressWarnings("rawtypes")
  protected void logEditorResults(String filename, Class clazz, String methodName, String editorText) {
    if (logger.isInfoEnabled()) {
      logger.info("ResultFile: "+filename+" TestCase"+clazz.getName() + "#" + methodName + " Editor Result:\n" + editorText);
    }
//    logger.error(clazz.getName() + "#" + methodName + " Editor Result:\n" + editorText);
  }

}
