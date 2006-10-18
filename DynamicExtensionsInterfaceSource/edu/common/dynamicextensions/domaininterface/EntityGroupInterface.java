
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * Entities can be grouped in the form of entity goup.
 * @author geetika_bangard
 */
public interface EntityGroupInterface extends AbstractMetadataInterface
{

    /**
     * Returns a collection of entity objects present in the entity group 
     * @return Returns the entityCollection.
     */
     Collection getEntityCollection();
    
    /**
     * Adds an entity to the entity group.
     * @param entityInterface The entity to be added in the entity group.
     */
     void addEntity(EntityInterface entityInterface);
   
	/**
     * Returns the long name of the entity group
	 * @return Returns the longName.
	 */
	 String getLongName();
	/**
	 * @param longName The longName to set.
	 */
	 void setLongName(String longName);
     
	/**
     * Returns the short name of the entity group
	 * @return Returns the shortName.
	 */
	 String getShortName();
	/**
	 * @param shortName The shortName to set.
	 */
	 void setShortName(String shortName);
	/**
     * Returns the version of the entity group. 
	 * @return Returns the version.
	 */
	 String getVersion();
	/**
	 * @param version The version to set.
	 */
	 void setVersion(String version);
}
