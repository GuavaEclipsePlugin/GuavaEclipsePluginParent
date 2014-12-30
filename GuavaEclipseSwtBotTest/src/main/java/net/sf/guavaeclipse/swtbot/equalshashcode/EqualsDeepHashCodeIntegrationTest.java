package net.sf.guavaeclipse.swtbot.equalshashcode;

import static net.sf.guavaeclipse.swtbot.MenuSelection.EQUALS_HASHCODE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.swtbot.AbstractSwtBotIntegrationTest;

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
public class EqualsDeepHashCodeIntegrationTest extends AbstractSwtBotIntegrationTest {

  @BeforeClass
  public static void changePreferences() throws Exception {
    selectSmartSuper();
    selectArraysDeepHashCode();
  }

  @AfterClass
  public static void changePreferencesSelectSmartHashCode() throws Exception {
    selectSmartHashCode();
  }

  @Test
  public void createEqualsHashCode() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSampleSimple(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText = readFile("equalsHashCodeResults/Expected_EqualsDeepHashCode.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForExtendedClass(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsDeepHashCodeForExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

  @Test
  public void createEqualsHashCodeForInterfaceClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsDeepHashCodeForInterfaceClass.txt");
    assertThat(editorText, is(expectedText));
  }


  @Test
  public void createEqualsHashCodeForInterfaceClassAndExtendedClass() throws Exception {

    SWTBotEclipseEditor cutEditor = executeTestForSuperClassAndInterface(EQUALS_HASHCODE);

    String editorText = cutEditor.getText();
    String expectedText =
        readFile("equalsHashCodeResults/Expected_EqualsDeepHashCodeForInterfaceAndExtendedClass.txt");
    assertThat(editorText, is(expectedText));
  }

}
