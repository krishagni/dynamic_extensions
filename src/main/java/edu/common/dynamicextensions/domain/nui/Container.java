package edu.common.dynamicextensions.domain.nui;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;

import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.napi.FormEventsNotifier;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.ndao.ContainerDao;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.nutility.ContainerCache;
import edu.common.dynamicextensions.nutility.ContainerParser;
import edu.common.dynamicextensions.nutility.FormulaParser;
import edu.common.dynamicextensions.nutility.IdGenerator;

public class Container implements Serializable {
	private static final Logger logger = Logger.getLogger(Container.class);

	private static final long serialVersionUID = -6178237643696575798L;

	private static final String tableNameFmt = "DE_E_%d";
	
	private static final String columnNameFmt = "DE_A_%d";
	
	private static Pattern notAllowed = Pattern.compile("\\W+", Pattern.CASE_INSENSITIVE);

	private static final String primaryKeyCtrlName = "_?primary_key?_";

	private static final String CREATE_IDX_DDL = "create index %s_%sX on %s(%s)";

	private static final String ADD_FK = "alter table %s add constraint %s foreign key (%s) references %s(%s)";
			
	private Long id;
	
	private String name;

	private String caption;
	
	private String dbTableName;
	
	private String primaryKey = "IDENTIFIER";
	
	private String hierarchyTable;
	
	private String hierarchyAncestorCol;
	
	private String hierarchyDescendentCol;
	
	private String activeCond;
   	
	private int sequenceNo;

	private long ctrlId;
	
	private boolean managedTables = false;
	
	private Map<String, Control> controlsMap = new LinkedHashMap<>();
	
	private Set<String> userDefCtrlNames = new HashSet<>();

	private List<Control> deletedCtrls = new ArrayList<>();
	
	private List<SkipRule> skipRules = new ArrayList<>();

	private List<Layout> layouts = new ArrayList<>();

	private transient boolean isDto;
		
	private transient List<Control> addLog = new ArrayList<>();
	
	private transient List<Control> editLog = new ArrayList<>();
	
	private transient List<Control> delLog = new ArrayList<>();
	
	private transient Long createdBy;

	private transient Long lastUpdatedBy;

	private transient Date creationTime;

	private transient Date lastUpdatedTime;
	
	private transient int maxPvListSize;

	private transient List<String> undoDeletesList = new ArrayList<>();

	private transient boolean disableDeletedCtrlsTracking = false;

	public void useAsDto() {
		this.isDto = true;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (StringUtils.isNotBlank(name) && notAllowed.matcher(name).find()) {
			throw new FormException("Special characters (including spaces) are not allowed in the form names!");
		}
		
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}
	
