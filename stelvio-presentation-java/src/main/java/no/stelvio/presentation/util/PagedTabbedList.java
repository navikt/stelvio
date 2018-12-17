package no.stelvio.presentation.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.FacesEvent;

/**
 * Creating a tabbed and paged list to display with JSF datatable tag.
 * 
 * @param <T>
 *            Inncomming element type that list containes.
 * @author persone8306a256e67
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class PagedTabbedList<T> extends PagedSortableList<T> {

	private static final long serialVersionUID = -8145929833407932870L;

	/**
	 * Currently selected tab.
	 */
	private String currentPage;

	/**
	 * The map holds a list of row objects per tab key.
	 */
	private Map<String, List<Object>> indexSortColumnMap = new HashMap<>();

	/**
	 * List of tab keys.
	 */
	private List<String> keys = new ArrayList<>();

	/**
	 * Constructor, creating a tabbed and paged list to display with JSF datatable tag.
	 * 
	 * @param list
	 *            result to present in tabbed table
	 * @param sortColumnTabRange
	 *            tab range of sort column
	 * @param sortColumn
	 *            name of variable in result objects to sort on
	 */
	public PagedTabbedList(List<T> list, int sortColumnTabRange, String sortColumn) {
		super(list, sortColumnTabRange, sortColumn);

		String methodName = GET + sortColumn.substring(0, 1).toUpperCase() + sortColumn.substring(1);

		for (Object object : list) {
			try {
				// Using reflection find the get method matching the sortColumn input
				Method method = object.getClass().getMethod(methodName, (Class[]) null);
				// If this is not of type Integer, a PagedTabbedList cannot be used
				if (!Integer.class.equals(method.getReturnType())) {
					if (log.isDebugEnabled()) {
						log.debug(" Column sort type " + method.getReturnType() + " is not Integer");
					}
					continue;
				}
				Integer result = (Integer) method.invoke(object);
				// From the integer of this object find the first value of the range it belongs to.
				int rangeStart 
						= (int) (Math.floor(result.doubleValue() / (double) sortColumnTabRange) * (double) sortColumnTabRange);
				// The range will correspond to an id representing the tab in the GUI
				String tabId = rangeStart + "-" + (rangeStart + sortColumnTabRange - 1);
				// Find this tab in the list of all tabs. If it does not exist, add it to list of tabs.
				List<Object> subList = indexSortColumnMap.get(tabId);
				if (subList == null) {
					subList = new ArrayList<>();
					keys.add(tabId);
				}
				// The class holds a map of tabs, each with a list of objects found related to tab
				subList.add(object);
				indexSortColumnMap.put(tabId, subList);

			} catch (IllegalAccessException illegalAccess) {
				log.warn("IllegalAccessException:" + illegalAccess.getMessage());
			} catch (InvocationTargetException invocTarget) {
				log.warn("InvocationTargetException:" + invocTarget.getMessage());
			} catch (NoSuchMethodException noSuchMethodException) {
				log.warn("NoSuchMethodException:" + noSuchMethodException.getMessage());
			}
		}

		// Initialize the table to start with the first tab
		if (!keys.isEmpty()) {
			currentPage = keys.get(0);
			List<Object> subList = indexSortColumnMap.get(keys.get(0));
			setNumberOfRowsOnEachPage(list.indexOf(subList.get(subList.size() - 1)) - getRowIndex() + 1);
		}
		setNumberOfPages(indexSortColumnMap.size());

		if (log.isDebugEnabled()) {
			log.debug("Drawing a table with " + list.size() + " entries," + " and tabs  " + keys + ".");
		}
	}

	/**
	 * Handles the paging of the list.
	 * 
	 * @param event
	 *            JSF FacesEvent
	 */
	@Override
	public void pageing(FacesEvent event) {
		// jac2812: Changed this method due to SIR #63580. The paging does not work correctly in view SIS002 Opptjening
		// folketrygden if the view contains two lists. Therefore, the id's of the tab elements must be generated differently
		// for this view. The old element id was equal to the text on the tab, for instance _2000-2009. The new id consists of
		// the text "tab_" plus the index of the tab (starting from 0 on the leftmost tab). This way of generating tab ids is
		// used for all modules using this component in PSELV, but the old code is kept in the else-statement, since I don't
		// know if this component is being used elsewere.
		// sec2812: SIR #94709: Added name of the pageingHeader component to the tab id's, to make them unique when two
		// pageingHeaders are used in the same page.
		String id = event.getComponent().getId();
		String way = "";
		String tab = "tab_";
		if (id.contains(tab)) {
			int indexOfTabIndex = id.indexOf(tab) + tab.length();
			String tabIndex = id.substring(indexOfTabIndex);
			way = keys.get(Integer.parseInt(tabIndex));
		} else {
			way = id;
		}
		pageing(way);
	}

	/**
	 * Handles the paging of the list.
	 * 
	 * @param way
	 *            event id; next, previous, last, first
	 */
	public void pageing(String way) {
		if (keys.size() <= 1) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("way = " + way);
		}

		// Based on ActionEvent change the pageIndex, that is the currenly selected index.
		if (way.equals(NEXT)) {
			setPageIndex(getPageIndex() + 1);
		} else if (way.equals(PREVIOUS)) {
			setPageIndex(getPageIndex() - 1);
		} else if (way.equals(LAST)) {
			setPageIndex(keys.size());
		} else if (way.equals(FIRST)) {
			setRowIndex(0);
			setPageIndex(1);
		} else if (way.length() > 1 && way.startsWith("_")) {
			setPageIndex(keys.indexOf(way.substring(1)) + 1);
		} else if (way.length() > 1 && keys.contains(way)) {
			setPageIndex(keys.indexOf(way) + 1);
		}

		// Find the tab id representing the pageindex from the list of tab keys.
		currentPage = keys.get(getPageIndex() - 1);
		// Find the list of objects to be shown from the tab id
		List<Object> subList = indexSortColumnMap.get(currentPage);

		if (subList != null) {
			setRowIndex(getList().indexOf(subList.get(0)));
			setNumberOfRowsOnEachPage(getList().indexOf(subList.get(subList.size() - 1)) - getRowIndex() + 1);
		}
	}

	/**
	 * Get the list of tabs.
	 * 
	 * @return list of tabs
	 */
	public List<String> getKeys() {
		return keys;
	}

	/**
	 * Get the name of the currently selected tab.
	 * 
	 * @return currently selected tab
	 */
	public String getCurrentPage() {
		return currentPage;
	}

	/**
	 * Set the name of the currently selected tab.
	 * 
	 * @param currentPage
	 *            currently selected tab
	 */
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Get the key of a given element in the sorted column.
	 * 
	 * @param id
	 *            of sorted column
	 * @return key
	 */
	public String getPage(Integer id) {

		if (getSortColumn() == null || indexSortColumnMap == null || id == null) {
			return null;
		}

		String methodName = "get" + getSortColumn().substring(0, 1).toUpperCase() + getSortColumn().substring(1);

		for (String key : indexSortColumnMap.keySet()) {
			for (Object object : indexSortColumnMap.get(key)) {
				try {
					// Using reflection find the get method matching the sortColumn input
					Method method = object.getClass().getMethod(methodName, (Class[]) null);
					// If this is not of type Integer, a PagedTabbedList cannot be used
					if (!Integer.class.equals(method.getReturnType())) {
						return null;
					}
					Integer result = (Integer) method.invoke(object);
					if (result.equals(id)) {
						return key;
					}
				} catch (IllegalAccessException illegalAccess) {
					log.warn("IllegalAccessException:" + illegalAccess.getMessage());
				} catch (InvocationTargetException invocTarget) {
					log.warn("InvocationTargetException:" + invocTarget.getMessage());
				} catch (NoSuchMethodException noSuchMethodException) {
					log.warn("NoSuchMethodException:" + noSuchMethodException.getMessage());
				}
			}
		}
		return null;
	}

}
