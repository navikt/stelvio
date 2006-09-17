package no.stelvio.integration.framework.hibernate.helper;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sf.hibernate.mapping.Property;
import net.sf.hibernate.mapping.RootClass;
import net.sf.hibernate.type.OneToOneType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import no.stelvio.common.framework.performance.MonitorKey;
import no.stelvio.common.framework.performance.PerformanceMonitor;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.util.ReflectUtil;
import no.stelvio.common.framework.util.StringHelper;
import no.stelvio.integration.framework.hibernate.cfg.Configuration;
import no.stelvio.integration.framework.hibernate.formater.Formater;
import no.stelvio.integration.framework.hibernate.mapping.Column;
import no.stelvio.integration.framework.hibernate.mapping.OneToMany;
import no.stelvio.integration.framework.hibernate.mapping.OneToOne;

/**
 * This class converts from and to cics records.
 *
 * @author person5b7fd84b3197, Accenture
 * @version $Id: RecordConverterImpl.java 2802 2006-03-01 11:38:08Z skb2930 $
 */
public final class RecordConverterImpl implements BeanFactoryAware, RecordConverter {
	/** Logging */
	private static final Log LOG = LogFactory.getLog("no.stelvio.integration.framework.hibernate.helper.RecordConverter");
	private static final MonitorKey TORECORD = new MonitorKey("RecordConverter.classToRecord", MonitorKey.ADDITIONAL);
	private static final MonitorKey TOCLASS = new MonitorKey("RecordConverter.recordToClass", MonitorKey.ADDITIONAL);
	private static final String SET = "java.util.Set";
	private BeanFactory beanFactory;

	/**
	 * This method converts a string record to a bean class.
	 *
	 * @param config - The hibernate configuration
	 * @param record - The record
	 * @param rootClass - The rootClass of the configuration
	 * @return - The generated class
	 * @throws ServiceFailedException - If error in creating class
	 */
	public Object recordToClass(Configuration config, String record, RootClass rootClass)
			throws ServiceFailedException {
		PerformanceMonitor.start(TOCLASS);
		Iterator propertyIterator = rootClass.getPropertyIterator();
		// The container for the value to store on the class
		Object value = null;
		// Get the persistent class
		Class clazz = rootClass.getMappedClass();

		boolean runRecursive = true;
		// Get the instance of class to convert the record to
		Object instance = ReflectUtil.getNewInstance(clazz);
		// Loop over all properties on configured record
		while (propertyIterator.hasNext()) {
			Property prop = (Property) propertyIterator.next();

			if (LOG.isDebugEnabled()) {
				LOG.debug("recordToClass - Working Property: " + prop.getName());
			}
			// IF this check is true then the record has a child
			if (prop.getType().getReturnedClass().getName().equals(SET)) {
				// Call recursive
				String role = RecordHelper.getRoleName(rootClass, prop.getName());

				OneToMany otm = RecordHelper.getOneToMany(config, role);
				String relation = RecordHelper.getChildClass(otm);

				int offset = otm.getOffset();
				String countColumn = otm.getCountColumn();
				// Get the counter property
				Property conterProperty = RecordHelper.getProperty(rootClass, countColumn);

				String counterPropName = conterProperty.getName();

				Object val = ReflectUtil.getPropertyFromClass(instance, counterPropName);
				RootClass childClass = RecordHelper.getRootClass(config, relation);
				String childTableName = childClass.getTable().getName();
				// Get the child record
				int numRec = Integer.parseInt(val.toString());

				int oneRecordLength = RecordHelper.getRecordLength(config, childTableName);

				if (LOG.isDebugEnabled()) {
					LOG.debug(
							"recordToClass - role:"
							+ role
							+ ";relation:"
							+ relation
							+ ";countColumn:"
							+ countColumn
							+ ";childTableName:"
							+ childTableName);
				}
				int off = offset;
				Set childs = new LinkedHashSet();
				for (int i = 0; i < numRec; i++) {
					String childRecord = StringHelper.getProperty(oneRecordLength, off, record);
					off = off + oneRecordLength;
					Object obj = recordToClass(config, childRecord, childClass);
					if (LOG.isDebugEnabled()) {
						LOG.debug("recordToClass - Return from recursive. Object Returned: " + obj);
					}
					childs.add(obj);
				}

				value = childs;
				runRecursive = false;
			} else if (prop.getType().getClass() == OneToOneType.class) {
				OneToOne oto = (OneToOne) prop.getValue();
				String subRecord = StringHelper.getProperty(oto.getLength(), oto.getOffset(), record);
				RootClass childClass = RecordHelper.getRootClass(config, oto.getType().getReturnedClass().getName());
				value = recordToClass(config, subRecord, childClass);
				runRecursive = false;
			}

			if (runRecursive) {
				Column col = RecordHelper.getColumn(rootClass, prop.getName());
				value = StringHelper.getProperty(col.getLength(), col.getOffset(), record);

				String formater = col.getFormater();

				// Run formater
				if (StringUtils.isNotBlank(formater)) {
					Formater forMater = (Formater) beanFactory.getBean(formater);
					value = forMater.formatOutput(value.toString());
				}
			}


			ReflectUtil.setPropertyOnClass(value, instance, prop.getName());

			// set back if more child records in this record
			runRecursive = true;
		}
		PerformanceMonitor.end(TOCLASS);
		return instance;
	}

