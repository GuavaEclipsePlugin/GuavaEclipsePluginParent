package net.sf.guavaeclipse.swtbot;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.net.URL;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public abstract class AbstractSwtBotIntegrationTest {

	protected static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		closeWelcomeView();
	}

	@AfterClass
	public static void tearDown() {
		bot.sleep(2000);
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
			bot.waitUntil(Conditions.waitForShell(is(viewByTitle.bot()
					.activeShell().widget)));
		} catch (WidgetNotFoundException e) {
			// do nothing because welcome view is already closed...
		}
	}

	public void createJavaProjectIfNotExists(String projectName) {
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
				sleep();
				bot.tree().getTreeItem(projectName).select();
			} catch (WidgetNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String readExpectedFile(String fileName) {
		URL url = this.getClass().getClassLoader().getResource(fileName);
		try {
			return Resources.toString(url, Charsets.UTF_8).replaceAll("\r", "");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