	public String getPrimaryKey() {
		return primaryKey == null ? "IDENTIFIER" : primaryKey;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getHierarchyTable() {
		return hierarchyTable;
	}

	public void setHierarchyTable(String hierarchyTable) {
		this.hierarchyTable = hierarchyTable;
	}

	public String getHierarchyAncestorCol() {
		return hierarchyAncestorCol;
	}

	public void setHierarchyAncestorCol(String hierarchyAncestorCol) {
		this.hierarchyAncestorCol = hierarchyAncestorCol;
	}

	public String getHierarchyDescendentCol() {
		return hierarchyDescendentCol;
	}

	public void setHierarchyDescendentCol(String hierarchyDescendentCol) {
		this.hierarchyDescendentCol = hierarchyDescendentCol;
	}

	public String getActiveCond() {
		return activeCond;
	}

	public void setActiveCond(String activeCond) {
		this.activeCond = activeCond;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}
	
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Collection<Control> getControls() {
		return controlsMap.values();
	}

	public Map<String, Control> getControlsMap() {
		return controlsMap;
	}
	
	public void setControlsMap(Map<String, Control> controlsMap) {
		this.controlsMap = controlsMap;
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public int getMaxPvListSize() {
		return maxPvListSize;
	}

	public void setMaxPvListSize(int maxPvListSize) {
		this.maxPvListSize = maxPvListSize;
		getControlsMap().values().stream()
			.filter(ctrl -> ctrl instanceof SubFormControl)
			.map(ctrl -> (SubFormControl) ctrl)
			.forEach(sfCtrl -> sfCtrl.getSubContainer().setMaxPvListSize(maxPvListSize));
	}

	public void setControls(Set<Control> controls) {
		controlsMap.clear();
		for (Control control : controls) {
			controlsMap.put(control.getName(), control);
		}
	}
	
	public List<SkipRule> getSkipRules() {
		return skipRules;
	}
	
	public SkipRuleBuilder newSkipRule() {
		return new SkipRuleBuilder(this);
	}
	
	public void addSkipRule(SkipRule rule) {
		skipRules.add(rule);
	}
	
	public void removeSkipRule(int i) {
		if (i < 0 || i > skipRules.size()) {
			throw new FormException("SkipRule index out of bounds: " + i + " : " + skipRules.size());
		}
		
		skipRules.remove(i);
	}

	public List<Layout> getLayouts() {
		return layouts;
	}

	public void setLayouts(List<Layout> layouts) {
		this.layouts = layouts;
	}

	public Set<String> getUserDefCtrlNames() {
		return userDefCtrlNames;
	}

	public void setUserDefCtrlNames(Set<String> userDefCtrlNames) {
		this.userDefCtrlNames = userDefCtrlNames;
	}
	
	public boolean hasPhiFields() {
		boolean hasPhiFields = false;
		for (Control control : getAllControls()) {
			if (control.isPhi()) {
				hasPhiFields = true;
				break;
			}
		}

		return hasPhiFields;
	}

	public String getUdnFormula(String shortCodeFormula) {
		FormulaParser formulaParser = new FormulaParser();
		String udnFormula = shortCodeFormula;
		
		try {
			formulaParser.parseExpression(shortCodeFormula);
		} catch (Exception e) {
			throw new FormException("Error while parsing the formula : "+shortCodeFormula, e);
		}
	
		List<String> symbols = formulaParser.getSymbols();
		orderByLength(symbols);
		
		for (String symbol : symbols) {
			Control ctrl =  getControl(symbol, "\\.");
			
			if (ctrl == null) {
				throw new FormException("Control with name doesn't exist: " + symbol);
			}
			
			String userDefName = getControlCanonicalUdn(ctrl);
			udnFormula = udnFormula.replaceAll(symbol, userDefName);
		}
	
		return udnFormula;
	}
	
	public String getShortCodeFormula(String udnFormula) { 
		FormulaParser formulaParser = new FormulaParser();
		String shortCodeFormula = udnFormula;
		
		try {
			formulaParser.parseExpression(udnFormula);
		} catch (Exception e) {
			throw new FormException("Error while parsing the formula : "+shortCodeFormula,e);
		}
	
		List<String> symbols = formulaParser.getSymbols();
		orderByLength(symbols);
		
		for (String symbol : symbols) {
			Control ctrl =  getControlByUdn(symbol, "\\.");
			
			if (ctrl == null) {
				throw new FormException("Control with udn doesn't exist: " + symbol);
			}
			String ctrlName = getControlCanonicalName(ctrl);
			shortCodeFormula = shortCodeFormula.replaceAll(symbol, ctrlName);
		}
	
		return shortCodeFormula;
	}
	
	private void orderByLength(List<String> symbols) {
		Collections.sort(symbols, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.length() - s1.length(); // < 0 =0 > 0
			}
		});
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result	+ ((controlsMap == null) ? 0 : controlsMap.hashCode());
		result = prime * result + (int) (ctrlId ^ (ctrlId >>> 32));
		result = prime * result	+ ((dbTableName == null) ? 0 : dbTableName.hashCode());
		result = prime * result + ((primaryKey == null ? 0 : primaryKey.hashCode()));
		result = prime * result + ((hierarchyTable == null ? 0 : hierarchyTable.hashCode()));
		result = prime * result + ((hierarchyAncestorCol == null ? 0 : hierarchyAncestorCol.hashCode()));
		result = prime * result + ((hierarchyDescendentCol == null ? 0 : hierarchyDescendentCol.hashCode()));
		result = prime * result + ((activeCond == null ? 0 : activeCond.hashCode()));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sequenceNo;
		result = prime * result	+ ((skipRules == null) ? 0 : skipRules.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		Container other = (Container) obj;
		if (!StringUtils.equals(caption, other.caption) ||
		    (controlsMap == null && other.controlsMap != null) ||
			controlsMap.equals(other.controlsMap) ||
			ctrlId != other.ctrlId ||
			!StringUtils.equals(dbTableName, other.dbTableName) ||
			!StringUtils.equals(primaryKey, other.primaryKey) ||
			!StringUtils.equals(hierarchyTable, other.hierarchyTable) ||
			!StringUtils.equals(hierarchyAncestorCol, other.hierarchyAncestorCol) ||
			!StringUtils.equals(hierarchyDescendentCol, other.hierarchyDescendentCol) ||
			!StringUtils.equals(activeCond, other.activeCond) ||
			!StringUtils.equals(name, other.name) ||
			sequenceNo != other.sequenceNo ||
			(skipRules == null && other.skipRules != null) ||
			!skipRules.equals(other.skipRules)) {
			return false;
		}
			
		return true;
	}
	
	private Object readResolve() {
		if (userDefCtrlNames == null) {
			userDefCtrlNames = new HashSet<String>();
		}
		
		return this;
	}

	public List<Container> getSubContainers() {
		return controlsMap.values().stream()
			.filter(ctrl -> ctrl instanceof SubFormControl)
			.map(ctrl -> ((SubFormControl) ctrl).getSubContainer())
			.collect(Collectors.toList());
	}
	
	public Collection<List<Control>> getControlsGroupedByRow() {
		Map<Integer, List<Control>> rows = new TreeMap<Integer, List<Control>>();
		
		for (Control ctrl : controlsMap.values()) {
			List<Control> row = rows.get(ctrl.getSequenceNumber());
			if (row == null) {
				row = new ArrayList<Control>();
				rows.put(ctrl.getSequenceNumber(), row);
			}
			
			int xPos = ctrl.getxPos();
			int i;
			for (i = 0; i < row.size(); ++i) {
				if (row.get(i).getxPos() > xPos) {
					break;
				}
			}
			
			row.add(i, ctrl);
		}
		
		return rows.values();
	}
	
	
	public List<Container> getAllSubContainers() {
		List<Container> result = new ArrayList<Container>();
	
		List<Container> working = new ArrayList<Container>();
		working.add(this);
		while (!working.isEmpty()) {
			Container container = working.remove(0);
			List<Container> subContainers = container.getSubContainers();
			
			result.addAll(subContainers);			
			working.addAll(subContainers);			
		}
		
		return result;
	}	
	
	public List<Control> getAllControls() {
		List<Control> controls = new ArrayList<Control>();
		getAllControls(this, controls);
		return controls;
	}
		
	//
	// Behavioral API
	//
	public Container getReplica() {
		Container replica = Container.fromXml(this.toXml());
		replica.nullifyContainerIds();
		return replica;
	}

	public void nullifyContainerIds() {
		setId(null);
		for (Container sub : getAllSubContainers()) {
			sub.setId(null);
		}		
	}
		
	public void addControl(Control control) {
		ensureUniqueNameAndUdn(control);
		validateNameAndUdn(control);
		
		if (control.getSequenceNumber() == 0) {
			control.setSequenceNumber(++sequenceNo);
		} else if (control.getSequenceNumber() > sequenceNo) {
			sequenceNo = control.getSequenceNumber();
		}
				
		addLog.add(control);
		
		if (!isDto) {
			control.setId(++ctrlId);		
			if (control.getDbColumnName() == null) {
				control.setDbColumnName(String.format(columnNameFmt, ctrlId)); // set db name here
			}			
						
			if (control instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)control;
				Container sfContainer = sfCtrl.getSubContainer();
				if (sfContainer.isDto) {
					sfCtrl.setSubContainer(fromDto(sfContainer));
				}
			}
		}
		
		controlsMap.put(control.getName(), control);
		userDefCtrlNames.add(control.getUserDefinedName());
		control.setContainer(this);
	}

