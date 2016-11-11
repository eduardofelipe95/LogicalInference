package ic.ufal.br.logicalInference;

public class Negation extends LogicalExpression {
	
	Negation(String token, Categories categ, LogicalExpression left, LogicalExpression right){
		super(token, categ, left, right);
	}

	LogicalExpression solve() {
		if(this.left != null)
			this.left = this.left.solve();
		if(this.right != null)
			this.right = this.right.solve();
		
		LogicalExpression newNode = deMorgan(this.left);
		
		return newNode;
	}
	
	LogicalExpression deMorgan(LogicalExpression node){
		LogicalExpression newNode = null;
		
		if(node.categ == Categories.opConj){
			newNode = new Disjunction("v", Categories.opDisj, node.left, node.right);
		}
		else if(node.categ == Categories.opDisj){
			newNode = new Conjunction("^", Categories.opConj, node.left, node.right);
		}
		else if(node.categ == Categories.id){
			newNode = node;
			newNode.nid = (true ^ node.nid);
		}
		else if(node.categ == Categories.prTrue){
			newNode = node;
			newNode.token = "F";
			newNode.categ = Categories.prFalse;
		}
		else if(node.categ == Categories.prFalse){
			newNode = node;
			newNode.token = "T";
			newNode.categ = Categories.prTrue;
		}
		
		if(newNode.left != null)
			newNode.left = deMorgan(newNode.left);
		if(newNode.right != null)
			newNode.right = deMorgan(newNode.right);
		
		return newNode;
	}
	
}
