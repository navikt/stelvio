package no.stelvio.web.taglib.tasklist;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.tasklist.domain.Area;
import no.stelvio.common.tasklist.domain.Responsible;
import no.stelvio.common.tasklist.domain.TaskType;
import no.stelvio.common.tasklist.repository.TasklistTreeUtil;

/**
 * TODO: Document me
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class TasklistTreeUtilImpl implements TasklistTreeUtil {
	public List<Responsible> getTaskTreeModel(String responsibleId) {
		List<Responsible> responsibles = new ArrayList<Responsible>();
		
		// Responsible
		Responsible r1 = new Responsible();
		r1.setId("1");
		r1.setDescription("Mine oppgaver");
		
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
		Responsible r2 = new Responsible();
		r2.setId("4");
		r2.setDescription("Nav Eidfjord");
		
		// Area
		List<Area> areas2 = new ArrayList<Area>();
		Area a2 = new Area();
		a2.setId("4");
		a2.setDescription("Bidrag");
		
		// Tasktype
		List<TaskType> taskTypes2 = new ArrayList<TaskType>();
		TaskType t2 = new TaskType();
		t2.setId("4");
		t2.setDescription("Skatt/trekk");
		taskTypes2.add(t2);
		a2.setTaskTypes(taskTypes2);
		
		// Area
		Area a3 = new Area();
		a3.setId("5");
		a3.setDescription("Pensjon");
		
		// Tasktype
		List<TaskType> taskTypes3 = new ArrayList<TaskType>();
		TaskType t3 = new TaskType();
		t3.setId("4");
		t3.setDescription("Simuleringskontroll");
		taskTypes3.add(t3);
		
		TaskType t4 = new TaskType();
		t4.setId("6");
		t4.setDescription("Klage/anke");
		taskTypes3.add(t4);
		
		a3.setTaskTypes(taskTypes3);
		
		areas2.add(a2);
		areas2.add(a3);
		
		r2.setAreas(areas2);
		
		
		responsibles.add(r1);
		responsibles.add(r2);
		return responsibles;
	}
}