	private void ensureUniqueNameAndUdn(Control control) {
		if (StringUtils.isBlank(control.getName())) {
			throw new FormException("Control name cannot be null");
		}

		if (controlsMap.containsKey(control.getName())) {
			throw new FormException("Control with the same name '" + control.getName() + "' already exists");
		}

		if (StringUtils.isBlank(control.getUserDefinedName())) {
			throw new FormException("Control user defined name cannot be null");
		}

		if (userDefCtrlNames.contains(control.getUserDefinedName())) {
			throw new FormException("Control with the same user defined name '" + control.getUserDefinedName() + "' already exists");
		}

		for (Control deletedCtrl : getDeletedCtrls()) {
			if (control.getName().equals(deletedCtrl.getName())) {
				throw new FormException("Control with the same name '" + control.getName() + "' was already used");
			}

			if (control.getUserDefinedName().equals(deletedCtrl.getUserDefinedName())) {
				throw new FormException("Control with the same user defined name '" + control.getUserDefinedName() + "' was already used");
			}
		}
	}

	private void validateNameAndUdn(Control control) {
		if (StringUtils.isBlank(control.getName())) {
			throw new FormException("Control name cannot be empty");
		} else if (Character.isDigit(control.getName().trim().charAt(0))) {
			throw new FormException(
				"Control names like " + control.getName() +
				" starting with numeric characters are not allowed!");
		} else if (notAllowed.matcher(control.getName()).find()) {
			throw new FormException(
				"Control name " + control.getName() + " contains special characters. " +
				"Special characters including spaces are not allowed.");
		} else if (StringUtils.isBlank(control.getUserDefinedName())) {
			throw new FormException("Control UDN cannot be empty");
		} else if (Character.isDigit(control.getUserDefinedName().trim().charAt(0))) {
			throw new FormException(
				"Control UDNs like " + control.getUserDefinedName() +
				" starting with numeric characters are not allowed!");
		} else if (notAllowed.matcher(control.getUserDefinedName()).find()) {
			throw new FormException(
				"Control UDN " + control.getUserDefinedName() + " contains special characters. " +
				"Special characters including spaces are not allowed.");
		}
	}

	public void editControl(String name, Control control) {
		throwExceptionIfDto();
		editControl(name, control, false);
	}
	
	public void editControl(String name, Control control, boolean bulkEdit) {
		throwExceptionIfDto();
		
		Control existingControl = controlsMap.remove(name);
		if (existingControl == null) {
			// change this exception to status code
			throw new FormException("Control with name doesn't exist: " + name);
		}		
		userDefCtrlNames.remove(existingControl.getUserDefinedName());

		validateNameAndUdn(control);
		existingControl.setHidden(control.isHidden());

		if (control.getSequenceNumber() == 0) {
			control.setSequenceNumber(existingControl.getSequenceNumber());
		} else if (control.getSequenceNumber() > sequenceNo) {
			sequenceNo = control.getSequenceNumber();
		}
		
		if (remove(addLog, existingControl)) { 
			//
			// newly added control was edited
			//
			control.setId(existingControl.getId());
			control.setDbColumnName(existingControl.getDbColumnName());			
			add(addLog, control);
		} else if (!existingControl.getClass().getName().equals(control.getClass().getName())) {
			//
			// control type got changed...
			//
			if (isManagedTables()) {
				//
				// we allow arbitrary change for the managed tables
				//
				control.setId(++ctrlId);
				if (!(control instanceof MultiSelectControl) && !isManagedTables()) {	// For MSCtrls Column name is "VALUE"
					control.setDbColumnName(String.format(columnNameFmt, ctrlId)); 	    // Set DB name here
				}

				add(delLog, existingControl);
				add(addLog, control);
			} else if ((existingControl instanceof RadioButton && (control instanceof StringTextField || control instanceof ComboBox)) ||
				(existingControl instanceof ComboBox && (control instanceof StringTextField || control instanceof RadioButton)) ||
				(existingControl instanceof MultiSelectControl && control instanceof MultiSelectControl)) {

				//
				// only following change is allowed:
				// radio button to dropdown and vice-versa
				// radio button to string text field
				// dropdown to string text field
				// check box to multi-select dropdown and vice-versa
				//

				control.setId(existingControl.getId());
				control.setDbColumnName(existingControl.getDbColumnName());
				control.setContainer(this);

				if (existingControl instanceof MultiSelectControl) {
					MultiSelectControl existingMsCtrl = (MultiSelectControl)existingControl;
					MultiSelectControl newMsCtrl = (MultiSelectControl)control;
					newMsCtrl.setTableName(existingMsCtrl.getTableName());
				}

				add(editLog, control);
			} else {
				throw new FormException(
					"Changing field type from " +
					existingControl.getCtrlType() + " to " + control.getCtrlType() +
					" is not allowed");
			}
		} else {
			//
			// saved control is edited
			//
			if (existingControl instanceof MultiSelectControl) {
				MultiSelectControl existingMsCtrl = (MultiSelectControl)existingControl;
				MultiSelectControl newMsCtrl = (MultiSelectControl)control;
				if (!isManagedTables()) {
					// 
					// We do not allow to change table name in case of non-managed forms/tables
					//
					newMsCtrl.setTableName(existingMsCtrl.getTableName());
				}				
			} else if (existingControl instanceof SubFormControl) {
				SubFormControl existingSfCtrl = (SubFormControl)existingControl;
				SubFormControl newSfCtrl = (SubFormControl)control;
				
				existingSfCtrl.setForeignKey(newSfCtrl.getForeignKey());
				existingSfCtrl.setParentKey(newSfCtrl.getParentKey());
				existingSfCtrl.setPathLink(newSfCtrl.isPathLink());
				existingSfCtrl.setCustomFieldsInfo(newSfCtrl.getCustomFieldsInfo());
				existingSfCtrl.setFlatten(newSfCtrl.isFlatten());
				existingSfCtrl.setShowWhenExpr(newSfCtrl.getShowWhenExpr());
				
				if (!isManagedTables()) {
					newSfCtrl.setTableName(existingSfCtrl.getTableName());
				}
												
				if (bulkEdit) {
					Container newSfContainer = newSfCtrl.getSubContainer();
					Container existingSfContainer = existingSfCtrl.getSubContainer();
					newSfContainer.setId(existingSfContainer.getId());
					newSfContainer.disableDeletedCtrlsTracking(disableDeletedCtrlsTracking);
					if (!isManagedTables()) {
						newSfContainer.setDbTableName(existingSfContainer.getDbTableName());
					} else {
						existingSfContainer.setManagedTables(true); // perlocate down to all sub-forms
					}
					existingSfContainer.editContainer(newSfContainer);
					existingSfCtrl.setCaption(newSfCtrl.getCaption());
					existingSfCtrl.setUserDefinedName(newSfCtrl.getUserDefinedName());
					existingSfCtrl.setSequenceNumber(newSfCtrl.getSequenceNumber());
					control = existingSfCtrl;
				}				
			}
			
			control.setId(existingControl.getId());
			
			if (!isManagedTables()) {
				control.setDbColumnName(existingControl.getDbColumnName());
			}			
			control.setContainer(this);
			add(editLog, control);			
		}	

		controlsMap.put(control.getName(), control);
		userDefCtrlNames.add(control.getUserDefinedName());
	}
	
