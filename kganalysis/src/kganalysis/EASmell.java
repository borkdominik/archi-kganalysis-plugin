package kganalysis;

public class EASmell {
	
	private String name;
	private String description;
	private String query;
	private boolean detected;
	
	public EASmell() {
		
	}
	
	public EASmell(String name, String description, String query) {
		this.name = name;
		this.description = description;
		this.query = query;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuery() {
		return name;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
