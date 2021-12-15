package edu.common.dynamicextensions.nutility;

public interface ContainerSerializer {
	//
	// Serializes both view and skip rules
	//
	void serialize();
	
	void serialize(int maxPvListSize);
}