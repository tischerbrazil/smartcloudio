package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.website.LayerItem;

public class LayerBarFacade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;

	public List<Layer> getRootLayers(AbstractIdentityEntity abstractIdentityEntity) {
		try {
			Query query = entityManager.createNamedQuery(LayerItem.FIND_APP_LAYERS, Layer.class);
			query.setParameter("abstractIdentityEntity", abstractIdentityEntity);
			List<Layer> list = query.getResultList();
			return list == null ? new ArrayList<Layer>() : list;
		} catch (Exception e) {
			return new ArrayList<Layer>();
		}
	}

	public Long getRootLayersSize(AbstractIdentityEntity abstractIdentityEntity) {
		try {
			Query query = entityManager.createNamedQuery(LayerItem.FIND_APP_LAYERS_SIZE, Long.class);
			query.setParameter("abstractIdentityEntity", abstractIdentityEntity);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<LayerItem> getRootLayerItems(Layer layer) {
		try {
			Query query = entityManager.createNamedQuery(LayerItem.FIND_APP_LAYER_ROOT, LayerItem.class);
			query.setParameter("layer", layer);
			List<LayerItem> list = query.getResultList();
			return list == null ? new ArrayList<LayerItem>() : list;
		} catch (Exception e) {
			return new ArrayList<LayerItem>();
		}
	}

	public List<LayerItem> getSubLayers(Long parentId) {
		try {
			Query query = entityManager.createNamedQuery(LayerItem.FIND_APP_LAYER_PARENT_ID, LayerItem.class);
			query.setParameter("parentId", parentId);
			List<LayerItem> list = query.getResultList();
			return list == null ? new ArrayList<LayerItem>() : list;
		} catch (Exception e) {
			return new ArrayList<LayerItem>();
		}
	}

	public Boolean hasSubLayer(LayerItem layer) {
		try {
			Query query = entityManager.createNamedQuery(LayerItem.FIND_APP_LAYER_PARENT_ID, LayerItem.class);
			query.setParameter("parentId", layer.getId());
			return query.getResultList().size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	// public Boolean hasAtive(LayerItem layer) {
	// Query query = getEntityManager().createNamedQuery(LayerItem.FIND_REFERENCE,
	// LayerItem.class);
	// query.setParameter("parentId", layer.getId());
	// return query.getResultList().size() > 0;
	// }

}
