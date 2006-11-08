
package edu.common.dynamicextensions.domaininterface;

/**
 * This is a primitive attribute of type integar.Using this information a column of type integer is prepared.
 * @author geetika_bangard
 */
public interface IntegerAttributeInterface extends AttributeInterface
{

	/**
	 * Default value of type integer.
	 * @return Returns the defaultValue.
	 */
	Integer getDefaultValue();

	/**
	 * @param defaultValue The defaultValue to set.
	 */
	void setDefaultValue(Integer defaultValue);

	/**
	 * The measurement units are shown in the dynamically created user interface.
	 * The measurement units are meter,kg,cm etc.They are displayed after the user input control. 
	 * @return Returns the measurementUnits.
	 */
	String getMeasurementUnits();

	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits the measurement units to be set.
	 */
	void setMeasurementUnits(String measurementUnits);

	/**
	 * This method returns the places after the decimal point.
	 * @return the places after the decimal point.
	 */
	String getDecimalPlaces();

	/**
	 * This method sets the places after the decimal point of the DoubleAttribue.
	 * @param decimalPlaces the places after the decimal point to be set.
	 */
	void setDecimalPlaces(String decimalPlaces);

	/**
	 * This method returns the length of the number in digits.
	 * @return the length of the number in digits. 
	 */
	String getDigits();

	/**
	 * This method sets the length of the number in digits.
	 * @param digits the length of the number in digits.
	 */
	void setDigits(String digits);

	//	public String getSize();
	//	public void setSize(String size);

}