	public void deleteControl(String name) {
		throwExceptionIfDto();
		
		Control existingControl = controlsMap.remove(name);
		if (existingControl == null) {
			// change this exception to status code
			throw new FormException("Control with name doesn't exist: " + name);
		}
		userDefCtrlNames.remove(existingControl.getUserDefinedName());
		
		if (!remove(addLog, existingControl)) {
			add(delLog, existingControl);
			remove(editLog, existingControl);		
		}
		
		//
		// TODO: Remove skip rules of deleted controls
		//
	}

	public List<Control> getDeletedCtrls() {
		if (deletedCtrls == null) {
			deletedCtrls = new ArrayList<>();
		}

		return deletedCtrls;
	}

	public void addToUndoDeleteList(String udn) {
		undoDeletesList.add(udn);
	}

	public void disableDeletedCtrlsTracking(boolean input) {
		this.disableDeletedCtrlsTracking = input;
	}
	
	public Control getControl(String name) {
		if (name.equals(primaryKeyCtrlName)) {
			return getPrimaryKeyControl();
		}

		return controlsMap.get(name);
	}
	
	public Control getControlByUdn(String userDefName) {
		if (userDefName.equals(primaryKeyCtrlName)) {
			return getPrimaryKeyControl();
		}

		if (!userDefCtrlNames.contains(userDefName)) {
			return null;
		}
		
		Control result = null;		
		for (Control ctrl : getControls()) {
			if (ctrl.getUserDefinedName().equals(userDefName)) {
				result = ctrl;
				break;
			}
		}
		
		return result;
	}

	public NumberField getPrimaryKeyControl() {
		NumberField field = new NumberField();
		field.setName(primaryKeyCtrlName);
		field.setUserDefinedName(primaryKeyCtrlName);
		field.setContainer(this);
		field.setDbColumnName(getPrimaryKey());
		field.setCaption(getCaption() + " ID");
		return field;
	}

	public Long save(UserContext userCtxt) {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		return save(userCtxt, jdbcDao);
	}
	
	public Long save(UserContext userCtxt, JdbcDao jdbcDao) {		
		throwExceptionIfDto();

		ContainerDao dao = new ContainerDao(jdbcDao);
		validateContainer(dao);

		try {
			boolean insert = (id == null);
			if (!insert) {
				FormEventsNotifier.getInstance().notifyPreUpdate(this);
			}

			executeDDLWithoutTxn(jdbcDao);
			
			int numIds = 0;
			List<Container> subContainers = getAllSubContainers();
			
			if (id == null) {
				numIds = 1 + subContainers.size();
			} else {
				for (Container c : subContainers) {
					if (c.getId() == null) {
						++numIds;
					}
				}
			}

			List<Long> ids = null;			
			if (numIds > 0) {
				ids = dao.getContainerIds(numIds);
			}
			
			
			int i = 0;
			if (id == null) {
				id = ids.get(i++);
			}
			
			for (Container c : subContainers) {
				if (c.getId() == null) {
					c.setId(ids.get(i++));
				}
			}		

			setSkipControlFlags();
			
			if (insert) {
				dao.insert(userCtxt, this);
				FormEventsNotifier.getInstance().notifyCreate(this);
			} else {
				dao.update(userCtxt, this);
				FormEventsNotifier.getInstance().notifyUpdate(this);
			}
			
			ContainerCache.getInstance().remove(id);
			return id;			
		} catch (Exception e) {
			throw new FormException("Error saving container", e);
		} 
	}
	
	public static boolean deleteContainer(UserContext userCtxt, Long id) {
		return deleteContainer(userCtxt, id, false);
	}
	
	public static boolean softDeleteContainer(UserContext userCtxt, Long id) {
		return deleteContainer(userCtxt, id, true);
	}

	public static boolean deleteContainer(UserContext userCtxt, Long id, boolean softDelete) {
		Container container = getContainer(id);
		if (container == null) {
			return false;
		}


		boolean deleted = new ContainerDao(JdbcDaoFactory.getJdbcDao()).delete(userCtxt, container, softDelete);
		if (deleted) {
			ContainerCache.getInstance().remove(container.getId());
			FormEventsNotifier.getInstance().notifyDelete(container);
		}

		return deleted;
	}
				
	public static Container getContainer(Long id) {
		return getContainer(JdbcDaoFactory.getJdbcDao(), id);
	}
	
	public static Container getContainer(JdbcDao jdbcDao, Long id) {
		long t1 = Calendar.getInstance().getTimeInMillis();
		Container container = null;
		try {
			container = ContainerCache.getInstance().get(id);
			if (container == null) {
				// not present in cache, hit the database
				ContainerDao containerDao = new ContainerDao(jdbcDao);
				container = containerDao.getById(id);
				if (container != null) {
					// cache the form for future use
					ContainerCache.getInstance().put(container);
				}
			}

			return container;
		} catch (FormException iae) {
			throw new FormException("Error obtaining container: " + id + ". " + iae.getMessage(), iae);
		} catch (Exception e) {
			throw new FormException("Error obtaining container: " + id, e);
		} finally {
			if (container != null) {
				logger.info("Time taken to load the form " + container.getName() + " is: " + (Calendar.getInstance().getTimeInMillis() - t1) + " ms");
			}
		}
	}
	
	public static Container getContainer(String name) {
		return getContainer(JdbcDaoFactory.getJdbcDao(), name);
	}
	
