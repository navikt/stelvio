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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.NewspaperTable;
import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.ext.SortCriterion;
import org.apache.myfaces.component.html.ext.SortableModel;
import org.apache.myfaces.component.html.ext.UIComponentPerspective;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.custom.sortheader.HtmlCommandSortHeader;
import org.apache.myfaces.renderkit.html.ext.HtmlTableRenderer;
import org.apache.myfaces.renderkit.html.util.TableContext;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

/**
 * This class is a copy of the HtmlDataTable class found in Tomahawk v 1.1.6. It is needed by
 * no.nav.presentation.pensjon.common.jsf.componentHtmlDataTableHack which has been applied a patch for the bug reported in
 * TOMAHAWK-961.
 * 
 * @author person6045563b8dec, Accenture
 * 
 * @author personc7e15cb5f7a6 (latest modification by $Author: mmarinschek $)
 * @author person055361994206
 * @version $Revision: 483569 $ $Date: 2006-12-07 18:49:35 +0100 (Do, 07 Dez 2006) $
 */
public class HtmlDataTable extends HtmlDataTableHack implements UserRoleAware, NewspaperTable {
	private static final Log LOG = LogFactory.getLog(HtmlDataTable.class);

	private static final int PROCESS_DECODES = 1;
	private static final int PROCESS_VALIDATORS = 2;
	private static final int PROCESS_UPDATES = 3;

	private static final boolean DEFAULT_SORTASCENDING = true;
	private static final boolean DEFAULT_SORTABLE = false;
	private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

	/**
	 * the newspaperColumns property name.
	 */
	public static final String NEWSPAPER_COLUMNS_PROPERTY = "newspaperColumns";
	/**
	 * the spacer property name.
	 */
	public static final String SPACER_FACET_NAME = "spacer";
	/**
	 * the newspaperOrientation property name.
	 */
	public static final String NEWSPAPER_ORIENTATION_PROPERTY = "newspaperOrientation";

	/**
	 * the value of the column count property.
	 */
	private int newspaperColumns = 1;
	/**
	 * the value of the newspaper orientation property.
	 */
	private String newspaperOrientation = null;

	private SerializableDataModel preservedDataModel;

	private String forceIdIndexFormula = null;
	private String sortColumn = null;
	private Boolean sortAscending = null;
	private String sortProperty = null;
	private Boolean sortable = null;
	private String rowOnClick = null;
	private String rowOnDblClick = null;
	private String rowOnMouseDown = null;
	private String rowOnMouseUp = null;
	private String rowOnMouseOver = null;
	private String rowOnMouseMove = null;
	private String rowOnMouseOut = null;
	private String rowOnKeyPress = null;
	private String rowOnKeyDown = null;
	private String rowOnKeyUp = null;
	private String rowStyleClass = null;
	private String rowStyle = null;
	private String rowGroupStyle = null;
	private String rowGroupStyleClass = null;
	private String varDetailToggler = null;
	private String bodyStyleClass = null;
	private String bodyStyle = null;

	private int sortColumnIndex = -1;

	private boolean isValidChildren = true;

	private Set expandedNodes = new HashSet();

	private Map detailRowStates = new HashMap();

	private TableContext tableContext = null;

