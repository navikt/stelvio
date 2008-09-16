package no.nav.bpchelper.query;

public class Restrictions {
	private Restrictions() {
	}
	
	public static SimpleExpression eq(String columnName, Object value) {
		return new SimpleExpression(columnName,value, " = ");
	}
	
	public static SimpleExpression le(String columnName, Object value) {
		return new SimpleExpression(columnName,value, " <= ");
	}
	
	public static SimpleExpression ge(String columnName, Object value) {
		return new SimpleExpression(columnName,value, " >= ");
	}
	
	public static SimpleExpression like(String columnName, Object value) {
		return new SimpleExpression(columnName,value, " LIKE ");
	}
	
	public static LogicalExpression or(Criterion lhs, Criterion rhs) {
		return new LogicalExpression(lhs, rhs, " OR ");
	}
}
