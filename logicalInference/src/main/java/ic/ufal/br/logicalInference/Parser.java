package ic.ufal.br.logicalInference;

import java.util.ArrayList;

public class Parser {
	
	private Token token;
	private LexicalAnalyzer la;
	ArrayList<String> tokens = new ArrayList<String>();
	
	public Parser(LexicalAnalyzer la){
		this.la = la;
	}
	
	LogicalExpression start(){
		nextToken();
		return E();
	}
	
	void nextToken(){
		token = la.nextToken();
		tokens.add(token.lexVal());
		//System.out.println(token.toString());
	}
	
	void error(){
		System.out.println("Entrada inválida!");
		System.exit(0);
	}

	LogicalExpression E(){
		LogicalExpression CNode = C();
		return Er(CNode);
	}
	
	LogicalExpression Er(LogicalExpression ErhNode){
		if(token.categ == Categories.opBiImp){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			LogicalExpression CNode = C();
			LogicalExpression Er1hNode = new BiImplication(lastToken, lastCateg, ErhNode, CNode);
			return Er(Er1hNode);
		}
		else
			return ErhNode;
	}
	
	LogicalExpression C(){
		LogicalExpression DNode = D();
		return Cr(DNode);
	}
	
	LogicalExpression Cr(LogicalExpression CrhNode){
		if(token.categ == Categories.opImp){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			LogicalExpression DNode = D();
			LogicalExpression Cr1hNode = new Implication(lastToken, lastCateg, CrhNode, DNode);
			return Cr(Cr1hNode);
		}
		else
			return CrhNode;
	}
	
	LogicalExpression D(){
		LogicalExpression FNode = F();
		return Dr(FNode);
	}
	
	LogicalExpression Dr(LogicalExpression DrhNode){
		if(token.categ == Categories.opDisj){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			LogicalExpression FNode = F();
			LogicalExpression Dr1hNode = new Disjunction(lastToken, lastCateg, DrhNode, FNode);
			return Dr(Dr1hNode);
		}
		else
			return DrhNode;
	}
	
	LogicalExpression F(){
		LogicalExpression GNode = G();
		return Fr(GNode);
	}
	
	LogicalExpression Fr(LogicalExpression FrhNode){
		if(token.categ == Categories.opConj){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			LogicalExpression GNode = G();
			LogicalExpression Fr1hNode = new Conjunction(lastToken, lastCateg, FrhNode, GNode);
			return Fr(Fr1hNode);
		}
		else
			return FrhNode;
	}
	
	LogicalExpression G(){
		LogicalExpression GNode = null;
		if(token.categ == Categories.opNeg){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			LogicalExpression HNode = H();
			GNode = new Negation(lastToken, lastCateg, HNode, null);
		}
		else
			GNode = H();
		
		return GNode;
	}
	
	LogicalExpression H(){
		LogicalExpression HNode = null;
		if(token.categ == Categories.abPar){
			nextToken();
			HNode = E();
			if(token.categ != Categories.fcPar)
				error();
			nextToken();
		}
		else if(token.categ == Categories.id || token.categ == Categories.prTrue || token.categ == Categories.prFalse){
			String lastToken = token.lexVal();
			Categories lastCateg = token.categ;
			nextToken();
			HNode = new Atom(lastToken, lastCateg, null, null);
		}
		else if(token.categ != Categories.EOF)
			error();

		return HNode;
	}
	
}
