package no.stelvio.common.codestable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Simple test class for testing persisting & loading an enum.
 *
 * @author personf8e9850ed756, Accenture
 */
@Entity
public class TestPersistenceWithEnums {
	private Integer id;
	
	@Id
	@Enumerated(EnumType.STRING)
	private TestCtiCode cc;

	protected TestPersistenceWithEnums() { }

	public TestPersistenceWithEnums(Integer id, TestCtiCode cc) {
		this.id = id;
		this.cc = cc;
	}
}
