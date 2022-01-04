package kganalysis;

public class EASmell {
	
	private String name;
	private String description;
	private String object;
	
	public EASmell(String name, String description, String object) {
		this.name = name;
		this.description = description;
		this.object = object;
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
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	

}
