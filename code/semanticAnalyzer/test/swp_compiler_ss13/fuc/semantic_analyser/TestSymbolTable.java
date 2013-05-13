package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;

public class TestSymbolTable implements SymbolTable {

	private final Map<String, Type> table;

	public TestSymbolTable() {
		table = new HashMap<String, Type>();
	}

	@Override
	public SymbolTable getParentSymbolTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isDeclared(String identifier) {
		// TODO Auto-generated method stub
		return table.get(identifier) != null;
	}

	@Override
	public Type lookupType(String identifier) {
		Type type = table.get(identifier);
		if (type != null)
			return type;
		return null;
	}

	@Override
	public void insert(String identifier, Type type) {
		table.put(identifier, type);
	}

	@Override
	public Boolean remove(String identifier) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putTemporary(String identifier, Type type) {
		// TODO Auto-generated method stub

	}

}
