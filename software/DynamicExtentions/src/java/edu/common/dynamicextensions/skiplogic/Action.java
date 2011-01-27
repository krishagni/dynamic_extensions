/**
 *
 */
package edu.common.dynamicextensions.skiplogic;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;


/**
 * @author Gaurav_mehta
 *
 */
/**
 * @author kunal_kamble
 *
 */
public interface Action
{

	/**
	 * Perform action.
	 * @param control dependent control
	 */
	void performAction(ControlInterface control);


	/**
	 * Sets the identifier.
	 * @param identifier the new identifier
	 */
	void setIdentifier(Long identifier);

	/**
	 * Gets the identifier.
	 * @return the identifier
	 */
	Long getIdentifier();

	/**
	 * Sets the control identifier.
	 * @param controlIdentifier the new control identifier
	 */
	void setControl(ControlInterface control);

	/**
	 * Gets the control identifier.
	 * @return the control identifier
	 */
	ControlInterface getControl();

	/**
	 * @param controlInterface
	 */
	void resetAction(ControlInterface controlInterface);

	/**
	 * Sets the default skip logic value.
	 * @param defaultSkipLogicValue the new default skip logic value
	 */
	void setDefaultSkipLogicValue(PermissibleValueInterface defaultSkipLogicValue);

	/**
	 * Sets the default skip logic value.
	 * @param defaultSkipLogicValue the new default skip logic value
	 */
	PermissibleValueInterface getDefaultSkipLogicValue();

}