	/**
	 * Get table context.
	 * 
	 * @return table context
	 */
	public TableContext getTableContext() {
		if (tableContext == null) {
			tableContext = new TableContext();
		}
		return tableContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getClientId(FacesContext context) {
		String standardClientId = super.getClientId(context);
		int rowIndex = getRowIndex();
		if (rowIndex == -1) {
			return standardClientId;
		}

		String forcedIdIndex = getForceIdIndexFormula();
		if (forcedIdIndex == null || forcedIdIndex.length() == 0) {
			return standardClientId;
		}

		// Trick : Remove the last part starting with NamingContainer.SEPARATOR_CHAR that contains the rowIndex.
		// It would be best to not resort to String manipulation,
		// but we can't get super.super.getClientId() :-(
		int indexLast = standardClientId.lastIndexOf(NamingContainer.SEPARATOR_CHAR);
		if (indexLast == -1) {
			LOG.info("Could not parse super.getClientId. forcedIdIndex will contain the rowIndex.");
			return standardClientId + NamingContainer.SEPARATOR_CHAR + forcedIdIndex;
		}

		// noinspection UnnecessaryLocalVariable
		String parsedForcedClientId = standardClientId.substring(0, indexLast + 1) + forcedIdIndex;
		return parsedForcedClientId;
	}

	/**
	 * {@inheritDoc}
	 */
	public UIComponent findComponent(String expr) {
		if (expr.length() > 0 && Character.isDigit(expr.charAt(0))) {
			int separatorIndex = expr.indexOf(NamingContainer.SEPARATOR_CHAR);

			String rowIndexStr = expr;
			String remainingPart = null;

			if (separatorIndex != -1) {
				rowIndexStr = expr.substring(0, separatorIndex);
				remainingPart = expr.substring(separatorIndex + 1);
			}

			int rowIndex = Integer.valueOf(rowIndexStr).intValue();

			if (remainingPart == null) {
				LOG.error("Wrong syntax of expression : " + expr + " rowIndex was provided, but no component name.");
				return null;
			}

			UIComponent comp = super.findComponent(remainingPart);

			if (comp == null) {
				return null;
			}

			// noinspection UnnecessaryLocalVariable
			UIComponentPerspective perspective = new UIComponentPerspective(this, comp, rowIndex);
			return perspective;
		} else {
			return super.findComponent(expr);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRowIndex(int rowIndex) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (rowIndex < -1) {
			throw new IllegalArgumentException("rowIndex is less than -1");
		}

		UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
		/* Just for obtaining an iterator which must be passed to saveDescendantComponentStates() */
		Set set = new HashSet();
		set.add(facet);
		if (rowIndex != -1 && facet != null) {
			detailRowStates.put(getClientId(facesContext), saveDescendantComponentStates(set.iterator(), false));
		}

		String rowIndexVar = getRowIndexVar();
		String rowCountVar = getRowCountVar();
		String previousRowDataVar = getPreviousRowDataVar();
		if (rowIndexVar != null || rowCountVar != null || previousRowDataVar != null) {
			Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

			// we only need to provide the previousRowDataVar for a valid rowIndex
			if (previousRowDataVar != null && rowIndex >= 0) {
				if (isRowAvailable()) {
					// previous row is available
					requestMap.put(previousRowDataVar, getRowData());
				} else {
					// no previous row available
					requestMap.put(previousRowDataVar, null);
				}
			}

			super.setRowIndex(rowIndex);

			if (rowIndex >= 0) {
				// regular row index, update request scope variables
				if (rowIndexVar != null) {
					requestMap.put(rowIndexVar, Integer.valueOf(rowIndex));
				}

				if (rowCountVar != null) {
					requestMap.put(rowCountVar, Integer.valueOf(getRowCount()));
				}
			} else {
				// rowIndex == -1 means end of loop --> remove request scope variables
				if (rowIndexVar != null) {
					requestMap.remove(rowIndexVar);
				}

				if (rowCountVar != null) {
					requestMap.remove(rowCountVar);
				}

				if (previousRowDataVar != null) {
					requestMap.remove(previousRowDataVar);
				}
			}
		} else {
			// no extended var attributes defined, no special treatment
			super.setRowIndex(rowIndex);
		}

		if (rowIndex != -1 && facet != null) {
			Object rowState = detailRowStates.get(getClientId(facesContext));

			restoreDescendantComponentStates(set.iterator(), rowState, false);

		}

		if (varDetailToggler != null) {
			Map requestMap = getFacesContext().getExternalContext().getRequestMap();
			requestMap.put(varDetailToggler, this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processDecodes(FacesContext context) {
		if (!isRendered()) {
			return;
		}

		// We must remove and then replace the facet so that
		// it is not processed by default facet handling code
		//
		Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
		super.processDecodes(context);
		if (facet != null) {
			getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME, facet);
		}

		setRowIndex(-1);
		processColumns(context, PROCESS_DECODES);
		setRowIndex(-1);
		processDetails(context, PROCESS_DECODES);
		setRowIndex(-1);
	}

	/**
	 * Process details.
	 * 
	 * @param context
	 *            context
	 * @param processAction
	 *            process action
	 */
	private void processDetails(FacesContext context, int processAction) {
		UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);

		if (facet != null) {
			int first = getFirst();
			int rows = getRows();
			int last;
			if (rows == 0) {
				last = getRowCount();
			} else {
				last = first + rows;
			}
			for (int rowIndex = first; last == -1 || rowIndex < last; rowIndex++) {
				setRowIndex(rowIndex);

				if (!isCurrentDetailExpanded()) {
					continue;
				}

				// scrolled past the last row
				if (!isRowAvailable()) {
					break;
				}

				// If we are in the decode phase, the values restored into our
				// facet in setRowIndex() may be incorrect. This will happen
				// if some input fields are rendered in some rows, but not
				// rendered in others. In this case the input field components
				// will still contain the _submittedValue from the previous row
				// that had that input field and _submittedValue will not be set to
				// null by the process() method if there was no value submitted.
				// Thus, an invalid component state for that row will be saved in
				// _detailRowStates. The validation phase will not put a null into
				// _sumbittedValue either, b/c the component is not rendered, so
				// validation code doesn't run. This erroneous value will propagate all the way
				// to the render phase, and result in all rows on the current page being
				// rendered with the "stuck" _submittedValue, rather than evaluating the
				// value to render for every row.
				//
				// We can fix this by initializing _submittedValue of all input fields in the facet
				// to null before calling the process() method below during the decode phase.
				//
				if (PROCESS_DECODES == processAction) {
					resetAllSubmittedValues(facet);
				}

				process(context, facet, processAction);

				if (rowIndex == (last - 1)) {
					Set set = new HashSet();
					set.add(facet);
					detailRowStates.put(getClientId(FacesContext.getCurrentInstance()), saveDescendantComponentStates(set
							.iterator(), false));
				}
			}
		}
	}

	/**
	 * Reset all submitted values.
	 * 
	 * @param component
	 *            component
	 */
	private void resetAllSubmittedValues(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			((EditableValueHolder) component).setSubmittedValue(null);
		}

		for (Iterator it = component.getFacetsAndChildren(); it.hasNext();) {
			resetAllSubmittedValues((UIComponent) it.next());
		}
	}

	/**
	 * Process columns.
	 * 
	 * @param context
	 *            context
	 * @param processAction
	 *            process action
	 */
	private void processColumns(FacesContext context, int processAction) {
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			UIComponent child = (UIComponent) it.next();
			if (child instanceof UIColumns) {
				process(context, child, processAction);
			}
		}
	}

	/**
	 * Process.
	 * 
	 * @param context
	 *            context
	 * @param component
	 *            component
	 * @param processAction
	 *            process action
	 */
	private void process(FacesContext context, UIComponent component, int processAction) {
		switch (processAction) {
		case PROCESS_DECODES:
			component.processDecodes(context);
			break;
		case PROCESS_VALIDATORS:
			component.processValidators(context);
			break;
		case PROCESS_UPDATES:
			component.processUpdates(context);
			break;
		default:
			// Cannot happen
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processValidators(FacesContext context) {
		if (!isRendered()) {
			return;
		}
		// We must remove and then replace the facet so that
		// it is not processed by default facet handling code
		//
		Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
		super.processValidators(context);
		if (facet != null) {
			getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME, facet);
		}

		processColumns(context, PROCESS_VALIDATORS);
		setRowIndex(-1);
		processDetails(context, PROCESS_VALIDATORS);
		setRowIndex(-1);

		if (context.getRenderResponse()) {
			isValidChildren = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void processUpdates(FacesContext context) {
		if (!isRendered()) {
			return;
		}

		// We must remove and then replace the facet so that
		// it is not processed by default facet handling code
		//
		Object facet = getFacets().remove(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
		super.processUpdates(context);
		if (facet != null) {
			getFacets().put(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME, facet);
		}

		processColumns(context, PROCESS_UPDATES);
		setRowIndex(-1);
		processDetails(context, PROCESS_UPDATES);
		setRowIndex(-1);

		if (isPreserveDataModel()) {
			updateModelFromPreservedDataModel(context);
		}

		if (context.getRenderResponse()) {
			isValidChildren = false;
		}
	}

	/**
	 * Update model from preserved datamodel.
	 * 
	 * @param context
	 *            context
	 */
	private void updateModelFromPreservedDataModel(FacesContext context) {
		ValueBinding vb = getValueBinding("value");
		if (vb != null && !vb.isReadOnly(context)) {
			SerializableDataModel dm = (SerializableDataModel) getDataModel();
			Class type = vb.getType(context);
			if (DataModel.class.isAssignableFrom(type)) {
				vb.setValue(context, dm);
			} else if (List.class.isAssignableFrom(type)) {
				vb.setValue(context, dm.getWrappedData());
			} else if (OBJECT_ARRAY_CLASS.isAssignableFrom(type)) {
				List lst = (List) dm.getWrappedData();
				vb.setValue(context, lst.toArray(new Object[lst.size()]));
			} else if (ResultSet.class.isAssignableFrom(type)) {
				throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
			} else {
				// Assume scalar data model
				List lst = (List) dm.getWrappedData();
				if (lst.size() > 0) {
					vb.setValue(context, lst.get(0));
				} else {
					vb.setValue(context, null);
				}
			}
		}
		preservedDataModel = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}

		if (isValidChildren && !hasErrorMessages(context)) {
			preservedDataModel = null;
		}

		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			UIComponent component = (UIComponent) iter.next();
			if (component instanceof UIColumns) {
				// Merge the columns from the tomahawk dynamic component
				// into this object.
				((UIColumns) component).encodeTableBegin(context);
			}
		}

		// replace facet header content component of the columns, with a new command sort header component
		// if sortable=true, replace it for all, if not just for the columns marked as sortable
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			UIComponent component = (UIComponent) iter.next();
			if (component instanceof UIColumn) {
				UIColumn aColumn = (UIColumn) component;
				UIComponent headerFacet = aColumn.getHeader();

				boolean replaceHeaderFacets = isSortable(); // if the table is sortable, all
				// header facets can be changed if needed
				String columnName = null;
				String propertyName = null;
				boolean defaultSorted = false;

				if (aColumn instanceof HtmlSimpleColumn) {
					HtmlSimpleColumn asColumn = (HtmlSimpleColumn) aColumn;
					propertyName = asColumn.getSortPropertyName();
					defaultSorted = asColumn.isDefaultSorted();

					if (asColumn.isSortable()) {
						replaceHeaderFacets = true;
					}
				}

				// replace header facet with a sortable header component if needed
				if (replaceHeaderFacets && isSortHeaderNeeded(aColumn, headerFacet)) {
					propertyName = propertyName != null ? propertyName : getSortPropertyFromEL(aColumn);
					if (propertyName == null) {
						LOG.warn("Couldn't determine sort property for column [" + aColumn.getId() + "].");
					}

					if (headerFacet != null) {
						HtmlCommandSortHeader sortHeader = createSortHeaderComponent(context, aColumn, headerFacet,
								propertyName);
						columnName = sortHeader.getColumnName();

						aColumn.setHeader(sortHeader);
						sortHeader.setParent(aColumn);
					}
				} else if (headerFacet instanceof HtmlCommandSortHeader) {
					// command sort headers are already in place, just store the column name and sort property name
					HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) headerFacet;
					columnName = sortHeader.getColumnName();
					propertyName = sortHeader.getPropertyName();

					// if the command sort header component doesn't specify a sort property, determine it
					if (propertyName == null) {
						propertyName = getSortPropertyFromEL(aColumn);
						sortHeader.setPropertyName(propertyName);
					}

					if (propertyName == null) {
						LOG.warn("Couldn't determine sort property for column [" + aColumn.getId() + "].");
					}
				}

				// make the column marked as default sorted be the current sorted column
				if (defaultSorted && getSortColumn() == null) {
					setSortColumn(columnName);
					setSortProperty(propertyName);
				}
			}
		}

		// Now invoke the superclass encodeBegin, which will eventually
		// execute the encodeBegin for the associated renderer.
		super.encodeBegin(context);
	}

	/**
	 * Is sortheader needed.
	 * 
	 * @param parentColumn
	 *            parent column
	 * @param headerFacet
	 *            header facet
	 * @return true if sortheader needed
	 */
	protected boolean isSortHeaderNeeded(UIColumn parentColumn, UIComponent headerFacet) {
		return !(headerFacet instanceof HtmlCommandSortHeader);
	}

	/**
	 * Create sort header component.
	 * 
	 * @param context
	 *            context
	 * @param parentColumn
	 *            parent column
	 * @param initialHeaderFacet
	 *            initial header facet
	 * @param propertyName
	 *            property name
	 * @return sort header
	 */
	protected HtmlCommandSortHeader createSortHeaderComponent(FacesContext context, UIColumn parentColumn,
			UIComponent initialHeaderFacet, String propertyName) {
		Application application = context.getApplication();

		HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) application
				.createComponent(HtmlCommandSortHeader.COMPONENT_TYPE);
		String id = context.getViewRoot().createUniqueId();
		sortHeader.setId(id);
		sortHeader.setColumnName(id);
		sortHeader.setPropertyName(propertyName);
		sortHeader.setArrow(true);
		sortHeader.setImmediate(true); // needed to work when dataScroller is present
		sortHeader.getChildren().add(initialHeaderFacet);
		initialHeaderFacet.setParent(sortHeader);

		return sortHeader;
	}

