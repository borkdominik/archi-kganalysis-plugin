package kganalysis;

import java.util.ArrayList;
import java.util.List;

public enum SmellsProvider {
	
	INSTANCE;
	
	private List<EASmell> smells;
	
	private SmellsProvider() {
		smells = new ArrayList<EASmell>();
		
		// TODO: Load smells from json
		
		smells.add(new EASmell("Dead Component", "Unused component", "MATCH (n)\n"
				+ "WHERE not(n)-−()\n"
				+ "return n"));
		
	}
	
	public List<EASmell> getSmells() {
		return smells;
	}
	
	
}
