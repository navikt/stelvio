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

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.ExtendedComponentBase;

/**
 * Reimplement all UIData functionality to be able to have (protected) access the internal DataModel.
 * 
 * @author person055361994206 (latest modification by $Author: mkienenb $)
 * @version $Revision: 527623 $ $Date: 2007-04-11 19:25:20 +0000 (Wed, 11 Apr 2007) $
 */
public abstract class HtmlDataTableHack extends javax.faces.component.html.HtmlDataTable implements ExtendedComponentBase {
	private Map dataModelMap = new HashMap();

	// will be set to false if the data should not be refreshed at the beginning of the encode phase
	private boolean isValidChilds = true;

	// holds for each row the states of the child components of this UIData
	// This map lives only for the duration of a normal request lifecycle
	private Map rowStates = new HashMap();
	private Object lastRowIndexRowDataObject = null;

	// contains the initial row state which is used to initialize each row
	private Object initialDescendantComponentState = null;

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// Every field and method from here is identical to UIData !!!!!!!!!
	// EXCEPTION: we can create a DataModel from a Collection

	private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

	private static final boolean DEFAULT_PRESERVEROWSTATES = false;

	private int rowIndex = -1;
	private Boolean forceId;

	private Boolean preserveRowStates;

	/**
	 * Return true if row available.
	 * 
	 * @return true if row available
	 */
	public boolean isRowAvailable() {
		return getDataModel().isRowAvailable();
	}

	/**
	 * Get row count.
	 * 
	 * @return row count
	 */
	public int getRowCount() {
		return getDataModel().getRowCount();
	}

	/**
	 * Get row data.
	 * 
	 * @return row data
	 */
	public Object getRowData() {
		return getDataModel().getRowData();
	}