	/**
	 * Get sortproperty from el.
	 * 
	 * @param component
	 *            component
	 * @return sortproperty
	 */
	protected String getSortPropertyFromEL(UIComponent component) {
		if (getVar() == null) {
			LOG.warn("There is no 'var' specified on the dataTable, sort properties cannot be determined automaticaly.");
			return null;
		}

		for (Iterator iter = component.getChildren().iterator(); iter.hasNext();) {
			UIComponent aChild = (UIComponent) iter.next();
			if (aChild.isRendered()) {
				ValueBinding vb = aChild.getValueBinding("value");
				if (vb != null) {
					String expressionString = vb.getExpressionString();

					int varIndex = expressionString.indexOf(getVar() + ".");
					if (varIndex != -1) {
						int varEndIndex = varIndex + getVar().length();
						String tempProp = expressionString.substring(varEndIndex + 1, expressionString.length());

						StringTokenizer tokenizer = new StringTokenizer(tempProp, " +[]{}-/*|?:&<>!=()%");
						if (tokenizer.hasMoreTokens()) {
							return tokenizer.nextToken();
						}
					}
				} else {
					String sortProperty = getSortPropertyFromEL(aChild);
					if (sortProperty != null) {
						return sortProperty;
					}
				}
			}
		}

		return null;
	}

