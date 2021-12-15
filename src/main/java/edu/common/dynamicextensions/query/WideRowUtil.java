package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.query.ast.AggregateNode;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode;
import edu.common.dynamicextensions.query.ast.ConcatNode;
import edu.common.dynamicextensions.query.ast.CurrentDateNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode;
import edu.common.dynamicextensions.query.ast.DateFormatFuncNode;
import edu.common.dynamicextensions.query.ast.DateIntervalNode;
import edu.common.dynamicextensions.query.ast.DateRangeFuncNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.LiteralValueListNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;
import edu.common.dynamicextensions.query.ast.RoundOffNode;

public class WideRowUtil {
	// returns {tabAlias, pk}
    public static String[] getTabAliasPk(JoinTree rootNode, ExpressionNode exprNode) {    	
    	if (exprNode instanceof LiteralValueNode || 
    		exprNode instanceof LiteralValueListNode ||	
    		exprNode instanceof DateIntervalNode ||
    		exprNode instanceof CurrentDateNode) {
    		return null;
    	} else if (exprNode instanceof FieldNode) {
			FieldNode fieldNode = (FieldNode) exprNode;
			return new String[]{fieldNode.getTabAlias(), fieldNode.getCtrl().getContainer().getPrimaryKey()};
		} else if (exprNode instanceof DateFormatFuncNode) {
			DateFormatFuncNode dateFormatNode = (DateFormatFuncNode) exprNode;
			return getTabAliasPk(rootNode, dateFormatNode.getDateExpr());
    	} else if (exprNode instanceof DateDiffFuncNode) {
			DateDiffFuncNode dateDiffNode = (DateDiffFuncNode) exprNode;
			return getTabAliasPk(rootNode, dateDiffNode.getLeftOperand(), dateDiffNode.getRightOperand());
		} else if (exprNode instanceof DateRangeFuncNode) {
			DateRangeFuncNode dateRangeNode = (DateRangeFuncNode) exprNode;
			return getTabAliasPk(rootNode, dateRangeNode.getDateExpr());
    	} else if (exprNode instanceof AggregateNode) {
			AggregateNode aggNode = (AggregateNode) exprNode;
			return getTabAliasPk(rootNode, aggNode.getField());
		} else if (exprNode instanceof ConcatNode) {
			ConcatNode concatNode = (ConcatNode) exprNode;
			List<String> aliases = new ArrayList<>();
			for (ExpressionNode arg : concatNode.getArgs()) {
				String[] tabAliasPk = getTabAliasPk(rootNode, arg);
				if (tabAliasPk != null) {
					aliases.addAll(Arrays.asList(tabAliasPk));
				}
			}

			return aliases.toArray(new String[0]);
    	} else if (exprNode instanceof ArithExpressionNode) {
    		ArithExpressionNode arithExprNode = (ArithExpressionNode)exprNode;
    		return getTabAliasPk(rootNode, arithExprNode.getLeftOperand(), arithExprNode.getRightOperand());    		
    	} else if (exprNode instanceof RoundOffNode) {
    		return getTabAliasPk(rootNode, ((RoundOffNode)exprNode).getExprNode());
    	}
    	
    	throw new FormException("Unknown expression node type: " + exprNode);
    }
    
    private static String[] getTabAliasPk(JoinTree rootNode, ExpressionNode leftOperand, ExpressionNode rightOperand) {
		String[] leftTabAlias = getTabAliasPk(rootNode, leftOperand);
		String[] rightTabAlias = getTabAliasPk(rootNode, rightOperand);
		
		if (leftTabAlias == null && rightTabAlias == null) {
			return new String[] {rootNode.getAlias(), rootNode.getForm().getPrimaryKey()};
		} else if (leftTabAlias == null && rightTabAlias != null) {
			return rightTabAlias;
		} else if (leftTabAlias != null && rightTabAlias == null) {
			return leftTabAlias;
		} else if (leftTabAlias[0].equals(rightTabAlias[0])) {
			return leftTabAlias;    			
		} else {
			JoinTree leftNode = rootNode.getNode(leftTabAlias[0]);
			JoinTree rightNode = rootNode.getNode(rightTabAlias[0]);
			
			if (leftNode.isAncestorOf(rightNode)) {
				return rightTabAlias;
			} else if (rightNode.isAncestorOf(leftNode)) {
				return leftTabAlias;
			} else {
				JoinTree leftAncestor  = getTopLevelForm(leftNode);
				JoinTree rightAncestor = getTopLevelForm(rightNode);
				if (leftAncestor.isAncestorOf(rightAncestor)) {
					return rightTabAlias;
				} else if (rightAncestor.isAncestorOf(leftAncestor)) {
					return leftTabAlias;
				} else {
					JoinTree commonAncestor = rootNode.getCommonAncestor(leftNode, rightNode);
					return new String[] {commonAncestor.getAlias(), commonAncestor.getForm().getPrimaryKey()};
				}
			}
		}
    }

    private static JoinTree getTopLevelForm(JoinTree tree) {
    	if (tree.getParent() == null) {
    		return tree;
		}

    	if (tree.isSubForm() || tree.isNonTopLevelExtensionForm()) {
    		return getTopLevelForm(tree.getParent());
		}

    	return tree;
	}
}