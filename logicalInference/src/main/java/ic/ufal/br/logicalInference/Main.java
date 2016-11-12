package ic.ufal.br.logicalInference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	static LogicalExpression AST;
	static ArrayList<LogicalExpression> atoms = new ArrayList<LogicalExpression>();

	static void printAST(LogicalExpression node) {
		if (node.left != null)
			printAST(node.left);
		if (node.right != null)
			printAST(node.right);
		if (node.nid == true)
			System.out.println("~" + node.token);
		else
			System.out.println(node.token);
	}

	static LogicalExpression searchAtom(LogicalExpression node) {
		System.out.print("busca: ");
		printAST(AST);
		System.out.println();
		if (node.left != null && node.left.categ == Categories.opConj)
			node.left = searchAtom(node.left);
		if (node.right != null && node.right.categ == Categories.opConj)
			node.right = searchAtom(node.right);

		if (node.left != null && node.left.categ == Categories.opNeg) {
			node.left = node.left.solve();
		}
		if (node.right != null && node.right.categ == Categories.opNeg) {
			node.right = node.right.solve();
		}
		
		

		if (AST == node) {
			System.out.println("teste[");
			System.out.println(node.token);
			System.out.println(AST.token);
			System.out.println("]teste");
		}
		//if(node.isRoot == false){
		//System.out.println("1111111");
		if (node.left != null && node.left.categ == Categories.id 
				) {

			atoms.add(node.left);
			// AST = idk(AST, node.left);
			return node.right;
		}
		if (node.right != null && node.right.categ == Categories.id 
				) {
			atoms.add(node.right);
			// AST = idk(AST, node.right);
			return node.left;
		}
		//}
		return node;
	}

	// Boolean solveInf(LogicalExpression node){
	//
	// }

	static LogicalExpression idk(LogicalExpression node, LogicalExpression atom) {
		System.out.println("AST: " + AST.token);

		if (node.left != null)
			node.left = idk(node.left, atom);
		if (node.right != null)
			node.right = idk(node.right, atom);

		if (node.right != null && node.right.token.equals(atom.token)){
			if (atom.nid == true) {
				node.right = new Atom("false", Categories.prFalse, null, null);
			} else {
				System.out.println("aqui");
				node.right = new Atom("true", Categories.prTrue, null, null);
			}
			
			node = node.solve();

			System.out.println("1{");
			printAST(node);
			System.out.println("}1");
		}
		if (node.left != null && node.left.token.equals(atom.token)) {

			System.out.println("Aqui1");
			if (atom.nid == true) {
				node.left = new Atom("false", Categories.prFalse, null, null);
			} else {
				System.out.println("4");
				node.left = new Atom("true", Categories.prTrue, null, null);
			}
			node = node.solve();

			System.out.println("\\{");
			printAST(node);
			System.out.println("\\}");
		}

		return node;
	}

	static void infer() {

		for (int i = 0; i < 100; i++) {
			AST = searchAtom(AST);

			for (int j = atoms.size() - 1; atoms.size() > 0; j = atoms.size() - 1) {
				AST = idk(AST, atoms.get(j));
				atoms.remove(j);

			}
		}

		System.out.println("INFER:");
		printAST(AST);

	}

	public static void main(String[] args) throws IOException {
		LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("teste.txt");
		Parser parser = new Parser(lexicalAnalyzer);
		AST = parser.start();
		AST.isRoot = true;

		System.out.println("BEFORE:");
		printAST(AST);

		infer();
		System.out.println("AFTER:");
		printAST(AST);

	}

}
