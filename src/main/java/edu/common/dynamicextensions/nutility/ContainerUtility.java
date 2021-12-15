package edu.common.dynamicextensions.nutility;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class ContainerUtility {
	public static ContainerChangeLog getChangeLog(Container oldContainer, Container newContainer) {
		ContainerChangeLog changeLog = new ContainerChangeLog();
		
		for (Control newCtrl : newContainer.getControls()) {
			if (oldContainer.getControl(newCtrl.getName()) == null) {
				changeLog.getAddedCtrls().add(newCtrl);
			}
		}
		
		for (Control oldCtrl : oldContainer.getControls()) {
			if (newContainer.getControl(oldCtrl.getName()) == null) {
				changeLog.getDeletedCtrls().add(oldCtrl);
			}			
		}
		
		for (Control newCtrl : newContainer.getControls()) {
			Control oldCtrl = oldContainer.getControl(newCtrl.getName());
			
			if (newCtrl instanceof SubFormControl && oldCtrl instanceof SubFormControl) {
				Container oldSf = ((SubFormControl)oldCtrl).getSubContainer();
				Container newSf = ((SubFormControl)newCtrl).getSubContainer();
				
				ContainerChangeLog subFormLog = getChangeLog(oldSf, newSf);				
				if (subFormLog.anyChanges()) {
					changeLog.getEditedSubCtrls().put(oldCtrl.getName(), subFormLog);
				}
			} else if (oldCtrl != null && !oldCtrl.equals(newCtrl)) {
				changeLog.getEditedCtrls().add(newCtrl);
			}
		}
						
		return changeLog;
	}	
}