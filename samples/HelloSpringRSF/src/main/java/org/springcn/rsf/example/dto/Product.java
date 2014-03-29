package org.springcn.rsf.example.dto;

/**
 * Prodct Info.
 * 
 * @author denger
 */
public class Product {

	private String name;

	private Long id;

	private String desc;

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
