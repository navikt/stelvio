package no.stelvio.web.taglib.tasklist;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.web.taglib.tasklist.util.SaksbehandlerUtil;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class SaksbehandlerUtilImpl implements SaksbehandlerUtil {
	// Dummy data
	private static Responsible r1 = new Responsible();
	private static Responsible r2 = new Responsible();
	
	// Static init-block of dummy data
	static {
		// Responsible
		r1.setId("1");
		r1.setDescription("Sindre Jacobsen");
		
		// Area
		List<Area> areas1 = new ArrayList<Area>();
		Area a1 = new Area();
		a1.setId("1");
		a1.setDescription("Pensjon");
		areas1.add(a1);
		
		// Tasktype
		List<TaskType> taskTypes1 = new ArrayList<TaskType>();
		TaskType t1 = new TaskType();
		t1.setId("1");
		t1.setDescription("Generell");
		taskTypes1.add(t1);
		a1.setTaskTypes(taskTypes1);
		
		r1.setAreas(areas1);
		
		
		
		// Responsible
		r2.setId("2");
		r2.setDescription("Fridtjof Karlsen");
		
		// Area
		List<Area> areas2 = new ArrayList<Area>();
		Area a2 = new Area();
		a2.setId("2");
		a2.setDescription("Bidrag");
		areas2.add(a2);
		
		// Tasktype
		List<TaskType> taskTypes2 = new ArrayList<TaskType>();
		TaskType t2 = new TaskType();
		t2.setId("2");
		t2.setDescription("Kontrollsaker");
		taskTypes2.add(t2);
		a2.setTaskTypes(taskTypes2);
		
		r2.setAreas(areas2);
	}
	
	/**
	 * TODO: Document me
	 */
	public List<Responsible> getSaksbehandlere(String enhetId) {
		List<Responsible> saksbehandlere = new ArrayList<Responsible>();
		saksbehandlere.add(r2);
		saksbehandlere.add(r1);
		return saksbehandlere;
	}

	/**
	 * TODO: Document me
	 */
	public Responsible getSaksbehandler(String saksbehandlerNr) {
		if (saksbehandlerNr.equalsIgnoreCase("1")) {
			return r1;
		}
		else if (saksbehandlerNr.equalsIgnoreCase("2")) {
			return r2;
		}
		
		return null;
	}
}