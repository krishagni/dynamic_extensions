
package edu.common.dynamicextensions.xmi;

/**
 * This class is used For configuring the ImportXmi Process . for giving some optional FLags
 * Which are used for different purpose like saving only metadata, should Id name allowed or not etc.
 * @author pavan_kalantri
 *
 */
public class XMIConfiguration
{

	/**
	 * This flag is used to indicate weather tables should be created For DynamicExtensions or 
	 * only metadata should be saved
	 */
	private boolean isCreateTable = true;

	/**
	 * This flag indicates weather the new column should be added in child as foreignKey or not. 
	 */
	private boolean isAddCol4InhInChild = false;

	/**
	 * This flag indicates weather the attributes of the parent should be added to the child or not.
	 * and these attributes will act as separate its own attribute
	 */
	private boolean isAddInheriedAttr = false;

	/**
	 * This is used for CA-tissue specific purpose To indicate it is static model & to store
	 * metadata of that model only
	 */
	private boolean isEntGrpSysGen = false;

	/**
	 * This indicates is "id" attribute is generated by the system for each entity
	 * or should user be able to add attribute named "id" it should be added only if user specify it.
	 */
	private boolean isAdddIdAttr = true;

	private static XMIConfiguration xmiConfiguration;

	private XMIConfiguration()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method will create an object of the XMIConfiguration if it does not exist and return that object 
	 * @return xmiConfiguration object
	 */
	public static synchronized XMIConfiguration getInstance()
	{
		if (xmiConfiguration == null)
		{
			xmiConfiguration = new XMIConfiguration();

		}
		return xmiConfiguration;
	}

	/**
	 * Will return weather IdAttribute is system generated or not
	 * @return
	 */
	public boolean isAddIdAttribute()
	{
		return isAdddIdAttr;
	}

	/**
	 * Will set weather IdAttribute is system generated or not
	 * @param isIdAttributeSystemGenarated
	 */
	public void setAddIdAttr(boolean isAddIdAttr)
	{
		this.isAdddIdAttr = isAddIdAttr;
	}

	/**
	 * Will return weather entityGroup is system generated or not
	 * @return
	 */
	public boolean isEntityGroupSystemGenerated()
	{
		return isEntGrpSysGen;
	}

	/**
	 * Will set weather entityGroup is system generated or not
	 * @param isEntityGroupSystemGenerated
	 */
	public void setEntityGroupSystemGenerated(boolean isEntityGroupSystemGenerated)
	{
		this.isEntGrpSysGen = isEntityGroupSystemGenerated;
	}

	/**
	 * Will return weather table should be created or not
	 * @return
	 */
	public boolean isCreateTable()
	{
		return isCreateTable;
	}

	/**
	 * Will set weather table should be created or not
	 * @param isCreateTable
	 */
	public void setCreateTable(boolean isCreateTable)
	{
		this.isCreateTable = isCreateTable;
	}

	/**
	 * Will return weather extra column should be added to child which acts as foreign key of parent in case of inheritance
	 * @return
	 */
	public boolean isAddColumnForInherianceInChild()
	{
		return isAddCol4InhInChild;
	}

	/**
	 * Will set weather extra column should be added to child which acts as foreign key of parent in case of inheritance
	 * @param isAddColumnForInheriance
	 */
	public void setAddColumnForInherianceInChild(boolean isAddColumnForInheriance)
	{
		this.isAddCol4InhInChild = isAddColumnForInheriance;
	}

	/**
	 * Will return weather extra column should be added to child which acts as foreign key of parent in case of inheritance
	 * @return
	 */
	public boolean isAddInheriedAttribute()
	{
		return isAddInheriedAttr;
	}

	/**
	 * Will set weather extra column should be added to child which acts as foreign key of parent in case of inheritance
	 * @param isAddInheriedAttribute
	 */
	public void setAddInheriedAttribute(boolean isAddInheriedAttribute)
	{
		this.isAddInheriedAttr = isAddInheriedAttribute;
	}

}
