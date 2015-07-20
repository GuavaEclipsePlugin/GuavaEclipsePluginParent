package net.sf.guavaeclipse.swtbot.compare;

import static net.sf.guavaeclipse.swtbot.MenuSelection.COMPARE_TO;
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
public class ChangeTaskTag extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectMultiCommentsForCompareTo();
    setCompareToCommentTaskTag("FIXME");
  }
  
  @AfterClass
  public static void changePreferencesToXXX() throws Exception {
    setCompareToCommentTaskTag("XXX");
  }
  
  @Test
  public void createCompareToWithArrays() throws Exception {

    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimpleArray", 10);
    executePluginMethod(cutEditor, COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_ChangeTaskTagSampleSimpleArray.txt";
    logEditorResults(fileName, ChangeTaskTag.class, "createCompareToWithArrays()",
        editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));

  }
}