	public static Container getContainer(JdbcDao jdbcDao, String name) {
		long t1 = Calendar.getInstance().getTimeInMillis();

		Container container = null;
		try {
			container = ContainerCache.getInstance().get(name);
			if (container == null) {
				ContainerDao containerDao = new ContainerDao(jdbcDao);
				container = containerDao.getByName(name);
				if (container != null) {
					ContainerCache.getInstance().put(container);
				}
			}

			return container;
		} catch (FormException iae) {
			throw new FormException("Error obtaining container: " + name + ". " + iae.getMessage(), iae);
		} catch (Exception e) {
			throw new FormException("Error obtaining container: " + name, e);
		} finally {
			logger.info("Time taken to load the form " + name + " is: " + (Calendar.getInstance().getTimeInMillis() - t1) + " ms");
		}
	}
		
	public static List<ContainerInfo> getContainerInfo() {
		try {
			ContainerDao dao = new ContainerDao(JdbcDaoFactory.getJdbcDao());
			return dao.getContainerInfo();
		} catch (Exception e) {
			throw new FormException("Error obtaining container info", e);
		}		
	}
	
	public static List<ContainerInfo> getContainerInfoByCreator(Long creatorId) {
		try {
			ContainerDao dao = new ContainerDao(JdbcDaoFactory.getJdbcDao());
			return dao.getContainerInfoByCreator(creatorId);
		} catch (Exception e) {
			throw new FormException("Error obtaining container info by creator id: " + creatorId, e);
		}
	}
	
	public static Long createContainer(String formXml, String pvDir) 
	throws Exception {
		return createContainer(formXml, pvDir, true);
	}
	
	public static Long createContainer(String formXml, String pvDir, boolean createTables)
	throws Exception {
		return createContainer(null, formXml, pvDir, createTables);
	}
	
	public static Long createContainer(UserContext ctxt, String formXml, String pvDir, boolean createTables)
	throws Exception {
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container parsedContainer = parser.parse();
		return createContainer(ctxt, parsedContainer, createTables);
	}
	
	public static Long createContainer(UserContext ctxt, InputStream in, boolean createTables) 
	throws Exception {
		ContainerParser parser = new ContainerParser(in);
		Container parsedContainer = parser.parse();
		return createContainer(ctxt, parsedContainer, createTables);
	}

	public static Long createContainer(UserContext ctxt, Container parsedContainer, boolean createTables) {
		Container existingContainer = null;		
		if (parsedContainer.getId() != null) {
			existingContainer = getContainer(parsedContainer.getId());
		}
		
		if (existingContainer == null) { 
			if (parsedContainer.getName() != null) {
				existingContainer = getContainer(parsedContainer.getName());
			}
		}
		
		Container container = null;
		if (existingContainer != null) {
			existingContainer.editContainer(parsedContainer);
			container = existingContainer;
		} else if (parsedContainer.isDto) {
			container = fromDto(parsedContainer);
			container.setManagedTables(!createTables);
		} else {
			container = parsedContainer;
		}

		return container.save(ctxt);
	}
				
	public void editContainer(Container newContainer) {
		if (!this.getName().equals(newContainer.getName())) {
			throw new FormException("Error : Container name cannot be edited");
		}
		
		if (isManagedTables()) {
			setDbTableName(newContainer.getDbTableName());
		}
		
		setName(newContainer.getName());
		setCaption(newContainer.getCaption());
		
		if (newContainer.getPrimaryKey() != null) {
			this.setPrimaryKey(newContainer.getPrimaryKey());
		}
		
		setHierarchyTable(newContainer.getHierarchyTable());
		setHierarchyAncestorCol(newContainer.getHierarchyAncestorCol());
		setHierarchyDescendentCol(newContainer.getHierarchyDescendentCol());
		setActiveCond(newContainer.getActiveCond());
		disableDeletedCtrlsTracking(newContainer.disableDeletedCtrlsTracking);

		if (disableDeletedCtrlsTracking) {
			getDeletedCtrls().clear();
		}
		
		for (Control  ctrl : newContainer.getControls()) {
			if (getControl(ctrl.getName()) == null) {
				addControl(ctrl);
			} else {
				editControl(ctrl.getName(), ctrl, true);				
			}			
		}
		
		deleteRemovedControls(newContainer);


		for (String undoUdn : newContainer.undoDeletesList) {
			undoDelete(undoUdn);
		}
		
		//
		// TODO: Simply copying skip rules of new container.
		// Check whether this is correct way to handle?
		// Probably check for skip rules referring to non-existing fields
		//
		this.skipRules.clear();
		this.skipRules.addAll(newContainer.getSkipRules());

		if (this.layouts != null) {
			this.layouts.clear();
		} else {
			this.layouts = new ArrayList<>();
		}

		if (newContainer.getLayouts() != null) {
			this.layouts.addAll(newContainer.getLayouts());
		}
	}

	protected void deleteRemovedControls(Container newContainer) {
		Collection<Control> existingCtrls = getControls();
		Collection<Control> removedCtrls = new ArrayList<Control>();
		for (Control ctrl : existingCtrls) {
			if (newContainer.getControl(ctrl.getName()) == null) {
				//deleteControl(ctrl.getName());
				removedCtrls.add(ctrl);
			}			
		}
		
		for (Control removedCtrl : removedCtrls) {
			deleteControl(removedCtrl.getName());
		}

		if (!disableDeletedCtrlsTracking) {
			getDeletedCtrls().addAll(removedCtrls);
		}
	}

	protected void undoDelete(String undoUdn) {
		String[] parts = undoUdn.split("\\.", 2);
		if (parts.length == 1) {
			int idx = -1;
			for (Control ctrl : getDeletedCtrls()) {
				++idx;
				if (ctrl.getUserDefinedName().equals(parts[0])) {
					break;
				}
			}

			if (idx < 0 || idx >= getDeletedCtrls().size()) {
				throw new FormException("No control with UDN '" + parts[0] + "' is in deleted state.");
			}


			Control ctrl = getDeletedCtrls().remove(idx);
			if (ctrl.getSequenceNumber() > sequenceNo ) {
				sequenceNo = ctrl.getSequenceNumber();
			} else {
				ctrl.setSequenceNumber(++sequenceNo);
			}

			controlsMap.put(ctrl.getName(), ctrl);
			userDefCtrlNames.add(ctrl.getUserDefinedName());
		} else {
			Control ctrl = getControlByUdn(parts[0]);
			if (ctrl == null) {
				throw new FormException("No control with UDN: '" + parts[0] + "' exists.");
			}

			if (ctrl instanceof SubFormControl) {
				Container subForm = ((SubFormControl) ctrl).getSubContainer();
				subForm.undoDelete(parts[1]);
			} else {
				throw new FormException("Control with UDN '" + parts[0] + "' is not a subform.");
			}
		}
	}
	
