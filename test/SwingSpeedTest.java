package test;

import java.util.GregorianCalendar;
import javax.swing.SwingUtilities;

/**
 * speed comparison java.lang.Thread vs. java.lang.Runnable
 * when calling {@link SwingUtilities#invokeAndWait(Runnable)}.
 */
public class SwingSpeedTest {
	
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * How many times invokeAndWait will be called in a test
	 */
	public static final int TESTCOUNT = 100000;

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("Java Version : " + System.getProperty("java.version"));
		System.out.println("JVM Vendor : " + System.getProperty("java.vm.vendor"));
		System.out.println("Architecture : " + System.getProperty("os.arch"));
		System.out.println("OS : " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		
		// true : test with java.lang.Thread , false : test with java.lang.Runnable
		boolean testWithThread = false;
		
		// the first test seems to be affected by overhead to load class 
		test(testWithThread);
		
		long sum = 0L;
		long elapsed = 0L;
		for(int i=0; i<20; i++) {
			elapsed = test(testWithThread);
			System.out.println("new " + (testWithThread?"Thread":"Runnable") + "() : " + elapsed + "ms");
			sum += elapsed;
			System.gc();
		}
		
		System.out.println("done. total=" + sum + "ms. av=" + (sum/20) + "ms.");
	}
	
	public static long test(boolean testWithThread) {
		GregorianCalendar start = null;
		GregorianCalendar end = null;
		long elapsed = 0L;
		
		// timestamp before test
		start = new GregorianCalendar();
		try {
			for(int i=0; i<TESTCOUNT; i++) {
				Runnable runnable = null;
				if(testWithThread) {
					runnable = new Thread() {
						public void run() {
							int a=1;
							a++;
						}
					};
				}
				else {
					runnable = new Runnable() {
						public void run() {
							int a=1;
							a++;
						}
					};
				}
				// change here to invokeLater or nothing
				SwingUtilities.invokeAndWait(runnable);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// timestamp after test
		end = new GregorianCalendar();
		
		elapsed = end.getTimeInMillis() - start.getTimeInMillis();
		return elapsed;
	}

}
