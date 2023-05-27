package com.sam.myVault.entities;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "myVault")
public class MyVault {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = false)
	private String description;	
	
//	@Column(name = "price",nullable = false)
//    private double price;
//
	@Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;
    @Lob
	private byte[] content;
	public MyVault() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public byte[] getContent(){
		return content;
	}

	public byte[] getImage() {
		return image;
	}

	public void  setContent(byte[] content){
		this.content = content;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", image="
				+ Arrays.toString(image) + ", createDate=" + createDate + "]";
	}
   
}