	protected void executeDDLWithoutTxn(JdbcDao jdbcDao) 
	throws Exception {
		Transaction txn = TransactionManager.getInstance().newTxn();
		try {
			executeDDL(jdbcDao, null);
		} finally {
			if (txn != null) {
				TransactionManager.getInstance().commit(txn);
			}
		}
	}
	

	protected void executeDDL(JdbcDao jdbcDao, String parentTableName) {
		if (isManagedTables()) {
			return;
		}
		
		//
		// 1. Execute DDL referring to addLog, editLog and delLog
		//
		List<ColumnDef> columnDefs = new ArrayList<>();
		for (Control ctrl : addLog) {
			if (isNonDataField(ctrl) || isMultiValued(ctrl) || isOneToOneNonInverse(ctrl)) {				
				continue;
			}
			
			columnDefs.addAll(ctrl.getColumnDefs());
		}

		if (id == null) {
			dbTableName = getUniqueTableName();

			List<String> indexCols = new ArrayList<>();
			if (parentTableName != null) {
				columnDefs.add(ColumnDef.get("PARENT_RECORD_ID", ColumnTypeHelper.getIntegerColType()));
				indexCols.add("PARENT_RECORD_ID");
			}
			
			createTable(jdbcDao, dbTableName, columnDefs, indexCols, true);
		} else {
			addTableColumns(jdbcDao, dbTableName, columnDefs);
		}
				
		for (Control ctrl : addLog) {
			if (ctrl instanceof MultiSelectControl) {
				MultiSelectControl mCtrl = (MultiSelectControl)ctrl;
				mCtrl.setTableName(getUniqueTableName());

				List<String> indexCols = Collections.singletonList("RECORD_ID");
				createTable(jdbcDao, mCtrl.getTableName(), ctrl.getColumnDefs(), indexCols, false);
			} else if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				sfCtrl.getSubContainer().executeDDL(jdbcDao, dbTableName);
			}
		}
		
