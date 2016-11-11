package ic.ufal.br.logicalInference;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	static LogicalExpression AST;
	static ArrayList<LogicalExpression> atoms = new ArrayList<LogicalExpression>(); 
	
	static void printAST(LogicalExpression node){
		if(node.left != null)
			printAST(node.left);
		if(node.right != null)
			printAST(node.right);
		if(node.nid == true)
			System.out.println("~" + node.token);
		else
			System.out.println(node.token);
	}
	
	static LogicalExpression searchAtom(LogicalExpression node){
		if(node.left != null && node.left.categ == Categories.opConj)
			node.left = searchAtom(node.left);
		if(node.right != null && node.right.categ == Categories.opConj)
			node.right = searchAtom(node.right);
		
		if(node.left != null && node.left.categ == Categories.opNeg){
			node.left = node.left.solve();
		}
		if(node.right != null && node.right.categ == Categories.opNeg){
			node.right = node.right.solve();
		}
		
		if(node.left != null && node.left.categ == Categories.id){
			
			atoms.add(node.left);
			//AST = idk(AST, node.left);
			return node.right;
		}
		else if(node.right != null && node.right.categ == Categories.id ){
			atoms.add(node.right);
			//AST = idk(AST, node.right);
			return node.left;
		}
		
		return node;
	}
	
//	Boolean solveInf(LogicalExpression node){
//		
//	}
	
	static LogicalExpression idk(LogicalExpression node, LogicalExpression atom){
		if(node.left != null)
			node.left = idk(node.left, atom);
		if(node.right != null)
			node.right = idk(node.right, atom);
		
		if(node.right != null && node.right.token.equals(atom.token) 
				&& node != AST){
			if(atom.nid == true){
				node.right = new Atom("false", Categories.prFalse, null, null);
			}
			else{
				node.right = new Atom("true", Categories.prTrue, null, null);
			}
			
			return node.solve();
		}
		if(node.left != null && node.left.token.equals(atom.token) 
				&& node != AST){
			if(atom.nid == true){
				node.left = new Atom("false", Categories.prFalse, null, null);
			}
			else{
				node.left = new Atom("true", Categories.prTrue, null, null);
			}
			
			return node.solve();
		}
		
		return node;
	}
	
	static void infer(){

		for(int i = 0; i < 100; i++){
			AST = searchAtom(AST);
			
			for(int j = atoms.size() - 1; atoms.size() > 0; j = atoms.size() - 1){ 
				AST = idk(AST, atoms.get(j));
				atoms.remove(j);
			
			}
		}
		
		
		
		System.out.println("INFER:");
		printAST(AST);
		
		
	}
	
	
    public static void main( String[] args ) throws IOException{
    	LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("teste.txt");
    	Parser parser = new Parser(lexicalAnalyzer);
    	AST = parser.start();
    	
    	System.out.println("BEFORE:");
    	printAST(AST);
    	
    	infer();
    	System.out.println("AFTER:");
    	printAST(AST);
    			
    }
    
}