	/**
	 * Get row index.
	 * 
	 * @return row index
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Hack since RI does not call getRowIndex().
	 * {@inheritDoc}
	 */
	public String getClientId(FacesContext context) {
		String clientId = super.getClientId(context);
		int rowIndex = getRowIndex();
		if (rowIndex == -1) {
			return clientId;
		}
		// the following code tries to avoid rowindex to be twice in the client id
		int index = clientId.lastIndexOf(NamingContainer.SEPARATOR_CHAR);
		if (index != -1) {
			String rowIndexString = clientId.substring(index + 1);
			try {
				if (Integer.parseInt(rowIndexString) == rowIndex) {
					return clientId;
				}
			} catch (NumberFormatException e) {
				return clientId + NamingContainer.SEPARATOR_CHAR + rowIndex;
			}
		}
		return clientId + NamingContainer.SEPARATOR_CHAR + rowIndex;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIData#processUpdates(javax.faces.context.FacesContext)
	 */
	public void processUpdates(FacesContext context) {
		super.processUpdates(context);
		// check if a update model error forces the render response for our data
		if (context.getRenderResponse()) {
			isValidChilds = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIData#processValidators(javax.faces.context.FacesContext)
	 */
	public void processValidators(FacesContext context) {
		super.processValidators(context);
		// check if a validation error forces the render response for our data
		if (context.getRenderResponse()) {
			isValidChilds = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIData#encodeBegin(javax.faces.context.FacesContext)
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		initialDescendantComponentState = null;
		if (isValidChilds && !hasErrorMessages(context)) {
			// Refresh DataModel for rendering:
			dataModelMap.clear();
			if (!isPreserveRowStates()) {
				rowStates.clear();
			}
		}
		super.encodeBegin(context);
	}

	/**
	 * Set preserve row states.
	 * 
	 * @param preserveRowStates preserve row state
	 */
	public void setPreserveRowStates(boolean preserveRowStates) {
		this.preserveRowStates = Boolean.valueOf(preserveRowStates);
	}

	/**
	 * Retrun true if prederve row states.
	 * 
	 * @return true if prederve row states
	 */
	public boolean isPreserveRowStates() {
		if (preserveRowStates != null) {
			return preserveRowStates.booleanValue();
		}
		ValueBinding vb = getValueBinding("preserveRowStates");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_PRESERVEROWSTATES;
	}

	/**
	 * Retrun true if has error messages.
	 * 
	 * @param context context
	 * @return true if has error messages
	 */
	protected boolean hasErrorMessages(FacesContext context) {
		for (Iterator iter = context.getMessages(); iter.hasNext();) {
			FacesMessage message = (FacesMessage) iter.next();
			if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		setRowIndex(-1);
		super.encodeEnd(context);
	}

	/**
	 * Set row index.
	 * 
	 * @param rowIndex row index
	 */
	public void setRowIndex(int rowIndex) {
		if (rowIndex < -1) {
			throw new IllegalArgumentException("rowIndex is less than -1");
		}

		if (this.rowIndex == rowIndex) {
			return;
		}

		FacesContext facesContext = getFacesContext();
		DataModel dataModel = getDataModel();

		Object rowDataBeforeRowIndexChange = null;
		if (isRowAvailable()) {
			rowDataBeforeRowIndexChange = dataModel.getRowData();
		}

		if (this.rowIndex == -1) {
			if (initialDescendantComponentState == null) {
				initialDescendantComponentState = saveDescendantComponentStates(getChildren().iterator(), false);
			}
		} else {
			if ((!isPreserveRowStates()) || (ObjectUtils.equals(rowDataBeforeRowIndexChange, lastRowIndexRowDataObject))) {
				Object key = getRowStateKeyForRow();
				Object value = saveDescendantComponentStates(getChildren().iterator(), false);
				Log log = LogFactory.getLog(getClass());
				log.info("Storing rowState for key=" + key + ", _rowIndex=" + this.rowIndex + ", rowData="
						+ (isRowAvailable() ? getRowData() : "<null>") + ", value=" + value);
				rowStates.put(key, value);
			}
		}

		this.rowIndex = rowIndex;

		dataModel.setRowIndex(rowIndex);

		String var = getVar();
		if (rowIndex == -1) {
			if (var != null) {
				facesContext.getExternalContext().getRequestMap().remove(var);
			}
		} else {
			if (var != null) {
				if (isRowAvailable()) {
					Object rowData = dataModel.getRowData();
					facesContext.getExternalContext().getRequestMap().put(var, rowData);
				} else {
					facesContext.getExternalContext().getRequestMap().remove(var);
				}
			}
		}

		lastRowIndexRowDataObject = null;
		if (isRowAvailable()) {
			lastRowIndexRowDataObject = dataModel.getRowData();
		}

		if (this.rowIndex == -1) {
			restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
		} else {
			Object key = getRowStateKeyForRow();
			Log log = LogFactory.getLog(getClass());
			Object rowState = rowStates.get(key);
			log.info("Fetching rowState for key=" + key + ", _rowIndex=" + this.rowIndex + ", rowData="
					+ (isRowAvailable() ? getRowData() : "<null>") + ", got " + rowState);
			if (rowState == null) {
				restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
			} else {
				restoreDescendantComponentStates(getChildren().iterator(), rowState, false);
			}
		}
	}

	/**
	 * Restore descendant component states.
	 * 
	 * @param childIterator iterator
	 * @param state state
	 * @param restoreChildFacets restore child facets
	 */
	protected void restoreDescendantComponentStates(Iterator childIterator, Object state, boolean restoreChildFacets) {
		Iterator descendantStateIterator = null;
		while (childIterator.hasNext()) {
			if (descendantStateIterator == null && state != null) {
				descendantStateIterator = ((Collection) state).iterator();
			}
			UIComponent component = (UIComponent) childIterator.next();
			// reset the client id (see spec 3.1.6)
			component.setId(component.getId());
			if (!component.isTransient()) {
				Object childState = null;
				Object descendantState = null;
				if (descendantStateIterator != null && descendantStateIterator.hasNext()) {
					Object[] object = (Object[]) descendantStateIterator.next();
					childState = object[0];
					descendantState = object[1];
				}
				if (childState != null && component instanceof EditableValueHolder) {
					((EditableValueHolderState) childState).restoreState((EditableValueHolder) component);
				}
				Iterator childsIterator;
				if (restoreChildFacets) {
					childsIterator = component.getFacetsAndChildren();
				} else {
					childsIterator = component.getChildren().iterator();
				}
				restoreDescendantComponentStates(childsIterator, descendantState, true);
			}
		}
	}

	/**
	 * Save descendant componenty state.
	 * 
	 * @param childIterator iteraTOR
	 * @param saveChildFacets save child facets
	 * @return child states
	 */
	protected Object saveDescendantComponentStates(Iterator childIterator, boolean saveChildFacets) {
		Collection childStates = null;
		while (childIterator.hasNext()) {
			if (childStates == null) {
				childStates = new ArrayList();
			}
			UIComponent child = (UIComponent) childIterator.next();
			if (!child.isTransient()) {
				Iterator childsIterator;
				if (saveChildFacets) {
					childsIterator = child.getFacetsAndChildren();
				} else {
					childsIterator = child.getChildren().iterator();
				}
				Object descendantState = saveDescendantComponentStates(childsIterator, true);
				Object state = null;
				if (child instanceof EditableValueHolder) {
					state = new EditableValueHolderState((EditableValueHolder) child);
				}
				childStates.add(new Object[] { state, descendantState });
			}
		}
		return childStates;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValueBinding(String name, ValueBinding binding) {
		if (name == null) {
			throw new NullPointerException("name");
		} else if (name.equals("value")) {
			dataModelMap.clear();
		} else if (name.equals("var") || name.equals("rowIndex")) {
			throw new IllegalArgumentException(
					"You can never set the 'rowIndex' or the 'var' attribute as a value-binding. " 
					+ "Set the property directly instead. Name " + name);
		}
		super.setValueBinding(name, binding);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIData#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		super.setValue(value);
		dataModelMap.clear();
		rowStates.clear();
		isValidChilds = true;
	}

	/**
	 * Get datamodel.
	 * 
	 * @return datamodel
	 */
	protected DataModel getDataModel() {
		DataModel dataModel = null;
		String clientID = "";

		UIComponent parent = getParent();
		if (parent != null) {
			clientID = parent.getClientId(getFacesContext());
		}
		dataModel = (DataModel) dataModelMap.get(clientID);
		if (dataModel == null) {
			dataModel = createDataModel();
			dataModelMap.put(clientID, dataModel);
		}

		return dataModel;
	}

	/**
	 * Set datamodel.
	 * 
	 * @param datamodel datamodel
	 */
	protected void setDataModel(DataModel datamodel) {
		UIComponent parent = getParent();
		String clientID = "";
		if (parent != null) {
			clientID = parent.getClientId(getFacesContext());
		}
		dataModelMap.put(clientID, datamodel);
	}

	/**
	 * Creates a new DataModel around the current value.
	 * 
	 * @return datamodel
	 */
	protected DataModel createDataModel() {
		Object value = getValue();
		if (value == null) {
			return EMPTY_DATA_MODEL;
		} else if (value instanceof DataModel) {
			return (DataModel) value;
		} else if (value instanceof List) {
			return new ListDataModel((List) value);
		} else if (value instanceof Collection) { // accept a Collection is not supported in the Spec
			return new ListDataModel(new ArrayList((Collection) value));
		} else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass())) {
			return new ArrayDataModel((Object[]) value);
		} else if (value instanceof ResultSet) {
			return new ResultSetDataModel((ResultSet) value);
		} else if (value instanceof Result) {
			return new ResultDataModel((Result) value);
		} else {
			return new ScalarDataModel(value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = preserveRowStates;
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		preserveRowStates = (Boolean) values[1];
	}

	/**
	 * Empty datamodel.
	 */
	private static final DataModel EMPTY_DATA_MODEL = new SerializableDataModel() {
		public boolean isRowAvailable() {
			return false;
		}

		public int getRowCount() {
			return 0;
		}

		public Object getRowData() {
			throw new IllegalArgumentException();
		}

		public int getRowIndex() {
			return -1;
		}

		public void setRowIndex(int i) {
			if (i < -1) {
				throw new IndexOutOfBoundsException("Index < 0 : " + i);
			}
		}

		public Object getWrappedData() {
			return null;
		}

		public void setWrappedData(Object obj) {
			if (obj == null) {
				return; // Clearing is allowed
			}
			throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
		}
	};

	/**
	 * EditableValueHolderState.
	 */
	private class EditableValueHolderState {
		private final Object value;
		private final boolean localValueSet;
		private final boolean valid;
		private final Object submittedValue;

		/**
		 * Creates a new instance of EditableValueHolderState.
		 *
		 * @param evh editable value holder
		 */
		public EditableValueHolderState(EditableValueHolder evh) {
			value = evh.getLocalValue();
			localValueSet = evh.isLocalValueSet();
			valid = evh.isValid();
			submittedValue = evh.getSubmittedValue();
		}

		/**
		 * Restore state.
		 * 
		 * @param evh  editable value holder
		 */
		public void restoreState(EditableValueHolder evh) {
			evh.setValue(value);
			evh.setLocalValueSet(localValueSet);
			evh.setValid(valid);
			evh.setSubmittedValue(submittedValue);
		}
	}

	/**
	 * Set forceid.
	 * 
	 * @param b forceid
	 */
	public void setForceId(boolean b) {
		forceId = Boolean.valueOf(b);
	}

	/**
	 * Is force id.
	 * 
	 * @return forceid
	 */
	public boolean isForceId() {
		if (forceId != null) {
			return forceId.booleanValue();
		}
		ValueBinding vb = getValueBinding("forceId");
		return vb != null && booleanFromObject(vb.getValue(getFacesContext()), false);
	}

	/**
	 * Get boolean from object.
	 * 
	 * @param obj object
	 * @param defaultValue default value
	 * @return boolean value of obj
	 */
	private static boolean booleanFromObject(Object obj, boolean defaultValue) {
		if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		} else if (obj instanceof String) {
			return Boolean.valueOf(((String) obj)).booleanValue();
		}

		return defaultValue;
	}

	/**
	 * Get row state key for row.
	 * 
	 * @return rowstatekey for row
	 */
	public Object getRowStateKeyForRow() {
		if (isRowAvailable()) {
			return getRowData();
		}

		// Default back to the old behavior
		return getClientId(FacesContext.getCurrentInstance());
	}

	/**
	 * Remove all preserved row state for the dataTable.
	 */
	public void clearRowStates() {
		rowStates.clear();
	}

	/**
	 * Remove preserved row state for deleted row and adjust row state to reflect deleted row.
	 * 
	 * @param deletedIndex
	 *            index of row to delete
	 */
	public void deleteRowStateForRow(int deletedIndex) {
		// save row index
		int savedRowIndex = getRowIndex();

		setRowIndex(deletedIndex);
		Object currentRowStateKey = getRowStateKeyForRow();

		// copy next rowstate to current row for each row from deleted row onward.
		int rowCount = getRowCount();
		for (int index = deletedIndex + 1; index < rowCount; ++index) {
			setRowIndex(index);
			Object nextRowStateKey = getRowStateKeyForRow();

			Object nextRowState = rowStates.get(nextRowStateKey);
			if (nextRowState == null) {
				rowStates.remove(currentRowStateKey);
			} else {
				rowStates.put(currentRowStateKey, nextRowState);
			}
			currentRowStateKey = nextRowStateKey;
		}

		// Remove last row
		rowStates.remove(currentRowStateKey);

		// restore saved row index
		setRowIndex(savedRowIndex);
	}

}