	/**
	 * return the index coresponding to the given column name.
	 * 
	 * @param columnName
	 *            column name
	 * @return the index coresponding to the given column name.
	 */
	protected int columnNameToIndex(String columnName) {
		int index = 0;
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			UIComponent aChild = (UIComponent) iter.next();
			if (aChild instanceof UIColumn) {
				UIComponent headerFacet = ((UIColumn) aChild).getHeader();
				if (headerFacet != null && headerFacet instanceof HtmlCommandSortHeader) {
					HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader) headerFacet;
					if (columnName != null && columnName.equals(sortHeader.getColumnName())) {
						return index;
					}
				}
			}

			index += 1;
		}

		return -1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.component.UIData#encodeEnd(javax.faces.context.FacesContext)
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			UIComponent component = (UIComponent) iter.next();
			if (component instanceof UIColumns) {
				((UIColumns) component).encodeTableEnd(context);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFirst() {
		if (preservedDataModel != null) {
			// Rather get the currently restored DataModel attribute
			return preservedDataModel.getFirst();
		} else {
			return super.getFirst();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFirst(int first) {
		if (preservedDataModel != null) {
			// Also change the currently restored DataModel attribute
			preservedDataModel.setFirst(first);
		}
		super.setFirst(first);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRows() {
		if (preservedDataModel != null) {
			// Rather get the currently restored DataModel attribute
			return preservedDataModel.getRows();
		} else {
			return super.getRows();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRows(int rows) {
		if (preservedDataModel != null) {
			// Also change the currently restored DataModel attribute
			preservedDataModel.setRows(rows);
		}
		super.setRows(rows);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object saveState(FacesContext context) {
		boolean preserveSort = isPreserveSort();

		Object[] values = new Object[36];
		values[0] = super.saveState(context);
		values[1] = preserveDataModel;

		if (isPreserveDataModel()) {
			values[2] = saveAttachedState(context, getSerializableDataModel());
		} else {
			values[2] = null;
		}
		values[3] = this.preserveSort;
		values[4] = forceIdIndexFormula;
		values[5] = sortColumn;
		values[6] = sortAscending;
		values[7] = sortProperty;
		values[8] = sortable;
		values[9] = renderedIfEmpty;
		values[10] = rowCountVar;
		values[11] = rowIndexVar;

		values[12] = rowOnClick;
		values[13] = rowOnDblClick;
		values[14] = rowOnMouseDown;
		values[15] = rowOnMouseUp;
		values[16] = rowOnMouseOver;
		values[17] = rowOnMouseMove;
		values[18] = rowOnMouseOut;
		values[19] = rowOnKeyPress;
		values[20] = rowOnKeyDown;
		values[21] = rowOnKeyUp;

		values[22] = rowStyleClass;
		values[23] = rowStyle;

		values[24] = preserveSort ? getSortColumn() : null;
		values[25] = preserveSort ? Boolean.valueOf(isSortAscending()) : null;

		values[26] = varDetailToggler;
		values[27] = expandedNodes;
		values[28] = rowGroupStyle;
		values[29] = rowGroupStyleClass;
		values[30] = sortedColumnVar;
		values[31] = Integer.valueOf(sortColumnIndex);

		values[32] = Integer.valueOf(newspaperColumns);
		values[33] = newspaperOrientation;
		values[34] = bodyStyle;
		values[35] = bodyStyleClass;

		return values;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#getDataModel()
	 */
	protected DataModel getDataModel() {
		if (preservedDataModel != null) {
			setDataModel(preservedDataModel);
			preservedDataModel = null;
		}

		return super.getDataModel();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#createDataModel()
	 */
	protected DataModel createDataModel() {
		DataModel dataModel = super.createDataModel();

		boolean isSortable = isSortable();

		if (!(dataModel instanceof SortableModel)) {
			// if sortable=true make each column sortable
			// if sortable=false, check to see if at least one column sortable case in which
			// the current model needs to be wrapped by a sortable one.
			for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
				UIComponent component = (UIComponent) iter.next();
				if (component instanceof HtmlSimpleColumn) {
					HtmlSimpleColumn aColumn = (HtmlSimpleColumn) component;
					if (isSortable()) {
						aColumn.setSortable(true);
					}

					if (aColumn.isSortable()) {
						isSortable = true;
					}

					if (aColumn.isDefaultSorted() && getSortColumn() == null) {
						setSortProperty(aColumn.getSortPropertyName());
					}
				}
			}

			if (isSortable) {
				dataModel = new SortableModel(dataModel);
			}
		}

		if (isSortable && getSortProperty() != null) {
			SortCriterion criterion = new SortCriterion(getSortProperty(), isSortAscending());
			List criteria = new ArrayList();
			criteria.add(criterion);

			((SortableModel) dataModel).setSortCriteria(criteria);
		}

		return dataModel;
	}

	/**
	 * {@inheritDoc}
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		preserveDataModel = (Boolean) values[1];
		if (isPreserveDataModel()) {
			preservedDataModel = (SerializableDataModel) restoreAttachedState(context, values[2]);
		} else {
			preservedDataModel = null;
		}
		preserveSort = (Boolean) values[3];
		forceIdIndexFormula = (String) values[4];
		sortColumn = (String) values[5];
		sortAscending = (Boolean) values[6];
		sortProperty = (String) values[7];
		sortable = (Boolean) values[8];
		renderedIfEmpty = (Boolean) values[9];
		rowCountVar = (String) values[10];
		rowIndexVar = (String) values[11];

		rowOnClick = (String) values[12];
		rowOnDblClick = (String) values[13];
		rowOnMouseDown = (String) values[14];
		rowOnMouseUp = (String) values[15];
		rowOnMouseOver = (String) values[16];
		rowOnMouseMove = (String) values[17];
		rowOnMouseOut = (String) values[18];
		rowOnKeyPress = (String) values[19];
		rowOnKeyDown = (String) values[20];
		rowOnKeyUp = (String) values[21];

		rowStyleClass = (String) values[22];
		rowStyle = (String) values[23];

		if (isPreserveSort()) {
			String sortColumn = (String) values[24];
			Boolean sortAscending = (Boolean) values[25];
			if (sortColumn != null && sortAscending != null) {
				ValueBinding vb = getValueBinding("sortColumn");
				if (vb != null && !vb.isReadOnly(context)) {
					vb.setValue(context, sortColumn);
				}

				vb = getValueBinding("sortAscending");
				if (vb != null && !vb.isReadOnly(context)) {
					vb.setValue(context, sortAscending);
				}
			}
		}

		varDetailToggler = (String) values[26];
		expandedNodes = (Set) values[27];
		rowGroupStyle = (String) values[28];
		rowGroupStyleClass = (String) values[29];
		sortedColumnVar = (String) values[30];
		sortColumnIndex = values[31] != null ? ((Integer) values[31]).intValue() : -1;
		newspaperColumns = ((Integer) values[32]).intValue();
		newspaperOrientation = (String) values[33];
		bodyStyle = (String) values[34];
		bodyStyleClass = (String) values[35];
	}

	/**
	 * Get serializable datamodel.
	 * 
	 * @return serializable datamodel
	 */
	public SerializableDataModel getSerializableDataModel() {
		DataModel dm = getDataModel();
		if (dm instanceof SerializableDataModel) {
			return (SerializableDataModel) dm;
		}
		return createSerializableDataModel();
	}

	/**
	 * Create serializable datamodel.
	 * 
	 * @return _SerializableDataModel serializable datamodel
	 */
	private SerializableDataModel createSerializableDataModel() {
		Object value = getValue();
		if (value == null) {
			return null;
		} else if (value instanceof DataModel) {
			return new SerializableDataModel(getFirst(), getRows(), (DataModel) value);
		} else if (value instanceof List) {
			return new SerializableListDataModel(getFirst(), getRows(), (List) value);
		} else if (value instanceof Collection) { // accept a Collection is not supported in the Spec
			return new SerializableListDataModel(getFirst(), getRows(), new ArrayList((Collection) value));
		} else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass())) {
			return new SerializableArrayDataModel(getFirst(), getRows(), (Object[]) value);
		} else if (value instanceof ResultSet) {
			return new SerializableResultSetDataModel(getFirst(), getRows(), (ResultSet) value);
		} else if (value instanceof javax.servlet.jsp.jstl.sql.Result) {
			return new SerializableResultDataModel(getFirst(), getRows(), (javax.servlet.jsp.jstl.sql.Result) value);
		} else {
			return new SerializableScalarDataModel(getFirst(), getRows(), value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRendered() {
		if (!UserRoleUtils.isVisibleOnUserRole(this)) {
			return false;
		}
		return super.isRendered();
	}

	/**
	 * Set force id index formula.
	 * 
	 * @param forceIdIndexFormula
	 *            force id index formula
	 */
	public void setForceIdIndexFormula(String forceIdIndexFormula) {
		this.forceIdIndexFormula = forceIdIndexFormula;
		ValueBinding vb = getValueBinding("forceIdIndexFormula");
		if (vb != null) {
			vb.setValue(getFacesContext(), this.forceIdIndexFormula);
			this.forceIdIndexFormula = null;
		}
	}

	/**
	 * Get force id index formula.
	 * 
	 * @return force id index formula
	 */
	public String getForceIdIndexFormula() {
		if (forceIdIndexFormula != null) {
			return forceIdIndexFormula;
		}
		ValueBinding vb = getValueBinding("forceIdIndexFormula");
		if (vb == null) {
			return null;
		}
		Object eval = vb.getValue(getFacesContext());
		return eval == null ? null : eval.toString();
	}

	/**
	 * Specify what column the data should be sorted on.
	 * <p/>
	 * Note that calling this method <i>immediately</i> stores the value via any value-binding with name "sortColumn". This is
	 * done because this method is called by the HtmlCommandSortHeader component when the user has clicked on a column's sort
	 * header. In this case, the the model getter method mapped for name "value" needs to read this value in able to return the
	 * data in the desired order - but the HtmlCommandSortHeader component is usually "immediate" in order to avoid validating
	 * the enclosing form. Yes, this is rather hacky - but it works.
	 * 
	 * @param sortColumn
	 *            sort column
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
		// update model is necessary here, because processUpdates is never called
		// reason: HtmlCommandSortHeader.isImmediate() == true
		ValueBinding vb = getValueBinding("sortColumn");
		if (vb != null) {
			vb.setValue(getFacesContext(), this.sortColumn);
			this.sortColumn = null;
		}

		setSortColumnIndex(columnNameToIndex(sortColumn));
	}

	/**
	 * Get sort column.
	 * 
	 * @return sort column
	 */
	public String getSortColumn() {
		if (sortColumn != null) {
			return sortColumn;
		}
		ValueBinding vb = getValueBinding("sortColumn");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set sort ascending.
	 * 
	 * @param sortAscending
	 *            sort ascending
	 */
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = Boolean.valueOf(sortAscending);
		// update model is necessary here, because processUpdates is never called
		// reason: HtmlCommandSortHeader.isImmediate() == true
		ValueBinding vb = getValueBinding("sortAscending");
		if (vb != null) {
			vb.setValue(getFacesContext(), this.sortAscending);
			this.sortAscending = null;
		}
	}

	/**
	 * Return true if sort ascending.
	 * 
	 * @return true if sort ascending
	 */
	public boolean isSortAscending() {
		if (this.sortAscending != null) {
			return this.sortAscending.booleanValue();
		}
		ValueBinding vb = getValueBinding("sortAscending");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_SORTASCENDING;
	}

	/**
	 * Set sort property.
	 * 
	 * @param sortProperty
	 *            sort property
	 */
	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	/**
	 * Get sort property.
	 * 
	 * @return sort property
	 */
	public String getSortProperty() {
		return sortProperty;
	}

	/**
	 * Set sortable.
	 * 
	 * @param sortable
	 *            sortable
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Is sortable.
	 * 
	 * @return sortable
	 */
	public boolean isSortable() {
		if (sortable != null) {
			return sortable.booleanValue();
		}
		ValueBinding vb = getValueBinding("sortable");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_SORTABLE;
	}

	/**
	 * Set row on mouse over.
	 * 
	 * @param rowOnMouseOver
	 *            row on mouse over
	 */
	public void setRowOnMouseOver(String rowOnMouseOver) {
		this.rowOnMouseOver = rowOnMouseOver;
	}

	/**
	 * Get row on mouse over.
	 * 
	 * @return row on mouse over
	 */
	public String getRowOnMouseOver() {
		if (rowOnMouseOver != null) {
			return rowOnMouseOver;
		}
		ValueBinding vb = getValueBinding("rowOnMouseOver");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on mouse out.
	 * 
	 * @param rowOnMouseOut
	 *            row on mouse out
	 */
	public void setRowOnMouseOut(String rowOnMouseOut) {
		this.rowOnMouseOut = rowOnMouseOut;
	}

	/**
	 * Get row on mouse out.
	 * 
	 * @return row on mouse out
	 */
	public String getRowOnMouseOut() {
		if (rowOnMouseOut != null) {
			return rowOnMouseOut;
		}
		ValueBinding vb = getValueBinding("rowOnMouseOut");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on click.
	 * 
	 * @param rowOnClick
	 *            row on click
	 */
	public void setRowOnClick(String rowOnClick) {
		this.rowOnClick = rowOnClick;
	}

	/**
	 * Get row on click.
	 * 
	 * @return row on click
	 */
	public String getRowOnClick() {
		if (rowOnClick != null) {
			return rowOnClick;
		}
		ValueBinding vb = getValueBinding("rowOnClick");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on doubleclick.
	 * 
	 * @param rowOnDblClick
	 *            row on doubleclick
	 */
	public void setRowOnDblClick(String rowOnDblClick) {
		this.rowOnDblClick = rowOnDblClick;
	}

	/**
	 * Get row on doubleclick.
	 * 
	 * @return row on doubleclick
	 */
	public String getRowOnDblClick() {
		if (rowOnDblClick != null) {
			return rowOnDblClick;
		}
		ValueBinding vb = getValueBinding("rowOnDblClick");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Get row on key down.
	 * 
	 * @return row on key down
	 */
	public String getRowOnKeyDown() {
		if (rowOnKeyDown != null) {
			return rowOnKeyDown;
		}
		ValueBinding vb = getValueBinding("rowOnKeyDown");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on key down.
	 * 
	 * @param rowOnKeyDown
	 *            row on key down
	 */
	public void setRowOnKeyDown(String rowOnKeyDown) {
		this.rowOnKeyDown = rowOnKeyDown;
	}

	/**
	 * Get row on key pressed.
	 * 
	 * @return row on key pressed
	 */
	public String getRowOnKeyPress() {
		if (rowOnKeyPress != null) {
			return rowOnKeyPress;
		}
		ValueBinding vb = getValueBinding("rowOnKeyPress");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on key pressed.
	 * 
	 * @param rowOnKeyPress
	 *            row on key pressed
	 */
	public void setRowOnKeyPress(String rowOnKeyPress) {
		this.rowOnKeyPress = rowOnKeyPress;
	}

	/**
	 * Get row on key up.
	 * 
	 * @return row on key up
	 */
	public String getRowOnKeyUp() {
		if (rowOnKeyUp != null) {
			return rowOnKeyUp;
		}
		ValueBinding vb = getValueBinding("rowOnKeyUp");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on key up.
	 * 
	 * @param rowOnKeyUp
	 *            row on key up
	 */
	public void setRowOnKeyUp(String rowOnKeyUp) {
		this.rowOnKeyUp = rowOnKeyUp;
	}

	/**
	 * Get row style class.
	 * 
	 * @return row style class
	 */
	public String getRowStyleClass() {
		if (rowStyleClass != null) {
			return rowStyleClass;
		}
		ValueBinding vb = getValueBinding(JSFAttr.ROW_STYLECLASS_ATTR);
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row style class.
	 * 
	 * @param rowStyleClass
	 *            row style class
	 */
	public void setRowStyleClass(String rowStyleClass) {
		this.rowStyleClass = rowStyleClass;
	}

	/**
	 * Get row style.
	 * 
	 * @return row style
	 */
	public String getRowStyle() {
		if (rowStyle != null) {
			return rowStyle;
		}
		ValueBinding vb = getValueBinding(JSFAttr.ROW_STYLE_ATTR);
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row style.
	 * 
	 * @param rowStyle
	 *            row style
	 */
	public void setRowStyle(String rowStyle) {
		this.rowStyle = rowStyle;
	}

	/**
	 * Get row on mouse down.
	 * 
	 * @return row on mouse down
	 */
	public String getRowOnMouseDown() {
		if (rowOnMouseDown != null) {
			return rowOnMouseDown;
		}
		ValueBinding vb = getValueBinding("rowOnMouseDown");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on mouse down.
	 * 
	 * @param rowOnMouseDown
	 *            row on mouse down
	 */
	public void setRowOnMouseDown(String rowOnMouseDown) {
		this.rowOnMouseDown = rowOnMouseDown;
	}

	/**
	 * Get row on mouse move.
	 * 
	 * @return row on mouse move
	 */
	public String getRowOnMouseMove() {
		if (rowOnMouseMove != null) {
			return rowOnMouseMove;
		}
		ValueBinding vb = getValueBinding("rowOnMouseMove");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on mouse move.
	 * 
	 * @param rowOnMouseMove
	 *            row on mouse move
	 */
	public void setRowOnMouseMove(String rowOnMouseMove) {
		this.rowOnMouseMove = rowOnMouseMove;
	}

	/**
	 * Get row on mouse up.
	 * 
	 * @return row on mouse up
	 */
	public String getRowOnMouseUp() {
		if (rowOnMouseUp != null) {
			return rowOnMouseUp;
		}
		ValueBinding vb = getValueBinding("rowOnMouseUp");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row on mouse up.
	 * 
	 * @param rowOnMouseUp
	 *            row on mouse up
	 */
	public void setRowOnMouseUp(String rowOnMouseUp) {
		this.rowOnMouseUp = rowOnMouseUp;
	}

	/**
	 * Is valid children.
	 * 
	 * @return true if valif children
	 */
	protected boolean isValidChildren() {
		return isValidChildren;
	}

	/**
	 * Set valid children.
	 * 
	 * @param isValidChildren
	 *            valid children
	 */
	protected void setIsValidChildren(boolean isValidChildren) {
		this.isValidChildren = isValidChildren;
	}

	/**
	 * Get preservedDataModel.
	 * 
	 * @return preservedDataModel
	 */
	protected SerializableDataModel getPreservedDataModel() {
		return preservedDataModel;
	}

	/**
	 * Set preservedDataModel.
	 * 
	 * @param preservedDataModel
	 *            preservedDataModel
	 */
	protected void setPreservedDataModel(SerializableDataModel preservedDataModel) {
		this.preservedDataModel = preservedDataModel;
	}

	/**
	 * Is current detail explained.
	 * 
	 * @return true if current detail explained
	 */
	public boolean isCurrentDetailExpanded() {
		return expandedNodes.contains(Integer.valueOf(getRowIndex()));
	}

	/**
	 * Set var detail toggler.
	 * 
	 * @param varDetailToggler
	 *            var detail toggler
	 */
	public void setVarDetailToggler(String varDetailToggler) {
		this.varDetailToggler = varDetailToggler;
	}

	/**
	 * Get var detail toggler.
	 * 
	 * @return var detail toggler
	 */
	public String getVarDetailToggler() {
		return varDetailToggler;
	}

	/**
	 * Get row group style.
	 * 
	 * @return row group style
	 */
	public String getRowGroupStyle() {
		if (rowGroupStyle != null) {
			return rowGroupStyle;
		}
		ValueBinding vb = getValueBinding("rowGroupStyle");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row group style.
	 * 
	 * @param rowGroupStyle
	 *            row group style
	 */
	public void setRowGroupStyle(String rowGroupStyle) {
		this.rowGroupStyle = rowGroupStyle;
	}

	/**
	 * Get row group style class.
	 * 
	 * @return row group style class
	 */
	public String getRowGroupStyleClass() {
		if (rowGroupStyleClass != null) {
			return rowGroupStyleClass;
		}
		ValueBinding vb = getValueBinding("rowGroupStyleClass");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row group style class.
	 * 
	 * @param rowGroupStyleClass
	 *            row group style class
	 */
	public void setRowGroupStyleClass(String rowGroupStyleClass) {
		this.rowGroupStyleClass = rowGroupStyleClass;
	}

	/**
	 * Get body style.
	 * 
	 * @return body style
	 */
	public String getBodyStyle() {
		if (bodyStyle != null) {
			return bodyStyle;
		}
		ValueBinding vb = getValueBinding("bodyStyle");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set body style.
	 * 
	 * @param bodyStyle
	 *            body style
	 */
	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	/**
	 * Get body style class.
	 * 
	 * @return body style class
	 */
	public String getBodyStyleClass() {
		if (bodyStyleClass != null) {
			return bodyStyleClass;
		}
		ValueBinding vb = getValueBinding("bodyStyleClass");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set body style class.
	 * 
	 * @param bodyStyleClass
	 *            body style class
	 */
	public void setBodyStyleClass(String bodyStyleClass) {
		this.bodyStyleClass = bodyStyleClass;
	}

	/**
	 * Creates a new instance of HtmlDataTable.
	 */
	public HtmlDataTable() {
		setRendererType(DEFAULT_RENDERER_TYPE);
	}

	/**
	 * Change the status of the current detail row from collapsed to expanded or viceversa.
	 */
	public void toggleDetail() {
		Integer rowIndex = Integer.valueOf(getRowIndex());

		if (expandedNodes.contains(rowIndex)) {
			expandedNodes.remove(rowIndex);
		} else {
			expandedNodes.add(rowIndex);
		}
	}

	/**
	 * Return true if the current detail row is expanded.
	 * 
	 * @return true if the current detail row is expanded.
	 */
	public boolean isDetailExpanded() {
		Integer rowIndex = Integer.valueOf(getRowIndex());

		return expandedNodes.contains(rowIndex);
	}

	/**
	 * Get sort column index.
	 * 
	 * @return sort column index
	 */
	public int getSortColumnIndex() {
		if (sortColumnIndex == -1) {
			sortColumnIndex = columnNameToIndex(getSortColumn());
		}

		return sortColumnIndex;
	}

	/**
	 * Set sort column index.
	 * 
	 * @param sortColumnIndex
	 *            sort column index
	 */
	public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * Get the number of columns the table will be divided over.
	 * 
	 * @return newspaperColumns
	 */
	public int getNewspaperColumns() {
		return newspaperColumns;
	}

	/**
	 * Set the number of columns the table will be divided over.
	 * 
	 * @param newspaperColumns
	 *            newspaperColumns
	 */
	public void setNewspaperColumns(int newspaperColumns) {
		this.newspaperColumns = newspaperColumns;
	}

	/**
	 * Set the orientation of the newspaper columns.
	 * 
	 * @param newspaperOrientation
	 *            newspaperOrientation
	 */
	public void setNewspaperOrientation(String newspaperOrientation) {
		this.newspaperOrientation = newspaperOrientation;
	}

	/**
	 * Get the orientation of the newspaper columns.
	 * 
	 * @return newspaperOrientation
	 */
	public String getNewspaperOrientation() {
		return newspaperOrientation;
	}

	/**
	 * Gets the spacer facet, between each pair of newspaper columns.
	 * 
	 * @return spacer
	 */
	public UIComponent getSpacer() {
		return (UIComponent) getFacets().get(SPACER_FACET_NAME);
	}

	/**
	 * Sets the spacer facet, between each pair of newspaper columns.
	 * 
	 * @param spacer
	 *            spacer
	 */
	public void setSpacer(UIComponent spacer) {
		getFacets().put(SPACER_FACET_NAME, spacer);
	}

	/**
	 * Expand all details.
	 */
	public void expandAllDetails() {
		int rowCount = getRowCount();

		expandedNodes.clear();
		for (int row = 0; row < rowCount; row++) {
			expandedNodes.add(Integer.valueOf(row));
		}
	}

	/**
	 * Collapse all details.
	 */
	public void collapseAllDetails() {
		expandedNodes.clear();
	}

	/**
	 * Is expanded empty.
	 * 
	 * @return true is any of the details is expanded
	 */
	public boolean isExpandedEmpty() {
		boolean expandedEmpty = true;
		if (expandedNodes != null) {
			expandedEmpty = expandedNodes.isEmpty();
		}
		return expandedEmpty;
	}

	/**
	 * Clears expanded nodes set if expandedEmpty is true.
	 * 
	 * @param expandedEmpty
	 *            expanded empty
	 */
	public void setExpandedEmpty(boolean expandedEmpty) {
		if (expandedEmpty) {
			if (expandedNodes != null) {
				expandedNodes.clear();
			}
		}
	}

	// ------------------ xx GENERATED CODE BEGIN (do not modify!) --------------------
	// ------------------ MODIFIED ANYWAY, GENERATORS SHOULD USE CODE STANDARD --------------------

	/** Component type. */
	public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataTable";
	/** Default renderer type. */
	public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Table";

	private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
	private static final boolean DEFAULT_PRESERVESORT = true;
	private static final boolean DEFAULT_RENDEREDIFEMPTY = true;

	private Boolean preserveDataModel = null;
	private Boolean preserveSort = null;
	private String enabledOnUserRole = null;
	private String visibleOnUserRole = null;
	private Boolean renderedIfEmpty = null;
	private String rowIndexVar = null;
	private String rowCountVar = null;
	private String sortedColumnVar = null;
	private String previousRowDataVar = null;

	/**
	 * Set preserved datamodel.
	 * 
	 * @param preserveDataModel
	 *            preserved datamodel
	 */
	public void setPreserveDataModel(boolean preserveDataModel) {
		this.preserveDataModel = Boolean.valueOf(preserveDataModel);
	}

	/**
	 * Is preserved datamodel.
	 * 
	 * @return true if preserved datamodel
	 */
	public boolean isPreserveDataModel() {
		if (preserveDataModel != null) {
			return preserveDataModel.booleanValue();
		}
		ValueBinding vb = getValueBinding("preserveDataModel");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_PRESERVEDATAMODEL;
	}

	/**
	 * Set preserved sort.
	 * 
	 * @param preserveSort
	 *            preserved sort
	 */
	public void setPreserveSort(boolean preserveSort) {
		this.preserveSort = Boolean.valueOf(preserveSort);
	}

	/**
	 * Is preserved sort.
	 * 
	 * @return preserved sort
	 */
	public boolean isPreserveSort() {
		if (preserveSort != null) {
			return preserveSort.booleanValue();
		}
		ValueBinding vb = getValueBinding("preserveSort");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_PRESERVESORT;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabledOnUserRole(String enabledOnUserRole) {
		this.enabledOnUserRole = enabledOnUserRole;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEnabledOnUserRole() {
		if (enabledOnUserRole != null) {
			return enabledOnUserRole;
		}
		ValueBinding vb = getValueBinding("enabledOnUserRole");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVisibleOnUserRole(String visibleOnUserRole) {
		this.visibleOnUserRole = visibleOnUserRole;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getVisibleOnUserRole() {
		if (visibleOnUserRole != null) {
			return visibleOnUserRole;
		}
		ValueBinding vb = getValueBinding("visibleOnUserRole");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set rendered if empty.
	 * 
	 * @param renderedIfEmpty
	 *            rendered if empty
	 */
	public void setRenderedIfEmpty(boolean renderedIfEmpty) {
		this.renderedIfEmpty = Boolean.valueOf(renderedIfEmpty);
	}

	/**
	 * Is rendered if empty.
	 * 
	 * @return rendered if empty
	 */
	public boolean isRenderedIfEmpty() {
		if (renderedIfEmpty != null) {
			return renderedIfEmpty.booleanValue();
		}
		ValueBinding vb = getValueBinding("renderedIfEmpty");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_RENDEREDIFEMPTY;
	}

	/**
	 * Set row index var.
	 * 
	 * @param rowIndexVar
	 *            row index var
	 */
	public void setRowIndexVar(String rowIndexVar) {
		this.rowIndexVar = rowIndexVar;
	}

	/**
	 * Get row index var.
	 * 
	 * @return row index var
	 */
	public String getRowIndexVar() {
		if (rowIndexVar != null) {
			return rowIndexVar;
		}
		ValueBinding vb = getValueBinding("rowIndexVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set row count var.
	 * 
	 * @param rowCountVar
	 *            row count var
	 */
	public void setRowCountVar(String rowCountVar) {
		this.rowCountVar = rowCountVar;
	}

	/**
	 * Get row count var.
	 * 
	 * @return row count var
	 */
	public String getRowCountVar() {
		if (rowCountVar != null) {
			return rowCountVar;
		}
		ValueBinding vb = getValueBinding("rowCountVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set previousRowDataVar.
	 * 
	 * @param previousRowDataVar
	 *            previousRowDataVar
	 */
	public void setPreviousRowDataVar(String previousRowDataVar) {
		this.previousRowDataVar = previousRowDataVar;
	}

	/**
	 * Get previousRowDataVar.
	 * 
	 * @return previousRowDataVar
	 */
	public String getPreviousRowDataVar() {
		if (previousRowDataVar != null) {
			return previousRowDataVar;
		}
		ValueBinding vb = getValueBinding("previousRowDataVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * Set sorted column var.
	 * 
	 * @param sortedColumnVar
	 *            sorted column var
	 */
	public void setSortedColumnVar(String sortedColumnVar) {
		this.sortedColumnVar = sortedColumnVar;
	}

	/**
	 * Get sorted column var.
	 * 
	 * @return sorted column var
	 */
	public String getSortedColumnVar() {
		if (sortedColumnVar != null) {
			return sortedColumnVar;
		}
		ValueBinding vb = getValueBinding("sortedColumnVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	// ------------------ MODIFIED GENERATED CODE END ---------------------------------------
}
