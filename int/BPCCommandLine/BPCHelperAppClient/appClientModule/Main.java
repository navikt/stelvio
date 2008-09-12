import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.ibm.bpe.api.ActivityInstanceData;
import com.ibm.bpe.api.BusinessFlowManager;
import com.ibm.bpe.api.BusinessFlowManagerHome;
import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.ProcessInstanceData;
import com.ibm.bpe.api.ProcessTemplateData;


public class Main {
	public static void main(String[] args) {
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
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		
	}

}