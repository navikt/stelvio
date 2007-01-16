package no.stelvio.domain.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class represent a MenuItemScreen.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by any ORM that support JPA Annotations table mapping.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */

@Entity(name="MenuItemScreen")
@Table(name="MENU_SCREEN")
public class MenuItemScreen implements Serializable {

	@Transient
	private static final long serialVersionUID = 8436877079118486417L;

	/**
	 * The primary key.
	 */
	@Column(name="MENU_SCREEN_ID")
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long screenId;
	
	/**
	 * A list of MenuItem objects that are a part of this screen.
	 */
	@ManyToMany(mappedBy="screens")
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	/**
	 * The name of the screen.
	 */
	@Column(name="NAME")
	private String name;

	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	protected MenuItemScreen() {}
	
	/**
	 * Constructs a new MenuItemScreen
	 * @param screenId screen id
	 * @param name menu item screen name
	 */
	public MenuItemScreen(int screenId, String name){
		this.screenId = screenId;
		this.name = name;
	}
	
	/**
	 * Gets screen id
	 * @return the screenId
	 */
	public long getScreenId() {
		return screenId;
	}

	/**
	 * Sets screen id
	 * @param screenId the screenId to set
	 */
	public void setScreenId(int id) {
		this.screenId = id;
	}

	/**
	 * Gets name for this menu item
	 * @return the name of the screen.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this menu item
	 * @param name the screen name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets a list of menu items
	 * @return list of MenuItem objects that are a part of this screen.
	 */
	public List<MenuItem> getMenus() {
		return menuItems;
	}

	/**
	 * Sets a list of menu items
	 * @param menuItems sets the MenuItem objects that are a part of this screen.
	 */
	public void setMenus(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}