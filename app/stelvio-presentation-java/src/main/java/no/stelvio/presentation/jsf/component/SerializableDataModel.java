/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package no.stelvio.presentation.jsf.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

/**
 * This class is a copy of the HtmlDataTable class found in Tomahawk v 1.1.6 It is needed by
 * no.nav.presentation.pensjon.common.jsf.componentHtmlDataTableHack which has been applied a patch for the bug reported in
 * TOMAHAWK-961.
 * 
 * @author person6045563b8dec
 * 
 * 
 * Provide a serializable equivalent of the standard DataModel classes.
 * <p>
 * The standard JSF UIData components accept a DataModel as the "value" for the ordered sequence of objects to be rendered in
 * the table. Various types (List, array, ResultSet) are also accepted and automatically wrapped in one of the standard
 * DataModel classes.
 * <p>
 * The standard DataModel classes are not Serializable by default, because there is no state in the class which needs to be
 * preserved between render and postback. And the standard UIData components don't serialize the data model object, just the EL
 * expression for the "value" attribute; the data itself is refetched when needed by re-evaluating the EL expression.
 * <p>
 * However there can be good reasons to serialize the list of data that is <i>wrapped</i> by the DataModel along with the
 * UIData component. For these cases, the tomahawk t:dataTable component offers a "preserveDataModel" flag that will
 * automatically serialize the data model along with the HtmlDataTable component; it does this by invoking the "value" binding
 * of the t:dataTable then creating an instance of this class or one of its subclasses instead of the standard JSF DataModels.
 * <p>
 * This class performs two roles. It is the base implementation for specialised classes that wrap various datatypes that can be
 * returned from the table's "value" binding. It also implements the case where the value object returned is of type DataModel.
 * <p>
 * When the UIData's "value" binding returns a DataModel instance, this class extracts each rowData object from the wrapped data
 * of the original DataModel and adds these objects to an instance of this class which <i>is</i> Serializable. Of course the
 * rowdata objects must be serializable for this to work. As a side-effect, however, the original DataModel object will be
 * discarded, and replaced by an instance of this class. This means that any special optimisations or behaviour of the concrete
 * DataModel subclass will be lost.
 * 
 * @author person055361994206 (latest modification by $Author: grantsmith $)
 * @version $Revision: 472630 $ $Date: 2006-11-08 21:40:03 +0100 (Mi, 08 Nov 2006) $
 */
class SerializableDataModel extends DataModel implements Serializable {
	private static final long serialVersionUID = -3511848078295893064L;

	/** First. */
	protected int first;

	/** Rows. */
	protected int rows;

	/** Row count. */
	protected int rowCount;

	/** List. */
	protected List list;

	private transient int rowIndex = -1;

	/**
	 * Creates a new instance of SerializableDataModel.
	 *
	 * @param first first
	 * @param rows rows 
	 * @param dataModel datamodel
	 */
	public SerializableDataModel(int first, int rows, DataModel dataModel) {
		this.first = first;
		this.rows = rows;
		this.rowCount = dataModel.getRowCount();
		if (this.rows <= 0) {
			this.rows = this.rowCount - first;
		}
		this.list = new ArrayList(rows);
		for (int i = 0; i < this.rows; i++) {
			dataModel.setRowIndex(this.first + i);
			if (!dataModel.isRowAvailable()) {
				break;
			}
			this.list.add(dataModel.getRowData());
		}
		this.rowIndex = -1;

		DataModelListener[] dataModelListeners = dataModel.getDataModelListeners();
		for (int i = 0; i < dataModelListeners.length; i++) {
			DataModelListener dataModelListener = dataModelListeners[i];
			addDataModelListener(dataModelListener);
		}
	}

	/**
	 * Creates a new instance of SerializableDataModel.
	 */
	protected SerializableDataModel() {
	}

	/**
	 * Get first.
	 * 
	 * @return first
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * Set first.
	 * 
	 * @param first first
	 */
	public void setFirst(int first) {
		this.first = first;
	}

	/**
	 * Get rows.
	 * 
	 * @return rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Set rows.
	 * 
	 * @param rows rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * {@inheritDoc} 
	 */
	public boolean isRowAvailable() {
		return rowIndex >= first && rowIndex < first + rows && rowIndex < rowCount && list.size() > rowIndex - first;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getRowData() {
		if (!isRowAvailable()) {
			throw new IllegalStateException("row not available");
		}
		return list.get(rowIndex - first);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRowIndex(int rowIndex) {
		if (rowIndex < -1) {
			throw new IllegalArgumentException();
		}

		int oldRowIndex = this.rowIndex;
		this.rowIndex = rowIndex;
		if (oldRowIndex != this.rowIndex) {
			Object data = isRowAvailable() ? getRowData() : null;
			DataModelEvent event = new DataModelEvent(this, this.rowIndex, data);
			DataModelListener[] listeners = getDataModelListeners();
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].rowSelected(event);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getWrappedData() {
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWrappedData(Object obj) {
		if (obj != null) {
			throw new IllegalArgumentException("Cannot set wrapped data of _SerializableDataModel");
		}
	}
}
