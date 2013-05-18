package swp_compiler_ss13.fuc.symbolTable;

import java.util.HashMap;

import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;

public class SymbolTableImpl implements SymbolTable{

	private static long ext;
	
	private SymbolTable parent = null;
	private HashMap<String, Type> symbolMap;
	private HashMap<String, Liveliness> liveMap;
	private HashMap<String, String> aliasMap;
	

	public SymbolTableImpl(SymbolTable parent) {
		this.parent = parent;
		this.symbolMap = new HashMap<String, Type>();
		this.liveMap = new HashMap<String,Liveliness>();
		this.aliasMap = new HashMap<String, String>();
	}
	
	public SymbolTableImpl(){
		this.symbolMap = new HashMap<String, Type>();
		this.liveMap = new HashMap<String,Liveliness>();
		this.aliasMap = new HashMap<String, String>();
	}
	
	@Override
	public SymbolTable getParentSymbolTable() {
		return parent;
	}

	@Override
	public Boolean isDeclared(String identifier) {
		if(parent == null){
			return symbolMap.containsKey(identifier);
		}else{
			if(symbolMap.containsKey(identifier)){
				return true;
			}else{
				return parent.isDeclared(identifier);
			}
		}
	}

	@Override
	public Type lookupType(String identifier) {
		if(parent == null){
			return symbolMap.get(identifier);
		}else{
			if(symbolMap.containsKey(identifier)){
				return symbolMap.get(identifier);
			}else{
				return parent.lookupType(identifier);
			}
		}
	}

	@Override
	public Boolean insert(String identifier, Type type) {
		if(!isDeclared(identifier)){
			symbolMap.put(identifier, type);
			return true;
		}
		return false;
	}

	@Override
	public Boolean remove(String identifier) {
		return symbolMap.remove(identifier) != null;
	}

	@Override
	public void setLivelinessInformation(String identifier,
			Liveliness liveliness) {
		liveMap.put(identifier,liveliness);
	}

	@Override
	public Liveliness getLivelinessInformation(String identifier) {
		return liveMap.get(identifier);
	}

	@Override
	public String getNextFreeTemporary() {
		String temp;
		do{
			temp = "tmp" + ext;
			ext++;
		}while(isDeclared(temp));
		return temp;
	}

	@Override
	public void putTemporary(String identifier, Type type) {
		if(this.parent != null){
			parent.putTemporary(identifier, type);
		}else{
			insert(identifier, type);
		}
		
	}

	@Override
	public SymbolTable getRootSymbolTable() {
		if(parent == null){
			return this;
		}else{
			return parent.getRootSymbolTable();
		}
	}

	@Override
	public Boolean isDeclaredInCurrentScope(String identifier) {
		return symbolMap.containsKey(identifier);
	}

	@Override
	public Type lookupTypeInCurrentScope(String identifier) {
		return symbolMap.get(identifier);
	}

	@Override
	public void setIdentifierAlias(String identifier, String alias) {
		aliasMap.put(identifier, alias);
	}

	@Override
	public String getIdentifierAlias(String identifier) {
		return aliasMap.get(identifier);
	}

	@Override
	public SymbolTable getDeclaringSymbolTable(String identifier) {
		if(isDeclaredInCurrentScope(identifier)){
			return this;
		}else{
			if(parent != null){
				return parent.getDeclaringSymbolTable(identifier);
			}else{
				return null;
			}
		}
	}

}
