package no.nav.bpchelper.query;

import java.util.ArrayList;
import java.util.List;

public class Criteria {
	private List<Criterion> criterions = new ArrayList<Criterion>();
	private Integer resultRowLimit = null;

	public Criteria add(Criterion criterion) {
		criterions.add(criterion);
		return this;
	}

	public String toSqlString() {
		StringBuilder sb = new StringBuilder();
		for (Criterion criterion : criterions) {
			if (sb.length() > 0) {
				sb.append(" AND ");
			}
			sb.append("(").append(criterion.toSqlString()).append(")");
		}
		return sb.toString();
	}

	public Integer getResultRowLimit() {	
		return resultRowLimit;
	}

	public void setResultRowLimit(int limitCountInt) {
		resultRowLimit = limitCountInt;
	}
}
