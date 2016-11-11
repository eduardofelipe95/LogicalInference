package ic.ufal.br.logicalInference;

public class BiImplication extends LogicalExpression {
	
	BiImplication(String token, Categories categ, LogicalExpression left, LogicalExpression right){
		super(token, categ, left, right);
	}
	
	LogicalExpression solve() {
		if(this.left != null)
			this.left = this.left.solve();
		if(this.right != null)
			this.right = this.right.solve();
		
		LogicalExpression newRoot, newLeft, newRight;
		newLeft = new Implication("->", Categories.opImp, this.left, this.right);
		newRight = new Implication("->", Categories.opImp, null, null);
		
		newRight.left = copyBranchAST(this.right);
		newRight.right = copyBranchAST(this.left);
		
		newRoot = new Conjunction("^", Categories.opConj, newLeft, newRight);
		newRoot = newRoot.solve();
		
		return newRoot;
	}
	
	LogicalExpression copyBranchAST(LogicalExpression node){
		
		LogicalExpression newNode = copy(node);
		
		if(node.left != null){
			//node.left = copy(subTreeLeft);
			
			newNode.left = copyBranchAST(node.left);
		}
		if(node.right != null){
			//node.right = copy(subTreeRight);
			newNode.right = copyBranchAST(node.right);
		}
		
		return newNode;
	}
	
	LogicalExpression copy(LogicalExpression node){
		LogicalExpression newNode;
		if(node.categ == Categories.opConj)
			newNode = new Conjunction("^", Categories.opConj, null, null);
		else if(node.categ == Categories.opDisj)
			newNode = new Disjunction("v", Categories.opDisj, null, null);
		else if(node.categ == Categories.opImp)
			newNode = new Implication("->", Categories.opImp, null, null);
		else if(node.categ == Categories.opBiImp)
			newNode = new BiImplication("<->", Categories.opBiImp, null, null);
		else if(node.categ == Categories.opNeg)
			newNode = new Negation("~", Categories.opNeg, null, null);
		else if(node.categ == Categories.id)
			newNode = new Atom(node.token, Categories.id, null, null);
		else if(node.categ == Categories.prTrue)
			newNode = new Atom(node.token, Categories.prTrue, null, null);
		else
			newNode = new Atom(node.token, Categories.prFalse, null, null);
		
		newNode.nid = node.nid;
		
		return newNode;
	}
}
