package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.Layer;


public class LayerGroupBarFacade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;

	public List<Layer> getRootLayers(ApplicationEntity applicationEntity) {
		try {
			Query query = entityManager.createNamedQuery(Layer.FIND_APP_LAYERS, Layer.class);
			query.setParameter("applicationEntityId", applicationEntity);
			List<Layer> list = query.getResultList();
			return list == null ? new ArrayList<Layer>() : list;
		} catch (Exception e) {
			return new ArrayList<Layer>();
		}
	}

	public Long getRootLayersSize(ApplicationEntity applicationEntity) {
		try {
			Query query = entityManager.createNamedQuery(Layer.FIND_APP_LAYERS_SIZE, Long.class);
			query.setParameter("applicationEntityId", applicationEntity);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Layer> getRootLayers(Layer layerGroup) {
		try {
			Query query = entityManager.createNamedQuery(Layer.FIND_APP_LAYER_ROOT, Layer.class);
			query.setParameter("layer", layerGroup);
			List<Layer> list = query.getResultList();
			
			
			return list == null ? new ArrayList<Layer>() : list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Layer>();
		}
	}

	public List<Layer> getSubLayers(Long fatherId) {
		try {
			Query query = entityManager.createNamedQuery(Layer.FIND_APP_LAYER_PARENT_ID, Layer.class);
			query.setParameter("fatherId", fatherId);
			List<Layer> list = query.getResultList();
			return list == null ? new ArrayList<Layer>() : list;
		} catch (Exception e) {
			return new ArrayList<Layer>();
		}
	}

	public Boolean hasSubLayer(Layer layerGroup) {
		try {
			Query query = entityManager.createNamedQuery(Layer.FIND_APP_LAYER_PARENT_ID, Layer.class);
			query.setParameter("fatherId", layerGroup.getId());
			return query.getResultList().size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	// public Boolean hasAtive(Layer layerGroup) {
	// Query query = getEntityManager().createNamedQuery(Layer.FIND_REFERENCE,
	// Layer.class);
	// query.setParameter("parentId", layerGroup.getId());
	// return query.getResultList().size() > 0;
	// }

}
