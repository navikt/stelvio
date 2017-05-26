package no.stelvio.domain.menu;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class represent a MenuItem. Instances of this class are Java Persistence API Entities, and may be persisted by any ORM
 * product that support JPA Annotations table mapping.
 * 
 * @author person4f9bc5bd17cc (Accenture)
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 * @version $id$
 */
@NamedQueries({ @NamedQuery(name = "MenuItem.findParents", 
		query = "SELECT m FROM MenuItem m WHERE m.parent is null") })
@Entity
@Table(name = "T_MENYVALG")
public class MenuItem implements Serializable, Comparable {

	private static final long serialVersionUID = -987889062411683185L;

	/**
	 * The primary key.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MENYVALG_ID")
	private long menuId;

	/**
	 * Parent menu object, <code>null</code> if it's a root menuitem.
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private MenuItem parent;

	/**
	 * List of child MenuItem objects.
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	private Set<MenuItem> children = new TreeSet<MenuItem>();

	/**
	 * List of menuItemPermissions this menu is a part of.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_MNYVLG_TILGNG", joinColumns = @JoinColumn(name = "MENYVALG_ID"), 
			inverseJoinColumns = @JoinColumn(name = "TILGANG_ID"))
	private Set<MenuItemPermission> menuItemPermissions = new TreeSet<MenuItemPermission>();

	/**
	 * List of menuItemScreens this menu is a part of.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_MNYVLG_SKJMBD", joinColumns = @JoinColumn(name = "MENYVALG_ID"), 
			inverseJoinColumns = @JoinColumn(name = "SKJERMBILDE_ID"))
	private Set<MenuItemScreen> menuItemScreens = new TreeSet<MenuItemScreen>();

	/**
	 * The name of the flow to execute when clicking the menuitem.
	 */
	@Column(name = "FLYTNAVN")
	private String flowName;

	/**
	 * The sortingorder of the menuitem.
	 */
	@Column(name = "SORTERING_ORD")
	private int order;

	/**
	 * The text to display.
	 */
	@Column(name = "LEDETEKST")
	private String leadtext;

	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	protected MenuItem() {
	}

	/**
	 * Constructs a new MenuItem.
	 * 
	 * @param menuId
	 *            uniquely identifies this menu item
	 * @param parent
	 *            this menu items parent, <code>null</code> if this is a toplevel MenuItem
	 * @param order
	 *            the order of this menu item
	 * @param leadtext
	 *            the leadtext
	 */
	public MenuItem(int menuId, MenuItem parent, int order, String leadtext) {
		this.menuId = menuId;
		this.parent = parent;
		this.order = order;
		this.leadtext = leadtext;
	}

	/**
	 * Gets the menu items children.
	 * 
	 * @return the list of children MenuItem objects, empty list if this MenuItem is a leaf.
	 */
	public Set<MenuItem> getChildren() {
		return children;
	}

	/**
	 * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this
	 * object is less than, equal to, or greater than the specified object.
	 * 
	 * Compares this menu item with another menu item, the menu items are ordered according to the "order" attribute. Returns a
	 * negative integer, zero, or a positive integer as this menu item is less than, equal to or greater than the specified menu
	 * item.
	 * 
	 * @param item
	 *            The menu item to compare this menu item with
	 * @return an integer representing the ordering of the two menu items.
	 */
	public int compareTo(Object item) {
		if (item != null && item instanceof MenuItem) {
			return this.order - ((MenuItem) item).getOrder();
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((flowName == null) ? 0 : flowName.hashCode());
		result = prime * result + ((leadtext == null) ? 0 : leadtext.hashCode());
		result = prime * result + (int) (menuId ^ (menuId >>> 32));
		result = prime * result + ((menuItemPermissions == null) ? 0 : menuItemPermissions.hashCode());
		result = prime * result + ((menuItemScreens == null) ? 0 : menuItemScreens.hashCode());
		result = prime * result + order;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object item) {
		if (item == null || !(item instanceof MenuItem)) {
			return false;
		}
		return (this.order - ((MenuItem) item).getOrder()) == 0;
	}

	/**
	 * Sets the menu items chg.
	 * 
	 * @param children
	 *            the child MenuItem objects.
	 */
	public void setChildren(Set<MenuItem> children) {
		this.children = children;
	}

	/**
	 * Gets the flow name.
	 * 
	 * @return the flowName.
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * Sets the flow name.
	 * 
	 * @param flowName
	 *            the flowName to set.
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/**
	 * Gets menu idd.
	 * 
	 * @return the menuId.
	 */
	public long getMenuId() {
		return menuId;
	}

	/**
	 * Sets menu id.
	 * 
	 * @param menuId
	 *            the menuId to set.
	 */
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	/**
	 * Gets lead text.
	 * 
	 * @return the leadtext.
	 */
	public String getLeadtext() {
		return leadtext;
	}

	/**
	 * Sets lead text.
	 * 
	 * @param leadtext
	 *            the leadtext to set.
	 */
	public void setLeadtext(String leadtext) {
		this.leadtext = leadtext;
	}

	/**
	 * Gets parent.
	 * 
	 * @return the parent MenuItem object, <code>null</code> if <code>this</code> is a root menu item
	 */
	public MenuItem getParent() {
		return parent;
	}

	/**
	 * Sets parent.
	 * 
	 * @param parent
	 *            the parent MenuItem object to set.
	 */
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	/**
	 * Gets the sort order.
	 * 
	 * @return the sort order.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @param order
	 *            the sort order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Gets the permissions.
	 * 
	 * @return a list of MenuItemPermission objects this menu is a part of.
	 */
	public Set<MenuItemPermission> getPermissions() {
		return menuItemPermissions;
	}

	/**
	 * Sets the permissions.
	 * 
	 * @param menuItemPermissions
	 *            sets the MenuItemPermission objects this menu is a part of
	 */
	public void setPermissions(Set<MenuItemPermission> menuItemPermissions) {
		this.menuItemPermissions = menuItemPermissions;
	}

	/**
	 * Gets the screens.
	 * 
	 * @return a list of MenuItemScreen objects this menu is a part of.
	 */
	public Set<MenuItemScreen> getScreens() {
		return menuItemScreens;
	}

	/**
	 * Sets the screens.
	 * 
	 * @param menuItemScreens
	 *            sets the MenuItemScreen objects this menu is a part of.
	 */
	public void setScreens(Set<MenuItemScreen> menuItemScreens) {
		this.menuItemScreens = menuItemScreens;
	}

}