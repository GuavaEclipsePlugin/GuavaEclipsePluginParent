package net.sf.guavaeclipse.swtbot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EqualsHashCodeIntegrationTest extends
		AbstractSwtBotIntegrationTest {

	@Test
	public void createEqualsHashCode() throws Exception {

		createJavaProjectIfNotExists("SampleJavaProject");
		bot.menu("New").menu("Class").click();
		bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
		bot.textWithLabel("Na&me:").setText("SampleSimple");
		bot.button("Finish").click();
		sleep();
        SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
        cutEditor.setText(
		        "package net.sf.guavaeclipse.test;\n"
		      + "\n" 
		      + "public class SampleSimple {\n" 
		      + "	\n" 
		      + "	\n" 
		      + "	private int intValue;\n" 
		      + "	\n"
		      + "	private String strValue;\n" 
		      + "	\n" 
		      + "	\n" 
		      + "}\n");
        cutEditor.save();
		cutEditor.selectLine(9);
		SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
		contextMenu.setFocus();
		contextMenu.menu("Generate hashCode() and equals()").click();
		bot.button("Select All").click();
		bot.button("OK").click();
		sleep();
		cutEditor.save();

		String editorText = cutEditor.getText();
		String expectedText = readExpectedFile("Expected_EqualsHashCode.txt");
		assertThat(editorText, is(expectedText));
	}

	@Test
	public void createEqualsHashCodeForExtendedClass() throws Exception {

		bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
        bot.tree().getTreeItem("SampleJavaProject").select();
        bot.menu("New").menu("Class").click();
        bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
        bot.textWithLabel("Na&me:").setText("ExtendedSimpleClass");
        bot.textWithLabel("&Superclass:").setText("net.sf.guavaeclipse.test.SampleSimple");
        bot.button("Finish").click();
		sleep();
        
        SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
        cutEditor.setText(
                "package net.sf.guavaeclipse.test;\n"
              + "\n" 
              + "public class ExtendSimpleSample extends SampleSimple {\n" 
              + "   \n" 
              + "   \n" 
              + "   private Object objectValue;\n" 
              + "   \n"
              + "}\n");
        cutEditor.save();
        cutEditor.selectLine(7);
        SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
        contextMenu.setFocus();
        contextMenu.menu("Generate hashCode() and equals()").click();
        bot.button("Select All").click();
        bot.button("OK").click();
		sleep();
        cutEditor.save();

        String editorText = cutEditor.getText();
		String expectedText = readExpectedFile("Expected_EqualsHashCodeForExtendedClass.txt");
		assertThat(editorText, is(expectedText));
	}
	
	@Test
	public void createEqualsHashCodeForInterfaceClass() throws Exception {

		bot.waitUntil(Conditions.treeHasRows(bot.tree(), 1));
		bot.tree().getTreeItem("SampleJavaProject").select();
		bot.menu("New").menu("Interface").click();
		bot.textWithLabel("Na&me:").setText("InterfaceSample");
		bot.button("Finish").click();
		sleep();
		SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText("package net.sf.guavaeclipse.test;\n" + 
				"\n" + 
				"public interface InterfaceSample {\n" + 
				"\n" + 
				"	public String getStringValue\n" + 
				"}\n");
		cutEditor.save();

		bot.tree().getTreeItem("SampleJavaProject").select();
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

		cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText(
				"package net.sf.guavaeclipse.test;\n" + 
				"\n" + 
				"public class SampleImplementsInterface implements InterfaceSample {\n" + 
				"   \n" + 
				"   private String stringValue;\n" + 
				"   \n" + 
				"   @Override\n" +
				"   public String getStringValue() {\n" +
				"   	return this.stringValue;\n" +
				"   }\n" +
				"   \n" + 
				"}\n");
		cutEditor.save();
		cutEditor.selectLine(11);
		SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
		contextMenu.setFocus();
		contextMenu.menu("Generate hashCode() and equals()").click();
		bot.button("Select All").click();
		bot.button("OK").click();
		sleep();
		cutEditor.save();

		String editorText = cutEditor.getText();
		String expectedText = readExpectedFile("Expected_EqualsHashCodeForInterfaceClass.txt");
		assertThat(editorText, is(expectedText));
	}

}
