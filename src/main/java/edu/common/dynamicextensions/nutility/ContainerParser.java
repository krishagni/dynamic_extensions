package edu.common.dynamicextensions.nutility;

import static edu.common.dynamicextensions.nutility.ParserUtil.getLongValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Layout;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.Page;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PageField;
import edu.common.dynamicextensions.domain.nui.PageRow;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRuleBuilder;
import edu.common.dynamicextensions.domain.nui.SkipRuleBuilder.ActionBuilder;
import edu.common.dynamicextensions.domain.nui.SkipRuleBuilder.ConditionBuilder;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.factory.ControlFactory;
import edu.common.dynamicextensions.domain.nui.factory.ControlManager;
import edu.common.dynamicextensions.napi.FormException;

public class ContainerParser {

	private final String formXml;
	
	private Properties props = new Properties();
	
	private InputStream formXmlIn;

	public ContainerParser(String pvDir) {
		this(null, pvDir);
	}
	
	public ContainerParser(String formXml, String pvDir) {
		this.formXml = formXml;
		props.setProperty("pvDir", pvDir);		
	}
	
	public ContainerParser(InputStream in) {
		this.formXmlIn = in;
		this.formXml = null;
		props.setProperty("pvDir", "");
	}
		
	public Container parse() throws Exception {
		if ((formXml == null || formXml.trim().isEmpty()) && formXmlIn == null) {
			throw new FormException("Cannot call this method without form definition file");
		}
				
		InputStream formIn = null;
		try {
			if (formXml != null) {
				formIn = new FileInputStream(formXml);
			} else {
				formIn = formXmlIn; 
			}
			
			return parse(formIn);
		} finally {
			IoUtil.close(formIn);
		}
	}
	
	public Container parse(InputStream formIn) throws Exception {		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(formIn);
		doc.getDocumentElement().normalize();

		NodeList nodes = doc.getElementsByTagName("view");
		Node viewNode = nodes.item(0);

		Container container = parse((Element) viewNode, true);
		nodes = doc.getElementsByTagName("undoDeletedFields");
		if (nodes != null && nodes.getLength() == 1) {
			parseAndSetUndoDeletesList(container, (Element) nodes.item(0));
		}

		nodes = doc.getElementsByTagName("skipRules");
		if (nodes != null && nodes.getLength() == 1) {
			parseAndSetSkipRules(container, (Element) nodes.item(0));
		}

		nodes = doc.getElementsByTagName("layouts");
		if (nodes != null && nodes.getLength() == 1) {
			parseAndSetLayouts(container, (Element) nodes.item(0));
		}

		nodes = doc.getElementsByTagName("hiddenFields");
		if (nodes != null && nodes.getLength() > 0) {
			parseAndSetHiddenFields(container, nodes);
		}

		updateCalculatedSourceControls(container, container);
		return container;
	}

