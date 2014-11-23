package net.sf.guavaeclipse.swtbot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NoSuperEqualsHashCodeIntegrationTest extends
		AbstractSwtBotIntegrationTest {

	@BeforeClass
	public static void changePreferences() throws Exception {
		selectNoSuper();
	}

	@Test
	public void createEqualsHashCode() throws Exception {

		createJavaProjectIfNotExists("SampleJavaProject");
		deleteClassIfExists("SampleSimple");
		bot.menu("New").menu("Class").click();
		bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
		bot.textWithLabel("Na&me:").setText("SampleSimple");
		bot.button("Finish").click();
		sleep();
		SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText(readFile("Input_SampleSimple.txt"));
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
		String expectedText = readFile("Expected_EqualsHashCode.txt");
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
        
        SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText(readFile("Input_ExtendedSimpleClass.txt"));
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
		String expectedText = readFile("Expected_NoSuperEqualsHashCodeForExtendedClass.txt");
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
		cutEditor.setText(readFile("Input_InterfaceSample.txt"));
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

		cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText(readFile("Input_SampleImplementsInterface.txt"));
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
		String expectedText = readFile("Expected_EqualsHashCodeForInterfaceClass.txt");
		assertThat(editorText, is(expectedText));
	}

	@Test
	public void createEqualsHashCodeForInterfaceClassAndExtendedClass()
			throws Exception {
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
		
		SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.setText(readFile("Input_SampleExtendedAndInterface.txt"));
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
		String expectedText = readFile("Expected_NoSuperEqualsHashCodeForInterfaceAndExtendedClass.txt");
		assertThat(editorText, is(expectedText));
	}

}
