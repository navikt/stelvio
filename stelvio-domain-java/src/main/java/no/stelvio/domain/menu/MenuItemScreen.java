package no.stelvio.domain.menu;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class represent a MenuItemScreen. Instances of this class are Java Persistence API Entities, and may be persisted by any ORM
 * product that support JPA Annotations table mapping.
 * 
 * @version $id$
 */

@Entity
@Table(name = "T_SKJERMBILDE")
public class MenuItemScreen implements Serializable {

	@Transient
	private static final long serialVersionUID = 8436877079118486417L;

	/**
	 * The primary key.
	 */
	@Column(name = "SKJERMBILDE_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long screenId;

	/**
	 * A list of MenuItem objects that are a part of this screen.
	 */
	@ManyToMany(mappedBy = "menuItemScreens")
	private Set<MenuItem> menuItems = new TreeSet<MenuItem>();

	/**
	 * The name of the screen.
	 */
	@Column(name = "NAVN")
	private String name;

	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	protected MenuItemScreen() {
	}

	/**
	 * Constructs a new MenuItemScreen.
	 * 
	 * @param screenId
	 *            screen id
	 * @param name
	 *            menu item screen name
	 */
	public MenuItemScreen(int screenId, String name) {
		this.screenId = screenId;
		this.name = name;
	}

	/**
	 * Gets screen id.
	 * 
	 * @return the screenId
	 */
	public long getScreenId() {
		return screenId;
	}

	/**
	 * Sets screen id.
	 * 
	 * @param screenId
	 *            the screenId to set
	 */
	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	/**
	 * Gets name for this menu item.
	 * 
	 * @return the name of the screen.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this menu item.
	 * 
	 * @param name
	 *            the screen name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets a list of menu items.
	 * 
	 * @return list of MenuItem objects that are a part of this screen.
	 */
	public Set<MenuItem> getMenus() {
		return menuItems;
	}

	/**
	 * Sets a list of menu items.
	 * 
	 * @param menuItems
	 *            sets the MenuItem objects that are a part of this screen.
	 */
	public void setMenus(Set<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}