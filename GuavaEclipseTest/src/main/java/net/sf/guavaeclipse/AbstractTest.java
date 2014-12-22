package net.sf.guavaeclipse;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractTest {

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
