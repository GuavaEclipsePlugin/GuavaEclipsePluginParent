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
public class OnlyOneCommentCompareIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectOneCommentForCompareTo();
  }

  @Test
  public void createCompareTo() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(COMPARE_TO);

    String editorText = cutEditor.getText();
    String fileName = "compareResults/Expected_CompareTo.txt";
    logEditorResults(fileName, OnlyOneCommentCompareIntegrationTest.class, "createCompareTo()", editorText);
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
    String fileName = "compareResults/Expected_OneCommentSampleSimpleArray.txt";
    logEditorResults(fileName, OnlyOneCommentCompareIntegrationTest.class,
        "createCompareToWithArrays()", editorText);
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
    String fileName = "compareResults/Expected_OneCommentComparableObjectsClass.txt";
    logEditorResults(fileName, OnlyOneCommentCompareIntegrationTest.class,
        "createCompareToWithObjects()", editorText);
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
    String fileName = "compareResults/Expected_OneCommentComplexCompareTest.txt";
    logEditorResults(fileName, CompareIntegrationTest.class, "createCompareToComplex()",
        editorText);
    String expectedText = readFile(fileName);
    assertThat(editorText, is(expectedText));
  }


}
