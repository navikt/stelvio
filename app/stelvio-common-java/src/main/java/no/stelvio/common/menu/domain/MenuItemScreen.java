package no.stelvio.common.menu.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Class represent a MenuItemScreen.
 * Instances of this class are Java Persistence API Entities,
 * and may be persisted by an JPA EntityManager.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 * @todo should not be necessary with setters at all?
 */
@Entity(name="MenuItemScreen")
@Table(name="MENU_SCREEN")
public class MenuItemScreen implements Serializable {
	// TODO: Remove or create generated value
	private static final long serialVersionUID = 1L;

	/**
	 * The primary key.
	 */
	@Column(name="MENU_SCREEN_ID")
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int screenId;
	
	/**
	 * A list of MenuItem objects that are a part of this screen.
	 */
	@ManyToMany(mappedBy="screens")
	private List<MenuItem> menuItems;
	
	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 */
	// There shall be no setter-method for the version-field.
	@Version
	@Column(name="VERSION")
	private long version;
	
	/**
	 * The name of the screen.
	 */
	@Column(name="NAME")
	private String name;

	/**
	 * Default no-arg constructor protected as it should only be used by persistence provider.
	 */
	public MenuItemScreen() {}
	
	/**
	 * @return the screenId
	 */
	public int getScreenId() {
		return screenId;
	}

	/**
	 * @param screenId the screenId to set
	 */
	public void setScreenId(int id) {
		this.screenId = id;
	}

	/**
	 * @return the name of the screen.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the screen name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * In order to prevent "lost updates" all entities need to include a version field.
	 * This field is used by the persistence provider to implement optimistic locking.
	 * @return the version.
	 */ 
	public long getVersion() {
		return version;
	}

	/**
	 * @return a list of MenuItem objects that are a part of this screen.
	 */
	public List<MenuItem> getMenus() {
		return menuItems;
	}

	/**
	 * @param menuItems sets the MenuItem objects that are a part of this screen.
	 */
	public void setMenus(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}