package net.sf.guavaeclipse.swtbot.equalshashcode;

import static net.sf.guavaeclipse.swtbot.MenuSelection.EQUALS_HASHCODE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
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
public class EqualHashCodeGetterIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
    selectUseGetter();
  }

  @AfterClass
  public static void changePreferencesSelectUseField() throws Exception {
    selectUseField();
  }

  @Test
  public void createEqualsHashCode() throws Exception {

    createJavaProjectIfNotExists("SampleJavaProject");
    deleteClassIfExists("SampleSimple");
    createClass("SampleSimple");
    SWTBotEclipseEditor cutEditor = setClassContent("SampleSimple", 9);

    generateGetter(cutEditor, 7, "strValue");

    executePluginMethod(cutEditor, EQUALS_HASHCODE.getMenuString());

    String editorText = cutEditor.getText();
    String expectedText = readFile("equalsHashCodeResults/Expected_EqualsHashCode_Getter.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForExtendedClass() throws Exception {

    bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
    bot.tree().getTreeItem("SampleJavaProject").select();
    deleteClassIfExists("ExtendedSimpleClass");
    createClassWithSuperClass("ExtendedSimpleClass", "net.sf.guavaeclipse.test.SampleSimple");

    SWTBotEclipseEditor cutEditor = setClassContent("ExtendedSimpleClass", 7);

    generateGetter(cutEditor, 6, "objectValue");

    executePluginMethod(cutEditor, EQUALS_HASHCODE.getMenuString());

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsHashCodeForExtendedClass_Getter.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceClass_Getter.txt");
    assertThat(editorText, is(expectedText));
  }


  @Test
  public void createEqualsHashCodeForInterfaceClassAndExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSuperClassAndInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsHashCodeForInterfaceAndExtendedClass_Getter.txt");
    assertThat(editorText, is(expectedText));
  }

}