		for (Control ctrl : editLog) {
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				sfCtrl.getSubContainer().executeDDL(jdbcDao, dbTableName);
			}
		}

		//
		// 2. clear logs once done
		//
		addLog.clear();
		delLog.clear();
		editLog.clear();		
	}
	
	private void setSkipControlFlags() {
		for (SkipRule rule : skipRules) {
			for (SkipCondition cond : rule.getConditions()) {
				cond.getSourceControl().setSkipLogicSourceControl(true);
			}
			
			for (SkipAction action : rule.getActions()) {
				action.getTargetCtrl().setSkipLogicTargetControl(true);
			}
		}
	}
	
	private void createTable(JdbcDao jdbcDao, String tableName, List<ColumnDef> columnDefs, List<String> indexCols, boolean crtIdColumn) {
		StringBuilder ddl = new StringBuilder();
		ddl.append("CREATE TABLE ").append(tableName).append(" (");
		
		if (crtIdColumn) {
			ddl.append("IDENTIFIER ").append(ColumnTypeHelper.getIntegerColType()).append(" PRIMARY KEY, ");
		}
		
		for (ColumnDef columnDef : columnDefs) {
			ddl.append(columnDef.getColumnName()).append(" ").append(columnDef.getDbType()).append(", ");
		}		
		ddl.replace(ddl.length() - 2, ddl.length(), ")");
		
		jdbcDao.executeDDL(ddl.toString());

		if (indexCols != null) {
			for (String indexCol : indexCols) {
				jdbcDao.executeDDL(String.format(CREATE_IDX_DDL, tableName, indexCol, tableName, indexCol));
			}
		}

		addFks(jdbcDao, tableName, columnDefs);
	}
	
	private void addTableColumns(JdbcDao jdbcDao, String tableName, List<ColumnDef> columnDefs) {
		if (columnDefs == null || columnDefs.isEmpty()) {
			return;
		}
		
		StringBuilder ddl = new StringBuilder();
		ddl.append("ALTER TABLE ").append(tableName).append(" ADD (");
		for (ColumnDef columnDef :columnDefs) {
			ddl.append(columnDef.getColumnName()).append(" ").append(columnDef.getDbType()).append(", ");
		}
		ddl.delete(ddl.length() - 2, ddl.length());
		ddl.append(")");
		
		jdbcDao.executeDDL(ddl.toString());
		addFks(jdbcDao, tableName, columnDefs);
	}

	private void addFks(JdbcDao jdbcDao, String tableName, List<ColumnDef> columnDefs) {
		for (ColumnDef def : columnDefs) {
			if (def.getRefTable() == null || def.getRefColumn() == null) {
				continue;
			}

			try {
				jdbcDao.executeDDL(String.format(
					ADD_FK, tableName, "FK_" + tableName + "_" + def.getColumnName(), def.getColumnName(),
					def.getRefTable(), def.getRefColumn()
				));
			} catch (Throwable t) {
				logger.error("Error adding foreign key", t);
			}
		}
	}
	
	private void add(List<Control> list, Control ctrl) {
		list.add(ctrl);
	}
	
	private boolean remove(List<Control> list, Control ctrl) {
		boolean removed = false;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i).getName().equals(ctrl.getName())) {
				list.remove(i);
				removed = true;
				break;
			}
		}
		
		return removed;
	}
	
	private void throwExceptionIfDto() {
		if (isDto) {
			throw new FormException("Cannot invoke this operation on DTO");
		}
	}

	private void validateContainer(ContainerDao dao) {
		validateContainer(dao, false);
	}

	private void validateContainer(ContainerDao dao, boolean subForm) {
		if (StringUtils.isBlank(name)) {
			throw new FormException("Form name cannot be empty or blank!");
		}

		if (Character.isDigit(name.trim().charAt(0))) {
			throw new FormException("Form names like " + name + " starting with numeric characters are not allowed!");
		}

		if (notAllowed.matcher(name).find()) {
			throw new FormException(
				"Form name " + name + " contains special characters. " +
				"Special characters including spaces are not allowed.");
		}

		for (Control ctrl : getControls()) {
			validateNameAndUdn(ctrl);
			if (ctrl instanceof SubFormControl) {
				((SubFormControl) ctrl).getSubContainer().validateContainer(dao, true);
			}
		}

		if (subForm) {
			return;
		}

		try {
			Long dbId = dao.getIdByName(name);
			if (!Objects.equals(id, dbId)) {
				throw new FormException("Form with the same name " + name + " already exists");
			}
		} catch (SQLException e) {
			throw new FormException("Error querying database", e);
		}
	}
	
	protected void clearLogs() {
		addLog.clear();
		editLog.clear();
		delLog.clear();
	}
	
	public String toXml() {
//		XStream xstream = new XStream(new KXml2Driver());
		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		xstream.addPermission(NoTypePermission.NONE);
		xstream.addPermission(AnyTypePermission.ANY);

		setUpAliases(xstream);
		return xstream.toXML(this);
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
		
	public static Container fromXml(String xml) {
//		XStream xstream = new XStream(new KXml2Driver());
		try {
			XStream xstream = new XStream(new DomDriver());
			xstream.setMode(XStream.ID_REFERENCES);
			xstream.addPermission(NoTypePermission.NONE);
			xstream.addPermission(AnyTypePermission.ANY);

			setUpAliases(xstream);

			Container container = (Container)xstream.fromXML(xml);
			container.initLogs(); // for some reason, xstream is not initializing add/edit/deleteLogs of container
			return container;
		} catch (XStreamException xse) {
			throw new FormException("Error parsing container definition: " + xse.getMessage());
		}
	}
	
	private static Container fromDto(Container dtoContainer) {
		Container container = new Container();
		container.setName(dtoContainer.getName());
		container.setCaption(dtoContainer.getCaption());
		container.setDbTableName(dtoContainer.getDbTableName());
		container.setPrimaryKey(dtoContainer.getPrimaryKey());
		container.setHierarchyTable(dtoContainer.getHierarchyTable());
		container.setHierarchyAncestorCol(dtoContainer.getHierarchyAncestorCol());
		container.setHierarchyDescendentCol(dtoContainer.getHierarchyDescendentCol());
		container.setActiveCond(dtoContainer.getActiveCond());
		
		for (Control ctrl : dtoContainer.addLog) {
			container.addControl(ctrl);
		}		
		
		container.skipRules.addAll(dtoContainer.getSkipRules());
		return container;
	}
		
	private static void setUpAliases(XStream xstream) {
		xstream.alias("container", Container.class);
		xstream.alias("datePicker", DatePicker.class);
		xstream.alias("fileUpload", FileUploadControl.class);
		xstream.alias("label", Label.class);
		xstream.alias("subForm", SubFormControl.class);
		xstream.alias("checkBox", CheckBox.class);
		xstream.alias("comboBox", ComboBox.class);
		xstream.alias("listBox", ListBox.class);
		xstream.alias("radioButton", RadioButton.class);
		xstream.alias("numberField", NumberField.class);
		xstream.alias("stringField", StringTextField.class);
		xstream.alias("textArea", TextArea.class);
		xstream.alias("signature", SignatureControl.class);
		xstream.alias("pageBreak", PageBreak.class);
		
		xstream.alias("validationRule", ValidationRule.class);
		xstream.alias("skipRule", SkipRule.class);
		xstream.alias("skipCondition", SkipCondition.class);
		xstream.alias("logicalOp", LogicalOp.class);
		xstream.alias("relationalOp", RelationalOp.class);
		xstream.alias("showAction", ShowAction.class);
		xstream.alias("hideAction", HideAction.class);
		xstream.alias("enableAction", EnableAction.class);
		xstream.alias("disableAction", DisableAction.class);
		xstream.alias("showPvAction", ShowPvAction.class);
		xstream.alias("permissibleValue", PermissibleValue.class);
		xstream.alias("layout", Layout.class);
		xstream.alias("page", Page.class);
		xstream.alias("pageRow", PageRow.class);
		xstream.alias("pageField", PageField.class);
	}
	
	private String getUniqueTableName() {
		return String.format(tableNameFmt, getUniqueId());
	}
	
	// TODO: Hard coded tab name
	private Long getUniqueId() {
		return IdGenerator.getInstance().getNextId("DE_E_TNAMES");
	}
	
	private void getAllControls(Container container, List<Control> controls) {	
		for (Control control : container.getOrderedControlList()) {
			if (control instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)control;
				getAllControls(sfCtrl.getSubContainer(), controls);
			} else {
				controls.add(control);
			}
		}
	}

	public static void main(String[] args) {
		Container c = new Container();
		c.setName("Person Demographics");
		
		StringTextField txtField = new StringTextField();
		txtField.setName("First Name");
		c.addControl(txtField);
		
		System.err.println(c.toXml());
	}
		
	/**
	 * FIXME this is a temporary method, this field should be configurable as it was in the legacy model
	 * @return
	 */
	protected String getRequiredFieldIndicatior() {
		return "*";
	}

	public List<Control> getOrderedControlList() {
		final List<Control> controls = new ArrayList<Control>(getControls());
		java.util.Collections.sort(controls);
		return controls;
	}

	public List<Control> getUniqueControls() {
		return getOrderedControlList().stream()
			.filter(ctrl -> ctrl.isUnique() &&
				!(ctrl instanceof SubFormControl) &&
				!(ctrl instanceof MultiSelectControl) &&
				!(ctrl instanceof PageBreak) &&
				!(ctrl instanceof Label))
			.collect(Collectors.toList());
	}

	private void initLogs() {
		List<Container> containers = new ArrayList<Container>();
		containers.add(this);
		
		while (!containers.isEmpty()) {
			Container container = containers.remove(0);
			container.addLog = new ArrayList<Control>();
			container.editLog = new ArrayList<Control>();
			container.delLog = new ArrayList<Control>();
			
			for (Control ctrl : container.getControls()) {
				if (ctrl instanceof SubFormControl) {
					SubFormControl sfCtrl = (SubFormControl)ctrl;
					containers.add(sfCtrl.getSubContainer());
				}
			}			
		}		
	}
	 	
	//
	// Works if the input control belongs to container
	// on which this method is invoked
	//
	public String getControlCanonicalName(Control ctrl) {
		if (ctrl.getName() == null) {
			throw new FormException("Control name is null. Invalid control state");
		}
		
		Control formCtrl = controlsMap.get(ctrl.getName());
		if (formCtrl != null && formCtrl.getContainer() == this) {
			return formCtrl.getName();
		}
		
		for (Control c : getControls()) {
			if (!(c instanceof SubFormControl)) {
				continue;
			}
			
			SubFormControl sfCtrl = (SubFormControl)c;
			String name = sfCtrl.getSubContainer().getControlCanonicalName(ctrl);
			if (name != null) {
				return new StringBuilder(sfCtrl.getName()).append(".").append(name).toString();
			}
		}
		
		return null;		
	}
	
	public String getControlCanonicalUdn(Control ctrl) {
		if (ctrl.getUserDefinedName() == null) {
			throw new FormException("User defined name of control is null. Invalid control state");
		}
		
		Control formCtrl = controlsMap.get(ctrl.getName());
		if (formCtrl != null && formCtrl.getContainer() == this) {
			return formCtrl.getUserDefinedName();
		}
		
		for (Control c : getControls()) {
			if (!(c instanceof SubFormControl)) {
				continue;
			}
			
			SubFormControl sfCtrl = (SubFormControl)c;
			String userDefName = sfCtrl.getSubContainer().getControlCanonicalUdn(ctrl);
			if (userDefName != null) {
				return new StringBuilder(sfCtrl.getUserDefinedName()).append(".").append(userDefName).toString();
			}
		}
		
		return null;		
	}
		
	public Control getControl(String controlName, String separator) {
		Container container = this;
		Control ctrl = container.getControl(controlName);
		if (ctrl != null) {
			return ctrl;
		}
		
		String[] controlNameParts = controlName.split(separator);
		if (controlNameParts.length == 1) { // no sub form control name
			throw new FormException("Invalid control name: " + controlName);
		}
		
		for (int i = 0; i < controlNameParts.length - 1; ++i) {
			ctrl = container.getControl(controlNameParts[i]);
			if (!(ctrl instanceof SubFormControl)) {
				throw new FormException("Invalid control name: " + controlName);
			}
			
			SubFormControl sfCtrl = (SubFormControl)ctrl;
			container = sfCtrl.getSubContainer();			
		}
		
		ctrl = container.getControl(controlNameParts[controlNameParts.length - 1]);
		if (ctrl == null) {
			throw new FormException("Invalid control name: " + controlName);
		}
		
		return ctrl;
	}
	
	public Control getControlByUdn(String userDefinedName, String separator) {
		Container container = this;
		Control ctrl = container.getControlByUdn(userDefinedName);
		if (ctrl != null) {
			return ctrl;
		}
		
		String[] udnParts = userDefinedName.split(separator);
		if (udnParts.length == 1) { // no sub form control name
			throw new FormException("Invalid user defined name: " + userDefinedName);
		}
		
		for (int i = 0; i < udnParts.length - 1; ++i) {
			ctrl = container.getControlByUdn(udnParts[i]);
			if (!(ctrl instanceof SubFormControl)) {
				throw new FormException("Invalid user defined name: " + userDefinedName);
			}
			
			SubFormControl sfCtrl = (SubFormControl)ctrl;
			container = sfCtrl.getSubContainer();			
		}
		
		ctrl = container.getControlByUdn(udnParts[udnParts.length - 1]);
		if (ctrl == null) {
			throw new FormException("Invalid user defined name: " + userDefinedName);
		}
		
		return ctrl;
	}
	

	public SubFormControl getSubFormControl(String name) {
		SubFormControl subFormControl = null;
		for (Control control : getControls()) {
			if (!(control instanceof SubFormControl)) {
				continue;
			}
			SubFormControl sfCtl = (SubFormControl) control;
			if (name.equals(sfCtl.getSubContainer().getName())) {
				subFormControl = sfCtl;
				break;
			}

		}
		return subFormControl;
	}
	
	public static String getContainerNameById(Long id) {
		return getContainerNameById(JdbcDaoFactory.getJdbcDao(), id);
	}

	public static String getContainerNameById(JdbcDao jdbcDao, Long id) {
		try {
			ContainerDao containerDao = new ContainerDao(jdbcDao);
			return containerDao.getNameById(id);
		} catch (Exception e) {
			throw new FormException("Error obtaining container name with id : " + id);
		}
	}
	
	public Map<String, Object> getProps() {
		Map<String, Object> props = new HashMap<>();
		props.put("name", getName());
		props.put("caption", getCaption());
		putControls(props);

		if (getLayouts() != null) {
			List<Map<String, Object>> layoutProps = new ArrayList<>();
			for (Layout layout : getLayouts()) {
				layoutProps.add(layout.getProps());
			}

			props.put("layouts", layoutProps);
		}

		return props;
	}
	
	//
	// Indicates whether the form's table is managed by DE
	// or host application
	//
	public boolean isManagedTables() {
		return managedTables;
	}
	
	public void setManagedTables(boolean managedTables) {
		this.managedTables = managedTables;
	}
			
	private void putControls(Map<String, Object> containerProps) {
		List<List<Map<String, Object>>> rows = new ArrayList<List<Map<String, Object>>>();
		containerProps.put("rows", rows);
		
		for (List<Control> rowCtrls : getControlsGroupedByRow()) {
			List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
			for (Control ctrl : rowCtrls) {
				row.add(ctrl.getProps());
			}
			
			rows.add(row);
		}		
	}
	
	private boolean isMultiValued(Control ctrl) {
		if (ctrl instanceof MultiSelectControl) {
			return true;
		}
		
		if (ctrl instanceof SubFormControl) {
			SubFormControl sfCtrl = (SubFormControl)ctrl;
			return !sfCtrl.isOneToOne();
		}
		
		return false;
	}
	
	private boolean isOneToOneNonInverse(Control ctrl) {
		if (!(ctrl instanceof SubFormControl)) {
			return false;
		}
		
		SubFormControl sfCtrl = (SubFormControl)ctrl;
		return sfCtrl.isOneToOne() && !sfCtrl.isInverse();
	}
	
	private boolean isNonDataField(Control ctrl) {
		return ctrl instanceof Label || ctrl instanceof PageBreak;
	}
}
