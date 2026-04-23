package org.geoazul.model.app;

/**
 * SUPERADMIN - somebody with access to the site administration features and all other features.<br/>
 * ADMIN - somebody who has access to all the administration features.<br/>
 * DEVELOPER - somebody who has access to developer tools,<br/>
 * EDITOR - somebody who can publish and manage geometries including the geometries of other users,<br/>
 * AUTHOR - somebody who can publish and manage their own geometries,<br/>
 * COLABOLATOR - somebody who can write and manage their own geometries but cannot publish them,<br/>
 * USER - somebody who can only manage their profile.
 */

//SUPERADMINISTRATOR, ADMINISTRATOR, DEVELOPER, EDITOR, AUTHOR, COLABOLATOR, SUBSCRIBER
//FIXME
	public enum AppRole
	{
		superadmin, admin, developer, editor, author, colaborator,user
	}
