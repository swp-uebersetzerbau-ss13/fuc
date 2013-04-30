package swp_compiler_ss13.fuc.ir;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;

public class SymbolTableImpl implements SymbolTable {

	private SymbolTable parent = null;
	private Map<String, Type> symbols = new HashMap<>();

	@Override
	public SymbolTable getParentSymbolTable() {
		return this.parent;
	}

	@Override
	public Boolean isDeclared(String identifier) {
		if (this.symbols.containsKey(identifier)) {
			return true;
		}
		if (this.parent == null) {
			return false;
		}
		return this.parent.isDeclared(identifier);
	}

	@Override
	public Type lookupType(String identifier) {
		if (this.symbols.containsKey(identifier)) {
			return this.symbols.get(identifier);
		}
		if (this.parent == null) {
			return null;
		}
		return this.parent.lookupType(identifier);
	}

	@Override
	public void insert(String identifier, Type type) {
		this.symbols.put(identifier, type);
	}

	@Override
	public Boolean remove(String identifier) {
		return null != this.symbols.remove(identifier);
	}

	@Override
	public void setLivelinessInformation(String identifier, Liveliness liveliness) {
		// TODO Auto-generated method stub

	}

	@Override
	public Liveliness getLivelinessInformation(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNextFreeTemporary() {
		int i = 0;
		while (this.isDeclared("t" + i)) {
			i++;
		}
		return "t" + i;

	}

	@Override
	public void putTemporary(String identifier, Type type) {
		if (this.parent == null) {
			this.symbols.put(identifier, type);
		} else {
			this.parent.putTemporary(identifier, type);
		}
	}

}
