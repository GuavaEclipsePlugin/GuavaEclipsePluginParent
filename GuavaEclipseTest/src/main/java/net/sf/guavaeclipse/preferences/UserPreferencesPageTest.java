package net.sf.guavaeclipse.preferences;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.sf.guavaeclipse.constants.EqualsEqualityType;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.builder.Activator;
import com.builder.constant.MethodGenerationStratergy;
import com.builder.constant.UserPrefernce;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserPreferencesPageTest {

	@Before
	public void setUp() throws Exception {
		// Initialize the test fixture for each test
		// that is run.
		waitForJobs();
	}

	@After
	public void tearDown() throws Exception {
		waitForJobs();
	}

	@Test
	public void testDefaultPreferenceStoreMethodGenerationStratergy() {
		assertThat(UserPrefernce.getMethodGenerationStratergy(), is(MethodGenerationStratergy.SMART_OPTION));
	}

	@Test
	public void testChoice1PreferenceStoreMethodGenerationStratergy() {
		Activator.getDefault().getPreferenceStore().setValue("guavaPreference", "choice1");
		assertThat(UserPrefernce.getMethodGenerationStratergy(), is(MethodGenerationStratergy.USE_SUPER));
	}

	@Test
	public void testChoice2PreferenceStoreMethodGenerationStratergy() {
		Activator.getDefault().getPreferenceStore().setValue("guavaPreference", "choice2");
		assertThat(UserPrefernce.getMethodGenerationStratergy(), is(MethodGenerationStratergy.DONT_USE_SUPER));
	}

	@Test
	public void testChoice3PreferenceStoreMethodGenerationStratergy() {
		Activator.getDefault().getPreferenceStore().setValue("guavaPreference", "choice3");
		assertThat(UserPrefernce.getMethodGenerationStratergy(), is(MethodGenerationStratergy.SMART_OPTION));
	}

	@Test
	public void testDefaultEqualsEqualityType() {
		assertThat(UserPrefernce.getEqualsEqualityType(), is(EqualsEqualityType.INSTANCEOF));
	}

	@Test
	public void testCoice1EqualsEqualityType() {
		Activator.getDefault().getPreferenceStore().setValue("guavaEclipseEqualsPreference", EqualsEqualityType.INSTANCEOF.name());
		assertThat(UserPrefernce.getEqualsEqualityType(), is(EqualsEqualityType.INSTANCEOF));
	}

	@Test
	public void testChoice2EqualsEqualityType() {
		Activator.getDefault().getPreferenceStore().setValue("guavaEclipseEqualsPreference", EqualsEqualityType.CLASS_EQUALITY.name());
		assertThat(UserPrefernce.getEqualsEqualityType(), is(EqualsEqualityType.CLASS_EQUALITY));
	}

	/**
	 * Wait until all background tasks are complete.
	 */
	public void waitForJobs() {
		while (!Job.getJobManager().isIdle())
			delay(1000);
	}

	private void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();

		// If this is the UI thread,
		// then process input.

		if (display != null) {
			long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
			while (System.currentTimeMillis() < endTimeMillis) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
			try {
				Thread.sleep(waitTimeMillis);
			} catch (InterruptedException e) {
				// Ignored.
			}
		}
	}

}
