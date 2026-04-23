package org.geoazul.model.website;

import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.geoazul.model.basic.AbstractGeometry;

/**
 *  
 */

@Entity
@Table(name = "APP_POST")
@NamedQueries({
		@NamedQuery(name = UrlPostItem.FIND_ID, query = "select urlPostItem from UrlPostItem urlPostItem where urlPostItem.id = :id"),
		@NamedQuery(name = UrlPostItem.UPDATE_STRINGS, query="update UrlPostItem set strings = :newStrings where id = :id"),
		@NamedQuery(name = UrlPostItem.UPDATE_ALL,     query="update UrlPostItem set enabled= :enabled, father= :father, iconflag= :iconflag,"
				+ " layer= :layer, nome= :nome, parte= :parte, situacao= :situacao, strings = :strings where id = :id"),
		
		
		
		@NamedQuery(name = UrlPostItem.FIND_POSTS, query = 
		"select urlPostItem from UrlPostItem urlPostItem where urlPostItem.layer.id = :layerId order by urlPostItem.id desc"),
		@NamedQuery(name = UrlPostItem.FIND_POSTS_ID, query = 
				"select urlPostItem from UrlPostItem urlPostItem where urlPostItem.layer.id = :layerId order by urlPostItem.id desc")
		
		
})
public class UrlPostItem extends AbstractGeometry implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String FIND_ID = "UrlPostItem.uuid";
	public static final String FIND_POSTS = "UrlPostItem.findPosts";
	public static final String FIND_POSTS_ID = "UrlPostItem.findPostsId";
	 public static final String UPDATE_STRINGS = "UrlPostItem.UPDATE_STRINGS";
	 public static final String UPDATE_ALL = "UrlPostItem.UPDATE_ALL";
	 
	public UrlPostItem() {
		this.situacao = (short) 1;
		this.enabled = true;
	}


}
