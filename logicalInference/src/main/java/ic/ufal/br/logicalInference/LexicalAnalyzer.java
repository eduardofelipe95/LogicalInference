package ic.ufal.br.logicalInference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexicalAnalyzer {
	private int line, currentColumn, lastColumn;
	private String sentence;
	private String filePath;
	
	private final char EOF = '\0';
	
	public LexicalAnalyzer(String filePath) throws IOException{
		this.filePath = filePath;
		this.line = 0;
		this.currentColumn = 0;
		this.readFile();
		this.normalize();
	}
	
	private void readFile() throws IOException{
    	sentence = Files.readAllLines(Paths.get(filePath)).get(0);
	}
	
	private void normalize(){
//		sentence = sentence.replace("{", "(");
//		sentence = sentence.replace("}", ")");
		sentence = sentence.replace(",", ")^(");
//		sentence = sentence.replace("|=", "->");
		sentence = "(" + sentence + ")";
	}
	
	private Character nextChar(){
		if (currentColumn < sentence.length()) {
			lastColumn = currentColumn;
			return sentence.charAt(currentColumn++);
		}
		
		return EOF;
	}
	
	public Token nextToken(){
		char currentChar;
		String token = "";
		Token nextToken = null;
		Categories categ = null;
		
		do{
			currentChar = nextChar();
			if(!(currentChar == EOF || currentChar == '\n' || currentChar == ' ' || currentChar == '	')){
				token += currentChar;
				categ = tokenCateg(token);
				if(categ != Categories.unknown)
					break;
			}
		}
		while(currentChar != EOF);
		
		if(token.equals(""))
			categ = Categories.EOF;
		
		nextToken = new Token(token, categ, line, lastColumn);
		
		return nextToken;
	}
	
	private Categories tokenCateg(String token){
		if(token.equals("("))
			return Categories.abPar;
		else if(token.equals(")"))
			return Categories.fcPar;
		else if(token.equals("~"))
			return Categories.opNeg;
		else if(token.equals("{"))
			return Categories.abCh;
		else if(token.equals("}"))
			return Categories.fcCh;
		else if(token.equals("^"))
			return Categories.opConj;
		else if(token.equals("v"))
			return Categories.opDisj;
		else if(token.equals("->"))
			return Categories.opImp;
		else if(token.equals("<->"))
			return Categories.opBiImp;
		else if(token.equals("T"))
			return Categories.prTrue;
		else if(token.equals("F"))
			return Categories.prFalse;
		else if(token.matches("[a-zA-Z]"))
			return Categories.id;
		else
			return Categories.unknown;
	}
}
