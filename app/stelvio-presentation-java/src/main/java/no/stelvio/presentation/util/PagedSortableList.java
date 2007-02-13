package no.stelvio.presentation.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;


/**
 * Class to handle pageable lists. 
 *  
 * @author persond3cb2ee15f42, Accenture
 *
 * @param <T> 
 */
public class PagedSortableList<T> {

	/**
	 * Prefix to "get" methods
	 */
	private static final String GET = "get";

	/**
	 * Event occured when the first page is selected
	 */
	private static final String FIRST = "first";

	/**
	 * Event occured when the last page is selected
	 */
	private static final String LAST = "last";

	/**
	 * Event occured when the previous page is selected
	 */
	private static final String PREVIOUS = "previous";

	/**
	 * Event occured when the next page is selected
	 */
	private static final String NEXT = "next";

	/**
	 * The list
	 */
	private List<T> list = new ArrayList<T>();

	/**
	 * Holds which column that the list has been sorted by
	 */
	private String sortColumn;

	/**
	 * Holds which column that the list was last sorted by. Used to determin the
	 * value of ascending.
	 */
	private String lastSortColumn;

	/**
	 * Holds which direction the sorting has been done by
	 */
	private boolean ascending;

	/**
	 * Holds how many pages this list has
	 */
	private long numberOfPages;

	/**
	 * Holds how many items there are in the list
	 */
	private long numberOfItems = 0;

	/**
	 * Holds which item that is the last item on the page, starting at 1
	 */
	private int showEnd;

	/**
	 * Holds the number of how many rows that should be displayed on each page
	 */
	private int numberOfRowsOnEachPage;

	/**
	 * Holds which row that is the first row on the page, starting at 0
	 */
	private int rowIndex;

	/**
	 * Holds which page is current page, starting at 1
	 */
	private int pageIndex;

	/**
	 * Constructor for this class. Sets the list, number of rows to be displayed
	 * on each page, and the default sort column.
	 * 
	 * @param list
	 *            The list of objects
	 * @param numberOfRowsOnEachPage
	 *            the number of rows on each page
	 * @param defaultSortColumn
	 *            the default sort column
	 */
	public PagedSortableList(List<T> list, int numberOfRowsOnEachPage,
			String defaultSortColumn) {
		if (list == null) {
			list = new ArrayList<T>();
		}
		
		this.list = list;
		this.numberOfRowsOnEachPage = numberOfRowsOnEachPage;
		ascending = true;

		sortColumn = defaultSortColumn;
		sort(sortColumn, ascending);
		lastSortColumn = sortColumn;

		rowIndex = 0;
		pageIndex = 1;
		numberOfPages = (long) Math.ceil((double) list.size()
				/ (double) numberOfRowsOnEachPage);

		numberOfItems = list.size();

		setShowEnd();
	}

	/**
	 * Handles the paging of the list.
	 * 
	 * @param event the event that has occured
	 */
	public void pageing(ActionEvent event) {
		String way = event.getComponent().getId();

		if (NEXT.equals(way)) {
			rowIndex += numberOfRowsOnEachPage;
			pageIndex++;
		} else if (PREVIOUS.equals(way)) {
			rowIndex -= numberOfRowsOnEachPage;
			pageIndex--;
		} else if (LAST.equals(way)) {
			rowIndex = (int) (numberOfPages - 1) * numberOfRowsOnEachPage;
			pageIndex = (int) numberOfPages;
		} else if (FIRST.equals(way)) {
			rowIndex = 0;
			pageIndex = 1;
		}

		setShowEnd();

	}

	/**
	 * Sets the show end attribute
	 *
	 */
	private void setShowEnd() {
		showEnd = numberOfRowsOnEachPage * pageIndex;
		if (showEnd > numberOfItems) {
			showEnd = (int) numberOfItems;
		}
	}

	/**
	 * Determins which column the list should be sorted by, and which direction
	 * it should be sorted. It then calls the sort method, which does the actual
	 * sorting.
	 * 
	 * @param event the event that has occured
	 */
	public void sorting(ActionEvent event) {
		String column = event.getComponent().getId();

		if (column.equals(lastSortColumn)) {
			ascending = !ascending;
		} else {
			ascending = true;
		}

		lastSortColumn = column;
		setSortColumn(column);
		sort(column, ascending);
	}

	/**
	 * Sorts the list based on which column and which direction.
	 * 
	 * @param column the column to sort
	 * @param ascending indicates if the column should be sorted ascending or descending
	 */
	private void sort(final String column, final boolean ascending) {
		Comparator comparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				Object c1 = o1;
				Object c2 = o2;
				if (column == null) {
					return 0;
				}

				String methodName = GET
						+ column.substring(0, 1).toUpperCase()
						+ column.substring(1);
				try {
					Method m = c1.getClass().getMethod(methodName, null);
					Method m2 = c2.getClass().getMethod(methodName, null);

					Comparable res1 = (Comparable) m.invoke(c1);
					Comparable res2 = (Comparable) m2.invoke(c2);

					return ascending ? res1.compareTo(res2) : res2
							.compareTo(res1);

				} catch (IllegalAccessException illegalAccess) {
					// do nothing
				} catch (InvocationTargetException invocTarget) {
					// do nothing
				} catch (Exception general) {
					// do nothing
				}
				return 0;
			}
		};
		Collections.sort(list, comparator);

	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending the ascending to set
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return the lastSortColumn
	 */
	public String getLastSortColumn() {
		return lastSortColumn;
	}

	/**
	 * @param lastSortColumn the lastSortColumn to set
	 */
	public void setLastSortColumn(String lastSortColumn) {
		this.lastSortColumn = lastSortColumn;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * @return the numberOfItems
	 */
	public long getNumberOfItems() {
		return numberOfItems;
	}

	/**
	 * @param numberOfItems the numberOfItems to set
	 */
	public void setNumberOfItems(long numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	/**
	 * @return the numberOfPages
	 */
	public long getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(long numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * @return the numberOfRowsOnEachPage
	 */
	public int getNumberOfRowsOnEachPage() {
		return numberOfRowsOnEachPage;
	}

	/**
	 * @param numberOfRowsOnEachPage the numberOfRowsOnEachPage to set
	 */
	public void setNumberOfRowsOnEachPage(int numberOfRowsOnEachPage) {
		this.numberOfRowsOnEachPage = numberOfRowsOnEachPage;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the showEnd
	 */
	public int getShowEnd() {
		return showEnd;
	}

	/**
	 * @param showEnd the showEnd to set
	 */
	public void setShowEnd(int showEnd) {
		this.showEnd = showEnd;
	}

	/**
	 * @return the sortColumn
	 */
	public String getSortColumn() {
		return sortColumn;
	}

	/**
	 * @param sortColumn the sortColumn to set
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}


}
