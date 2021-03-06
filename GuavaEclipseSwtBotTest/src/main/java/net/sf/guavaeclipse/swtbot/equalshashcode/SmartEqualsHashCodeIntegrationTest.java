package net.sf.guavaeclipse.swtbot.equalshashcode;

import static net.sf.guavaeclipse.swtbot.MenuSelection.EQUALS_HASHCODE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SmartEqualsHashCodeIntegrationTest extends AbstractSwtBotIntegrationTest {

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
  public void createEqualsHashCode() throws Exception {
    selectSmartHashCode();
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, EQUALS_HASHCODE);


    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_SmartEqualsHashCode.txt";
    logEditorResults(fileName, SmartEqualsHashCodeIntegrationTest.class, "createEqualsHashCode()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }


  @Test
  public void createEqualsHashCodeAlwaysGuava() throws Exception {
    selectObjectsHashCode();
    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, EQUALS_HASHCODE);


    String editorText = cutEditor.getText();
    String fileName = "equalsHashCodeResults/Expected_EqualsHashCodeArray.txt";
    logEditorResults(fileName, SmartEqualsHashCodeIntegrationTest.class, "createEqualsHashCodeAlwaysGuava()", editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }

}
