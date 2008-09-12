import no.nav.bpchelper.actions.Action;
import no.nav.bpchelper.actions.ActionFactory;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


public class Main {
	public static void main(String[] args) {
		new Main().run(args);	
	}

	private void run(String[] args) {
		OptionsBuilder optionsBuilder = new OptionsBuilder();
		Options options = optionsBuilder.getOptions();
		
		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException parseEx) {
			System.out.println("Incorrect arguments (listed below). Due to this will the application now terminate");
			System.out.println(parseEx.getMessage());
			System.exit(-1); // TODO AR Find correct return code for error
		}
		
		if (cl.hasOption(OptionOpts.HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("BPCHelper", "Sample usage: launchClient BPCHelper", options, null);
			System.exit(0);
		}
		
		Action action = ActionFactory.getAction(cl);
		action.process();
		
		/*
		
		System.out.println("Arguments" + Arrays.toString(args));
		if (args.length<2){
			System.out.println("Usage: launchClient BPCHelper PROCESS|ACTIVITY ProcessID|ActivityID DELETE|TERMINATE ");
			System.exit(1);
		}
		
		try {
			
			InitialContext ctx=new InitialContext();
			Object result=ctx.lookup("java:comp/env/ejb/BusinessFlowManagerHome");
			BusinessFlowManagerHome processHome=(BusinessFlowManagerHome) PortableRemoteObject.narrow(result, BusinessFlowManagerHome.class);
			BusinessFlowManager businessFlowManager = processHome.create();
			if (args[0].equalsIgnoreCase("PROCESS")){
				
				String pIIDString=args[1];
				//ProcessTemplateData res = businessFlowManager.getProcessTemplate("LRBPEL");
				ProcessInstanceData processInstance = businessFlowManager.getProcessInstance(pIIDString);
				System.out.println("Type: "+processInstance.getProcessTemplateName()+" Date:"+processInstance.getCreationTime());
				String operationType=args[2];
				if (operationType.equalsIgnoreCase("DELETE")){
					businessFlowManager.delete(processInstance.getID());
					System.out.println("DELETED process");
				}else
				if (operationType.equalsIgnoreCase("TERMINATE")){
					businessFlowManager.forceTerminate(processInstance.getID());	
					System.out.println("TERMINATED process");
				}
			}
			if (args[0].equalsIgnoreCase("ACTIVITY")){
				String aIIDString=args[1];
				ActivityInstanceData activityInstance = businessFlowManager.getActivityInstance(aIIDString);
				System.out.println("Name:"+activityInstance.getName() + "Date: "+activityInstance.getStartTime());
				System.out.println("Fault: "+businessFlowManager.getFaultMessage(activityInstance.getID()));
				
			}
			
			
			
			
			
		} catch (Exception e) {		
			throw new RuntimeException(e);
		}
		*/
	}
}