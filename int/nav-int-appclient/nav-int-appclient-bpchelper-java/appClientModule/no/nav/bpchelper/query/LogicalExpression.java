package no.nav.bpchelper.query;

public class LogicalExpression implements Criterion {
	private Criterion lhs;

	private Criterion rhs;

	private String operator;

	protected LogicalExpression(Criterion lhs, Criterion rhs, String operator) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.operator = operator;
	}

	public String toSqlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(lhs.toSqlString()).append(")");
		sb.append(" ").append(operator).append(" ");
		sb.append("(").append(rhs.toSqlString()).append(")");
		return sb.toString();
	}
}
