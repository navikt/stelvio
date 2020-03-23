package no.stelvio.batch.controller.client;

import java.util.Arrays;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.client.support.DefaultBatchRunner;
import no.stelvio.batch.controller.client.support.SpringBatchEnabledBatchRunner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generic client for execution of java ejb batches. Use <code>DefaultBatchRunner</code> for reading the client configuration
 * and to start the batch.
 * 
 *
 * @version $Id$
 * 
 */
public final class BatchControllerClient {

	private static SpringBatchLauncher springBatchLauncher = new SpringBatchLauncher();
	private static final Log LOG = LogFactory.getLog(BatchControllerClient.class);
	
	/**
	 * Constructs a new BatchControllerClient. This constructor is implemented because BatchControllerClient is a utility class
	 * and should not be instantiated.
	 */
	private BatchControllerClient() {
		super();
	}

	/**
	 * Gets the arguments and calls a batchrunner to start the batch.
	 * 
	 * 
	 * @param args The arguments for running the batch, 
	 *            Classic Stelvio batch: 
	 *            arg0=batchname,
	 *            arg1=slice
	 *            Spring batch:
	 *            arg0=jobname,
	 *            arg1=parameters
	 * 
	 */
	public static void main(String[] args) {
		if (springBatchLauncher.isSpringBatch(args)) {
			int returnStatus = springBatchLauncher.runBatch(args, new SpringBatchEnabledBatchRunner());
			if (LOG.isInfoEnabled()) {
				LOG.info("Batch has terminated with status: " + returnStatus);
			}
			System.exit(returnStatus);
		} else { 
			launchClassicStelvioBatch(args);
		}
		
	}

	/**
	 * Gets the arguments and calls a batchrunner to start the batch as a classic Stelvio batch.
	 * 
	 * @param args The arguments for running the batch, 
	 *            arg0=batchname,
	 *            arg1=slice
	 */
	private static void launchClassicStelvioBatch(String[] args) {
		int returnStatus = BatchStatus.BATCH_OK;		
		String batchName = "";
		int slice = -1;

		// read the params
		try {
			batchName = args[0];
			String sliceStr = args[1];
			slice = Integer.parseInt(sliceStr);
			System.out.println("Batch arguments are: batch name: "
					+ batchName + " , slice: " + sliceStr);

		} catch (Exception ex) {
			System.err
					.println("Usage: param1=<batch_name> param2=<slice> param3=stop (optional)");
			System.exit(BatchStatus.BATCH_FATAL);
		}

		// Create runner
		BatchRunner batchRunner = new DefaultBatchRunner();

		// Stop argument supplied
		if (args.length == 3 && args[2].equalsIgnoreCase("stop")) {
			System.out.println("Attempting to stop batch...");

			boolean stopWasSent = batchRunner.stopBatch(batchName, slice);
			if (stopWasSent) {
				System.out.println("A stop request was sent to the batch "
						+ batchName + " slice " + slice);
				System.exit(0);
			} else {
				System.out.print("No running batch with name " + batchName
						+ " and slice " + slice
						+ " was found, there was nothing to stop. ");
				System.out.println("This is because there was no running batch, " 
						+ "or the batch was running on a node in a cluster that was unreachable.");
				System.exit(16);
			}
		} else {
			System.out.println("Starting batch...");
			// start the batch
			returnStatus = batchRunner.runBatch(batchName, slice);

			// exit with return value from batch
			System.out.println("Batch has terminated with status: "
					+ returnStatus);
			System.exit(returnStatus);
		}
	}	
	
	/**
	 * 
	 *
	 */
	static class SpringBatchLauncher {
		
		/**
		 * Checks if arguments matches input for a spring batch exectuion.
		 * @param args Command line arguments
		 * @return true if arguments matches spring batch execution, false otherwise.
		 */
		public boolean isSpringBatch(String[] args) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				return true;
			} catch (Exception e) {
				return false;
			}
			return false;
		}

		/**
		 * Parse arguments and launch batch.
		 * @param args Command line arguments
		 * @param batchRunner Runner that launches the remote ejb-batch.
		 * @return Statuscode of execution, see {@link BatchStatus}.
		 */
		public int runBatch(String[] args, SpringBatchEnabledBatchRunner batchRunner) {
			if (Arrays.asList(args).contains(("stop"))) {
				boolean stopMessageSent = batchRunner.stopBatch(args[0]);
				return stopMessageSent ? BatchStatus.BATCH_OK : BatchStatus.BATCH_FATAL;
			} else {
				return batchRunner.runBatch(args[0], args[1]);			
			}
		}
	}

}
