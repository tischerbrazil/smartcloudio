/*
 */

package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlMenuItem;

/**
 *
 * @author
 */

public class UrlMenuBarFacade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;

	public List<UrlMenu> getRootUrlMenus(AbstractIdentityEntity abstractIdentityEntity) {
		try {
			Query query = entityManager.createNamedQuery(UrlMenuItem.FIND_APP_MENUS, UrlMenu.class);
			query.setParameter("abstractIdentityEntity", abstractIdentityEntity);
			List<UrlMenu> list = query.getResultList();
			return list == null ? new ArrayList<UrlMenu>() : list;
		} catch (Exception e) {
			return new ArrayList<UrlMenu>();
		}
	}

	public Long getRootUrlMenusSize(AbstractIdentityEntity abstractIdentityEntity) {
		try {
			Query query = entityManager.createNamedQuery(UrlMenuItem.FIND_APP_MENUS_SIZE, Long.class);
			query.setParameter("abstractIdentityEntity", abstractIdentityEntity);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<UrlMenuItem> getRootUrlMenuItems(UrlMenu urlMenu) {
		try {
			Query query = entityManager.createNamedQuery(UrlMenuItem.FIND_APP_MENU_ROOT, UrlMenuItem.class);
			query.setParameter("urlMenu", urlMenu);
			List<UrlMenuItem> list = query.getResultList();
			return list == null ? new ArrayList<UrlMenuItem>() : list;
		} catch (Exception e) {
			return new ArrayList<UrlMenuItem>();
		}
	}

	public List<UrlMenuItem> getSubMenus(Long parentId) {
		try {
			Query query = entityManager.createNamedQuery(UrlMenuItem.FIND_APP_MENU_PARENT_ID, UrlMenuItem.class);
			query.setParameter("parentId", parentId);
			List<UrlMenuItem> list = query.getResultList();
			return list == null ? new ArrayList<UrlMenuItem>() : list;
		} catch (Exception e) {
			return new ArrayList<UrlMenuItem>();
		}
	}

	public Boolean hasSubMenu(UrlMenuItem menu) {
		try {
			Query query = entityManager.createNamedQuery(UrlMenuItem.FIND_APP_MENU_PARENT_ID, UrlMenuItem.class);
			query.setParameter("parentId", menu.getId());
			return query.getResultList().size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	// public Boolean hasAtive(UrlMenuItem menu) {
	// Query query = getEntityManager().createNamedQuery(UrlMenuItem.FIND_REFERENCE,
	// UrlMenuItem.class);
	// query.setParameter("parentId", menu.getId());
	// return query.getResultList().size() > 0;
	// }

}
