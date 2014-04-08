package no.nav.bpchelper.actions;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.EJBException;

import no.nav.bpchelper.readers.InputReader;

import com.ibm.bpe.api.ArchiveUnsupportedOperationException;
import com.ibm.bpe.api.BusinessFlowManagerService;
import com.ibm.bpe.api.ClientObjectWrapper;
import com.ibm.bpe.api.EngineNotAuthorizedException;
import com.ibm.bpe.api.EngineParameterNullException;
import com.ibm.bpe.api.IdWrongFormatException;
import com.ibm.bpe.api.IdWrongTypeException;
import com.ibm.bpe.api.InvalidLengthException;
import com.ibm.bpe.api.ObjectDoesNotExistException;
import com.ibm.bpe.api.PIID;
import com.ibm.bpe.api.PTID;
import com.ibm.bpe.api.ProcessException;
import com.ibm.bpe.api.UnexpectedFailureException;
import commonj.sdo.DataObject;

public class SamordningAction extends AbstractAction {

	@Override
	public String getName() {
		return "samordning";
	}

	@Override
	public int execute() {

		String processTemplate = "iverksettVedtakBP";
		String instanceName = "Samordning ";
		ClientObjectWrapper cow = new ClientObjectWrapper();
		InputReader reader = new InputReader();
		ArrayList<String> vedtak;
		int processCount = 0;

		vedtak = reader.getContent(getInputFile());
		BusinessFlowManagerService bfm = getBFMConnection()
				.getBusinessFlowManagerService().getAdaptee();

		PTID templateID;

		try {
			templateID = bfm.getProcessTemplate(processTemplate).getID();

			String processInputType = bfm.getProcessTemplate(templateID)
					.getInputMessageTypeName();

			for (String vedtakId : vedtak) {

				cow = bfm.createMessage(templateID, processInputType);

				DataObject input = (DataObject) cow.getObject();
				DataObject request = input.createDataObject("request");
				request.setString("vedtakId", vedtakId);
				DataObject tjenestedirektiv = request
						.createDataObject("tjenestedirektiv");
				tjenestedirektiv.setString("prioritet", "ONLINE");

				PIID piid = bfm.initiateAndSuspend(processTemplate,
						instanceName + vedtakId, cow);

				if (logger.isDebugEnabled()) {
					logger.debug("Processing process with id=<{}>.", piid);
				}
				
				bfm.skip(piid.toString(), "S150-Vedtak.SettTilIverksettelsesdato");
				bfm.skip(piid.toString(), "S550-Vedtak.LagreVedtakstatus3");
				bfm.skip(piid.toString(), "S560-Samordning.OpprettVedtakSamordning");

				// set variable in samordning response to make sure the instance will go into wait mode 
				ClientObjectWrapper samordneVedtakResponse = new ClientObjectWrapper();
				samordneVedtakResponse = bfm.createMessage(templateID, "samordneVedtakResponse");
				DataObject sor = (DataObject) samordneVedtakResponse.getObject();
				sor.setBoolean("ventPaaSvar", true);
				bfm.setVariable(piid.toString(), "S560-Samordning.OpprettVedtakSamordning", "samordneVedtakResponse", samordneVedtakResponse);

				bfm.resume(piid);
				processCount++;
			}
		} catch (EngineNotAuthorizedException e) {
			e.printStackTrace();
		} catch (EngineParameterNullException e) {
			e.printStackTrace();
		} catch (IdWrongTypeException e) {
			e.printStackTrace();
		} catch (InvalidLengthException e) {
			e.printStackTrace();
		} catch (ObjectDoesNotExistException e) {
			e.printStackTrace();
		} catch (UnexpectedFailureException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (IdWrongFormatException e) {
			e.printStackTrace();
		} catch (ArchiveUnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ProcessException e) {
			e.printStackTrace();
		}

		logger.info("{} process(es) started", processCount);
		return 0;
	}

}