	/**
	 * This method converts a bean class to a record form a given configuration. The method will get the properties form
	 * the been and append them into a string.
	 *
	 * @param config - The Hibernate configuration
	 * @param rootClass - The Hibernate root class of the configuration
	 * @param instance - Instance of the property bean
	 * @param buff - A new StringBuffer to be used in method
	 * @return - Returns the properties appended in a string
	 * @throws ServiceFailedException - If error in creating record
	 */
	public String classToRecord(Configuration config, RootClass rootClass, Object instance, StringBuffer buff)
			throws ServiceFailedException {

		PerformanceMonitor.start(TORECORD);
		Iterator propertyIterator = rootClass.getPropertyIterator();

		boolean runRecursive = true;

		while (propertyIterator.hasNext()) {
			Property prop = (Property) propertyIterator.next();
			if (LOG.isDebugEnabled()) {
				LOG.debug("classToRecord - WorkingProperty: " + prop.getName());
			}

			// IF this check is true then the record has a child
			if (prop.getType().getReturnedClass().getName().equals(SET)) {

				String role = RecordHelper.getRoleName(rootClass, prop.getName());

				OneToMany otm = RecordHelper.getOneToMany(config, role);
				String relation = RecordHelper.getChildClass(otm);

				int length = otm.getLength();

				RootClass childClass = RecordHelper.getRootClass(config, relation);

				Set set = (Set) ReflectUtil.getPropertyFromClass(instance, prop.getName());
				if (null == set) {
					// use LinkedHashSet to guarantee iteration order
					set = new LinkedHashSet();
				}
				Iterator iter = set.iterator();
				while (iter.hasNext()) {
					// get the child instance
					Object obj = iter.next();
					classToRecord(config, childClass, obj, buff);

				}

				int totLen = length - (buff.length() - otm.getOffset());

				StringHelper.appendProperty(totLen, null, buff, "string");

				runRecursive = false;

			} else if (prop.getType().getClass() == OneToOneType.class) {
				// NB! We will fill this with empty spaces since there is no need for ONE-TO-ONE in input
				OneToOne oto = (OneToOne) prop.getValue();
				RootClass childClass = RecordHelper.getRootClass(config, oto.getType().getReturnedClass().getName());
				Object obj = ReflectUtil.getPropertyFromClass(instance, prop.getName());
				if (obj == null) {
					StringHelper.appendProperty(oto.getLength(), null, buff, "string");
				} else {
					classToRecord(config, childClass, obj, buff);
				}
				runRecursive = false;
			}

			if (runRecursive) {

				Object value = ReflectUtil.getPropertyFromClass(instance, prop.getName());

				Column col = RecordHelper.getColumn(rootClass, prop.getName());

				String formater = col.getFormater();

				// Run formater
				if (StringUtils.isNotBlank(formater)) {
					Formater forMater = (Formater) beanFactory.getBean(formater);

					if (LOG.isDebugEnabled()) {
						LOG.debug("Input value to formater '" + formater + "' is: '" + value + "'.");
					}

					value = forMater.formatInput(value);
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug(
							"Appending property: "
							+ prop.getName()
							+ " with value: "
							+ value
							+ " at offset: "
							+ col.getOffset()
							+ " with length: "
							+ col.getLength());
				}

				StringHelper.appendProperty(col.getLength(), value, buff, prop.getType().getName());
			}

			runRecursive = true;
		}

		// Find one-to-one relation
		PerformanceMonitor.end(TORECORD);
		return buff.toString();
	}

	/**
	 * Callback that supplies the owning factory to the bean instance.
	 * <p>
	 * Invoked after population of normal bean properties but before an init callback like InitializingBean's afterPropertiesSet
	 * or a custom init-method.
	 *
	 * @param beanFactory owning BeanFactory (may not be null). The bean can immediately call methods on the factory.
	 * @throws BeansException in case of initialization errors
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
