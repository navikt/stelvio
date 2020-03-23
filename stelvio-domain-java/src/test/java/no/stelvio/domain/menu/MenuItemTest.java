package no.stelvio.domain.menu;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MenuItem.
 * 
 * @version $Id$
 * 
 */
public class MenuItemTest {

	private MenuItem menuItem1;
	private MenuItem menuItem2;
	private MenuItem menuItem3;

	/**
	 * Initializes the objects used in this test.
	 * 
	 */
	@Before
	public void setUp() {
		menuItem1 = new MenuItem();
		menuItem1.setOrder(1);
		menuItem2 = new MenuItem();
		menuItem2.setOrder(2);
		menuItem3 = new MenuItem();
		menuItem3.setOrder(3);
	}

	/**
	 * Compares two menuitems where the first one is less than the second one.
	 * 
	 */
	@Test
	public void compareLess() {
		assertTrue(menuItem1.compareTo(menuItem2) < 0);

	}

	/**
	 * Compares two menuitems where the first one is greater than the second one.
	 * 
	 */
	@Test
	public void compareGreater() {
		menuItem1.setOrder(2);
		menuItem2.setOrder(1);
		assertTrue(menuItem1.compareTo(menuItem2) > 0);

	}

	/**
	 * Compares two menuitems where they both have the same ordering.
	 */
	@Test
	public void compareEquals() {
		menuItem2.setOrder(1);
		assertTrue(menuItem1.compareTo(menuItem2) == 0);
	}

	/**
	 * Creates a set of children for a menu item, and verifies that an iteration over the menu items children returns the
	 * children in the correct order.
	 * 
	 */
	@Test
	public void ordedChildren() {
		Set<MenuItem> children = new TreeSet<MenuItem>();
		children.add(menuItem3);
		children.add(menuItem2);
		children.add(new MenuItem());
		menuItem1.setChildren(children);

		int currentOrder = 0;
		assertTrue(menuItem1.getChildren().size() == 3);
		for (MenuItem current : menuItem1.getChildren()) {
			assertTrue(current.getOrder() >= currentOrder);
			currentOrder = current.getOrder();
		}
	}

	/**
	 * Compares a menuItem to a null reference.
	 * 
	 */
	@Test
	public void compareToNull() {
		menuItem2 = null;
		assertTrue(menuItem1.compareTo(menuItem2) == -1);
	}

	/**
	 * Compares a menu item to an object of type other than MenuItem.
	 * 
	 */
	@Test
	public void compareToNonMenuItem() {
		assertTrue(menuItem1.compareTo(new Object()) == -1);
	}

}
