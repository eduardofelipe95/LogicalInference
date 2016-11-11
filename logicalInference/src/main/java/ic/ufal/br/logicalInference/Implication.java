package ic.ufal.br.logicalInference;

public class Implication extends LogicalExpression {
	
	Implication(String token, Categories categ, LogicalExpression left, LogicalExpression right){
		super(token, categ, left, right);
	}
	
	LogicalExpression solve(){
		if(this.left != null)
			this.left = this.left.solve();
		if(this.right != null)
			this.right = this.right.solve();
		
			LogicalExpression neg = new Negation("~", Categories.opNeg, this.left, null);
			LogicalExpression disj = new Disjunction("v", Categories.opDisj, neg, this.right);
			
			disj = disj.solve();
			
			return disj;
	}

}