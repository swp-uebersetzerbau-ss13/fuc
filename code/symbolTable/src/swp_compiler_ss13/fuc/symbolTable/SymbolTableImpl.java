package swp_compiler_ss13.fuc.symbolTable;

import java.util.HashMap;

import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;

public class SymbolTableImpl implements SymbolTable {

	private SymbolTable parent = null;
	private HashMap<String, Type> symbolTable;
	private HashMap<String, String> aliases;
	private HashMap<String, Liveliness> liveliness;
	private static long ext = 0L;

	public SymbolTableImpl(SymbolTable parent) {
		this.parent = parent;
		this.symbolTable = new HashMap<String, Type>();
		this.aliases = new HashMap<String, String>();
		this.liveliness = new HashMap<String, Liveliness>();
	}

	public SymbolTableImpl() {
		this.symbolTable = new HashMap<String, Type>();
		this.aliases = new HashMap<String, String>();
		this.liveliness = new HashMap<String, Liveliness>();
	}

	@Override
	public SymbolTable getParentSymbolTable() {
		return this.parent;
	}

	@Override
	public SymbolTable getRootSymbolTable() {
		if (this.parent == null) {
			return this;
		}
		return this.parent.getRootSymbolTable();
	}

	@Override
	public Boolean isDeclared(String identifier) {
		if (this.symbolTable.containsKey(identifier)) {
			return true;
		}
		if (this.parent != null) {
			return this.parent.isDeclared(identifier);
		}
		return false;
	}

	@Override
	public Boolean isDeclaredInCurrentScope(String identifier) {
		return this.symbolTable.containsKey(identifier);
	}

	@Override
	public Type lookupType(String identifier) {
		if (this.isDeclaredInCurrentScope(identifier)) {
			return this.symbolTable.get(identifier);
		}
		if (this.parent != null) {
			return this.parent.lookupType(identifier);
		}
		return null;
	}

	@Override
	public Type lookupTypeInCurrentScope(String identifier) {
		if (this.isDeclaredInCurrentScope(identifier)) {
			return this.symbolTable.get(identifier);
		}
		return null;
	}

	@Override
	public Boolean insert(String identifier, Type type) {
		if (this.isDeclaredInCurrentScope(identifier)) {
			return false;
		}
		this.symbolTable.put(identifier, type);
		return true;
	}

	@Override
	public Boolean remove(String identifier) {
		if (this.isDeclaredInCurrentScope(identifier)) {
			this.symbolTable.remove(identifier);
			this.aliases.remove(identifier);
			return true;
		}
		return false;
	}

	@Override
	public void setLivelinessInformation(String identifier, Liveliness liveliness) {
		this.liveliness.put(identifier, liveliness);
	}

	@Override
	public Liveliness getLivelinessInformation(String identifier) {
		return this.liveliness.get(identifier);
	}

	@Override
	public String getNextFreeTemporary() {
		String temp = "tmp" + ext;
		ext++;
		return temp;
	}

	@Override
	public void putTemporary(String identifier, Type type) {
		this.getRootSymbolTable().insert(identifier, type);
	}

	@Override
	public void setIdentifierAlias(String identifier, String alias) {
		this.aliases.put(identifier, alias);
	}

	@Override
	public String getIdentifierAlias(String identifier) {
		String alias = this.aliases.get(identifier);
		if (alias == null) {
			if (this.isDeclared(identifier)) {
				return identifier;
			}
			return null;
		}
		return alias;
	}

	@Override
	public SymbolTable getDeclaringSymbolTable(String identifier) {
		if (this.isDeclaredInCurrentScope(identifier)) {
			return this;
		}
		if (this.parent != null) {
			return this.parent.getDeclaringSymbolTable(identifier);
		}
		return null;
	}
}
