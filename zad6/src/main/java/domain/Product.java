package domain;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity
@NamedQueries({ @NamedQuery(name = "product.all", query = "SELECT p FROM Product p"),
		@NamedQuery(name = "product.id", query = "FROM Product p WHERE p.id=:productId"),
		@NamedQuery(name = "product.filterPrice", query = "SELECT p FROM Product p WHERE p.price BETWEEN :priceMin AND :priceMax"),
		@NamedQuery(name = "product.filterName", query = "SELECT p FROM Product p WHERE p.name=:productName"),
		@NamedQuery(name = "product.filterCategory", query = "SELECT p FROM Product p WHERE p.category=:productCategory") })

public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private float price;
	private String category;
	private List<Comment> comments = new ArrayList<Comment>();

	@JsonbTransient
	@OneToMany(mappedBy = "product")
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
