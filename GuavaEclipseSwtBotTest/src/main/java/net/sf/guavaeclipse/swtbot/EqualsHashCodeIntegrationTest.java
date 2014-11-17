package net.sf.guavaeclipse.swtbot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EqualsHashCodeIntegrationTest {

	private static final String expectedText = 
		"package net.sf.guavaeclipse.test;\n" + 
		"\n" + 
		"import com.google.common.base.Objects;\n" + 
		"\n" + 
		"public class SampleSimple {\n" + 
		"	\n" + 
		"	\n" + 
		"	private int intValue;\n" + 
		"	\n" + 
		"	private String strValue;\n" + 
		"\n" + 
		"	@Override\n" + 
		"	public int hashCode(){\n" + 
		"		return Objects.hashCode(intValue, strValue);\n" + 
		"	}\n" + 
		"	\n" + 
		"	@Override\n" + 
		"	public boolean equals(Object object){\n" + 
		"		if (object instanceof SampleSimple) {\n" + 
		"			SampleSimple that = (SampleSimple) object;\n" + 
		"			return Objects.equal(this.intValue, that.intValue)\n" + 
		"				&& Objects.equal(this.strValue, that.strValue);\n" + 
		"		}\n" + 
		"		return false;\n" + 
		"	}\n" + 
		"	\n" + 
		"	\n" + 
		"}\n";

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		closeWelcomeView();
	}

	private static void closeWelcomeView() {
		try {
			SWTBotView viewByTitle = bot.viewByTitle("Welcome");
			viewByTitle.close();
			bot.waitUntil(Conditions.waitForShell(is(viewByTitle.bot()
					.activeShell().widget)));
		} catch (WidgetNotFoundException e) {
			// do nothing because welcome view is already closed...
		}
	}

	@Test
	public void createEqualsHashCode() throws Exception {
		

		createJavaProjectIfNotExists("SampleJavaProject");
		bot.tree().getTreeItem("SampleJavaProject").select();
		bot.menu("New").menu("Class").click();
		bot.textWithLabel("Pac&kage:").setText("net.sf.guavaeclipse.test");
		bot.textWithLabel("Na&me:").setText("SampleSimple");
		bot.button("Finish").click();
		bot.sleep(2000);
		bot.tree().getTreeItem("SampleJavaProject").getNode("src").expand();
		bot.tree().getTreeItem("SampleJavaProject").getNode("src").getNode("net.sf.guavaeclipse.test").expand();
		bot.styledText().setText(
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
		bot.saveAllEditors();
		bot.sleep(2000);
		SWTBotEclipseEditor cutEditor = bot.activeEditor().toTextEditor();
		cutEditor.selectLine(9);
		SWTBotMenu contextMenu = cutEditor.contextMenu("Google Guava Helper");
		contextMenu.setFocus();
		contextMenu.menu("Generate hashCode() and equals()").click();
		bot.button("Select All").click();
		bot.button("OK").click();
		bot.sleep(1000);
		cutEditor.save();

		String editorText = cutEditor.getText();
		assertThat(editorText, is(expectedText));
	}

	private static void createJavaProjectIfNotExists(String projectName) {
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
			bot.button("Finish").click();
			bot.sleep(2000);
			bot.tree().getTreeItem(projectName).select();
			} catch (WidgetNotFoundException e1) {
				e1.printStackTrace();
			}
		}

	}

	@AfterClass
	public static void sleep() {
		bot.sleep(2000);
	}

}