	public Container parse(Element viewElement, boolean topLevel) {
		Container container = new Container();
		int currentRow = 0;
		
		container.useAsDto();
		setContainerProps(container, viewElement);
		
		NodeList viewNodes = viewElement.getChildNodes();
		for (int i = 0; i < viewNodes.getLength(); ++i) {
			if (viewNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			Node row = viewNodes.item(i);
			if (row.getNodeName().equals("row")) {
				++currentRow;
				List<Control> controls = parseFormRow(row, currentRow);
				for (Control control : controls) {
					container.addControl(control);
				}				
			} else if (topLevel && row.getNodeName().equals("pageBreak")) {
				++currentRow;
				PageBreak pageBreak = new PageBreak();
				pageBreak.setName("_pgBrk_" + currentRow);
				pageBreak.setUserDefinedName("_pgBrk_" + currentRow);
				pageBreak.setSequenceNumber(currentRow);
				container.addControl(pageBreak);
			}
		}
		
		return container;
	}
	
	private void updateCalculatedSourceControls(Container container, Container rootContainer) {
		FormulaParser formulaParser = new FormulaParser();
		for (Control control : container.getControls()) {
			if (control instanceof NumberField && ((NumberField) control).isCalculated()) {
				updateSourceCalculatedControls(rootContainer, formulaParser, control);

			} else if (control instanceof SubFormControl) {
				updateCalculatedSourceControls(((SubFormControl) control).getSubContainer(), rootContainer);
			}
		}
	}

	private void updateSourceCalculatedControls(Container rootContainer, FormulaParser formulaParser, Control control) {
		formulaParser.parseExpression(((NumberField) control).getFormula());
		for (String symbol : formulaParser.getSymbols()) {
			Control sourceControl = rootContainer.getControl(symbol,"\\.");
			sourceControl.setCalculatedSourceControl(true);
		}
	}

	private void setContainerProps(Container container, Element viewElement) {
		Long id = getLongValue(viewElement, "id");
		container.setId(id);

		String name = getTextValue(viewElement, "name");
		container.setName(name);
		
		String caption = getTextValue(viewElement, "caption");
		if (caption == null) {
			throw new FormException("Form caption can't be null");
		}
		container.setCaption(caption);		
		
		String table = getTextValue(viewElement, "table");
		if (table != null) {
			container.setDbTableName(table);
		}
		
		container.setPrimaryKey(getTextValue(viewElement, "primaryKey", "IDENTIFIER"));
		container.setHierarchyTable(getTextValue(viewElement, "hierarchyTable"));
		container.setHierarchyAncestorCol(getTextValue(viewElement,"hierarchyAncestorColumn"));
		container.setHierarchyDescendentCol(getTextValue(viewElement,"hierarchyDescendentColumn"));
		container.setActiveCond(getTextValue(viewElement, "activeCondition"));
	}
	
	private List<Control> parseFormRow(Node row, int currentRow) {
		List<Control> controls = new ArrayList<Control>();		
		NodeList ctrlNodes = row.getChildNodes();
		int xpos = 0;
		for (int i = 0; i < ctrlNodes.getLength(); ++i) {
			if (ctrlNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			xpos++;
			Element ctrlEle = (Element)ctrlNodes.item(i);
			String ctrlName = ctrlEle.getNodeName();
			ControlFactory factory = ControlManager.getInstance().getFactory(ctrlName);
			if (factory == null) {
				throw new FormException("Invalid control type: " + ctrlName);
			}
			
			controls.add(factory.parseControl(ctrlEle, currentRow, xpos, props));
		}
		
		return controls;		
	}
			
	private void parseAndSetSkipRules(Container container, Element skipRulesEle) {
		NodeList skipRules = skipRulesEle.getElementsByTagName("skipRule");
		if (skipRules == null) {
			return;
		}

		for(int i = 0 ; i < skipRules.getLength() ; i++) {
			Node node = skipRules.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			SkipRule rule = createSkipRule(container, (Element)node);
			container.addSkipRule(rule);
		}
	}
		
	private SkipRule createSkipRule(Container container, Element skipRuleEle) {
		SkipRuleBuilder ruleBuilder = container.newSkipRule();
		ConditionBuilder condBuilder = null;
		
		NodeList exprs = skipRuleEle.getElementsByTagName("oneOf");
		if (exprs == null || exprs.getLength() == 0) {
			exprs = skipRuleEle.getElementsByTagName("all");
			condBuilder = ruleBuilder.when().allOf();
		} else {
			condBuilder = ruleBuilder.when().anyOf();
		}
		
		if (exprs == null || exprs.getLength() != 1) {
			throw new FormException("More than one expression in skip rule");
		}			

		createSkipConditions(condBuilder, (Element)exprs.item(0));
		
		
		ActionBuilder actionBuilder = condBuilder.then().perform();
		Element actionsEle = (Element)skipRuleEle.getElementsByTagName("actions").item(0);
		NodeList actionList = actionsEle.getChildNodes();
		for (int i = 0; i < actionList.getLength(); ++i) {
			if (actionList.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
						
			Element actionEle = (Element)actionList.item(i);
			String ctrlName = actionEle.getAttribute("field");
					
			if (actionEle.getNodeName().equals("hide")) {
				actionBuilder.hide(ctrlName);
			} else if (actionEle.getNodeName().equals("showPv")) {
				//
				// TODO: Handle default pv
				//				
				List<PermissibleValue> pvs = ParserUtil.getPermissibleValues(actionEle, props.getProperty("pvDir"));
				actionBuilder.subsetPv(ctrlName, pvs, null);
			} else if (actionEle.getNodeName().equals("show")) {
				actionBuilder.show(ctrlName);
			} else if (actionEle.getNodeName().equals("enable")) {
				actionBuilder.enable(ctrlName);
			} else if (actionEle.getNodeName().equals("disable")) {
				actionBuilder.disable(ctrlName);
			}				
		}
		
		return ruleBuilder.get();		
	}
	
	private void createSkipConditions(ConditionBuilder conditionBuilder, Element expressionEle) {	
		NodeList conditionEls = expressionEle.getElementsByTagName("condition");
		for (int i = 0; i < conditionEls.getLength(); ++i) {
			if (conditionEls.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			Element conditionEl = (Element)conditionEls.item(i);
			String op = conditionEl.getAttribute("op").toLowerCase();
			String value = conditionEl.getAttribute("value");
			String ctrlName = conditionEl.getAttribute("field");
			
			if (op.equals("eq")) {
				conditionBuilder.eq(ctrlName, value);
			} else if (op.equals("lt")) {
				conditionBuilder.lt(ctrlName, value);
			} else if (op.equals("le")) {
				conditionBuilder.le(ctrlName, value);
			} else if (op.equals("gt")) {
				conditionBuilder.gt(ctrlName, value);
			} else if (op.equals("ge")) {
				conditionBuilder.ge(ctrlName, value);
			}			
		}
	}

	private void parseAndSetUndoDeletesList(Container container, Element undoDeletesEle) {
		NodeList controls = undoDeletesEle.getElementsByTagName("field");
		if (controls == null) {
			return;
		}

		for(int i = 0 ; i < controls.getLength() ; i++) {
			Node node = controls.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			String udn = ((Element) node).getAttribute("udn");
			if (StringUtils.isNotBlank(udn)) {
				container.addToUndoDeleteList(udn);
			}
		}
	}

	private void parseAndSetLayouts(Container container, Element layoutsEle) {
		NodeList layoutNodes = layoutsEle.getElementsByTagName("layout");
		if (layoutNodes == null || layoutNodes.getLength() == 0) {
			return;
		}

		List<Layout> layouts = new ArrayList<>();
		for (int i = 0; i < layoutNodes.getLength(); ++i) {
			Node node = layoutNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Layout layout = createLayout(container, (Element) node);
			if (layout != null) {
				layouts.add(layout);
			}
		}

		container.setLayouts(layouts);
	}

	private Layout createLayout(Container container, Element layoutEle) {
		NodeList pageNodes = layoutEle.getElementsByTagName("page");
		if (pageNodes == null || pageNodes.getLength() == 0) {
			return null;
		}

		List<Page> pages = new ArrayList<>();
		for (int i = 0; i < pageNodes.getLength(); ++i) {
			Node node = pageNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Page page = createPage(container, (Element) node);
			if (page != null) {
				pages.add(page);
			}
		}

		if (pages.isEmpty()) {
			return null;
		}

		Layout layout = new Layout();
		layout.setName(layoutEle.getAttribute("name"));
		layout.setPages(pages);
		return layout;
	}

	private Page createPage(Container container, Element pageEle) {
		NodeList pageRowNodes = pageEle.getElementsByTagName("pageRow");
		if (pageRowNodes == null || pageRowNodes.getLength() == 0) {
			return null;
		}

		List<PageRow> rows = new ArrayList<>();
		for (int i = 0; i < pageRowNodes.getLength(); ++i) {
			Node node = pageRowNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			PageRow row = createPageRow(container, (Element) node);
			if (row != null) {
				rows.add(row);
			}
		}

		if (rows.isEmpty()) {
			return null;
		}

		Page page = new Page();
		page.setRows(rows);
		return page;
	}

	private PageRow createPageRow(Container container, Element pageRowEle) {
		NodeList fieldNodes = pageRowEle.getElementsByTagName("pageField");
		if (fieldNodes == null || fieldNodes.getLength() == 0) {
			return null;
		}

		List<PageField> fields = new ArrayList<>();
		for (int i = 0; i < fieldNodes.getLength(); ++i) {
			Node node = fieldNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element el = (Element) node;
			String fieldName = el.getAttribute("name");
			Control ctrl = container.getControl(fieldName);
			if (ctrl == null) {
				continue;
			}

			PageField field = new PageField();
			field.setCtrl(ctrl);
			fields.add(field);
		}

		if (fields.isEmpty()) {
			return null;
		}

		PageRow pageRow = new PageRow();
		pageRow.setFields(fields);
		return pageRow;
	}

	private void parseAndSetHiddenFields(Container container, NodeList nodes) {
		for (int i = 0; i < nodes.getLength(); ++i) {
			NodeList hiddenFields = ((Element) nodes.item(i)).getElementsByTagName("field");
			for (int j = 0; j < hiddenFields.getLength(); ++j) {
				if (hiddenFields.item(j).getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				Control ctrl = null;
				Element field = (Element) hiddenFields.item(j);
				String udn = field.getAttribute("udn");
				if (StringUtils.isNotBlank(udn)) {
					ctrl = container.getControlByUdn(udn, "\\.");
				} else {
					String name = field.getAttribute("name");
					if (StringUtils.isNotBlank(name)) {
						ctrl = container.getControl(name, "\\.");
					}
				}

				if (ctrl != null) {
					ctrl.setHidden(true);
				}
			}
		}
	}
}
