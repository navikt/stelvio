package no.nav.bpchelper.query;

import java.util.ArrayList;
import java.util.List;

public class Criteria {
	private List<Criterion> criterions = new ArrayList<Criterion>();
	
	public Criteria add(Criterion criterion) {
		criterions.add(criterion);
		return this;
	}
	
	public String toSqlString() {
		StringBuffer sb = new StringBuffer();
		for (Criterion criterion: criterions) {
			if (sb.length() > 0) {
				sb.append(" AND ");
			}
			sb.append("(").append(criterion.toSqlString()).append(")");
		}
		return sb.toString();
	}
}
