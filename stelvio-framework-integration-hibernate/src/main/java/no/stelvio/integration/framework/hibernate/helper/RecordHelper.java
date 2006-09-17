package no.stelvio.integration.framework.hibernate.helper;

import java.util.Iterator;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.RootClass;
import net.sf.hibernate.mapping.Set;
import net.sf.hibernate.mapping.Table;
import net.sf.hibernate.type.ManyToOneType;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.integration.framework.hibernate.cfg.Configuration;
import no.stelvio.integration.framework.hibernate.mapping.Column;
import no.stelvio.integration.framework.hibernate.mapping.OneToMany;


/**
 * This class is a helper class to help with mapping
 * between cics commarea and persistent classes in java.
 * 
 * @author person5b7fd84b3197, Accenture
 * 
 */
public final class RecordHelper {

	/**
	 * Should not be instantiated.
	 */
	private RecordHelper() {
	}

	/**
	 * This method will get the column for a give property for a given class.
	 * 
	 * @param clazz the RootClass of the configuration.
	 * @param property the name of the property in the class.
	 * @return Column the column object for the property.
	 */
	public static Column getColumn(RootClass clazz, String property) {
		Property prop;

		try {
			prop = clazz.getProperty(property);
		} catch (MappingException e) {
			throw new SystemException(FrameworkError.JCA_MAPPING_ERROR, e, new String[] { clazz.getName(), property});
		}

		Iterator iter = prop.getColumnIterator();
		Column col = null;

		if (iter.hasNext()) {
			col = (Column) iter.next();
		}

		return col;
	}

	/**
	 * Getter for OneToMany relationships in the configuration.
	 * <p>
	 * Example of the role:
	 * <code>no.trygdeeteten.Parent.child</code> where child is the name.
	 * 
	 * @param config the hibernate configuration for JCA record.
	 * @param role the role name of the relationship.
	 * @return OneToMany OneToMany config class.
	 */
	public static OneToMany getOneToMany(Configuration config, String role) {
		Set set = (Set) config.getCollectionMapping(role);

		return (OneToMany) set.getElement();
	}

	/**
	 * Getter for the child class of a oneToMany relation.
	 * 
	 * @param relation OneToMany relation.
	 * @return String the relation class name.
	 */
	public static String getChildClass(OneToMany relation) {
		ManyToOneType type = (ManyToOneType) relation.getType();

		return type.getAssociatedClass().getName();
	}

	/**
	 * Generator for role name, role name is defined in Hibernate config.
	 * 
	 * @param parentClass a class which have a child.
	 * @param setName the name of the configured set attribute.
	 * @return String the role name.
	 */
	public static String getRoleName(RootClass parentClass, String setName) {
		String clazz = parentClass.getName();

		return clazz + '.' + setName;
	}

	/**
	 * This method will get the class for a given service.
	 * The service name represent the table in the Hibarnate mapping file.
	 * 
	 * @param config the hibernate configuration.
	 * @param service the service to get RootClass for.
	 * @return RootClass the class of the given service.
	 */
	public static RootClass getServiceRootClass(Configuration config, String service) {
		for (Iterator iterator = config.getClassMappings(); iterator.hasNext();) {
			RootClass rootClass = (RootClass) iterator.next();
			Table tab = rootClass.getTable();

			if (service.equals(tab.getName())) {
				return rootClass;
			}
		}

		// Should never occur
		return null;
	}

	/**
	 * This method gets a Hibernate RootClass.
	 * This is done using the fully qualified classname of the class.
	 * 
	 * @param config the hibernate config.
	 * @param clazz the class name.
	 * @return RootClass the hibernate rootclass.
	 */
	public static RootClass getRootClass(Configuration config, String clazz) {
		for (Iterator iterator = config.getClassMappings(); iterator.hasNext();) {
			RootClass rootClass = (RootClass) iterator.next();
			String name = rootClass.getMappedClass().getName();

			if (clazz.equals(name)) {
				return rootClass;
			}
		}

		// Should never occur
		return null;
	}

	/**
	 * This method will get the  method will get a given property.
	 * The property will be found using the corresponding column name form the configuration.
	 * 
	 * @param clazz the RootClass to find the property in.
	 * @param column the column to get property for.
	 * @return Property the corresponding property object.
	 */
	public static Property getProperty(RootClass clazz, String column) {
		for (Iterator propertyIterator = clazz.getPropertyIterator(); propertyIterator.hasNext();) {
			Property property = (Property) propertyIterator.next();
			Iterator columnIterator = property.getColumnIterator();

			if (columnIterator.hasNext()) {
				Column col = (Column) columnIterator.next();

				if (col.getName().equals(column)) {
					return property;
				}
			}
		}

		// Should never occur
		return null;
	}

	/**
	 * This method will find the length of a given record.
	 * The method will look in the configuration given and find
	 * the length of a record.
	 * 
	 * @param config the hibernate configuration.
	 * @param tableName the table to search in.
	 * @return int total length of record.
	 */
	public static int getRecordLength(Configuration config, String tableName) {
		for (Iterator iterator = config.getTableMappings(); iterator.hasNext();) {
			Table tab = (Table) iterator.next();

			if (tab.getName().equals(tableName)) {
				int colNum = tab.getColumnSpan();
				Column col = (Column) tab.getColumn(colNum);

				return col.getLength() + col.getOffset();
			}
		}

		// Should never occur
		return 0;
	}
}
