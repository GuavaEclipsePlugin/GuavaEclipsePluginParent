package net.sf.guavaeclipse.swtbot.tostring;

import static net.sf.guavaeclipse.swtbot.MenuSelection.TO_STRING;
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

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MoreObjectsArraysToStringIntegrationTest extends ArraysToStringIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
  }

  @AfterClass
  public static void changePreferencesAfterClass() throws Exception {
    selectSmartSuper();
    selectSmartHashCode();
  }

  @Test
  public void createToString() throws Exception {

    selectSmartHashCode();
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, TO_STRING);


    String editorText = cutEditor.getText();
    String fileName = "toStringResults/MoreObjectsExpected_SmartToString.txt";
    logEditorResults(fileName, MoreObjectsArraysToStringIntegrationTest.class, "createToString()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createToStringAlwaysObjects() throws Exception {
    selectObjectsHashCode();
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, TO_STRING);


    String editorText = cutEditor.getText();
    String fileName = "toStringResults/MoreObjectsExpected_ObjectsToString.txt";
    logEditorResults(fileName, MoreObjectsArraysToStringIntegrationTest.class, "createToStringAlwaysObjects()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createToStringAlwaysArrays() throws Exception {
    selectArraysDeepHashCode();
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, TO_STRING);


    String editorText = cutEditor.getText();
    String fileName = "toStringResults/MoreObjectsExpected_ArraysToString.txt";
    logEditorResults(fileName, MoreObjectsArraysToStringIntegrationTest.class, "createToStringAlwaysArrays()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
