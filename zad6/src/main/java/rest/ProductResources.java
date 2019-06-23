package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Comment;
import domain.Product;

@Path("/product")
@Stateless
public class ProductResources {

	@PersistenceContext
	EntityManager em;

	/**
	 * Pobiera wszystkie produkty
	 * 
	 * @return [{ "name":"Intel i6-7900k", "price": 2315.3, "category":"CPU" }]
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getAll() {
		return em.createNamedQuery("product.all", Product.class).getResultList();
	}

	/**
	 * Pobiera produkty na podstawie ceny produktu
	 * 
	 * @param priceMin
	 * @param priceMax
	 * @return JSON: { "name":"Intel i6-7900k", "price": 2315.3, "category":"CPU" }
	 */
	@GET
	@Path("/price/{priceMin}/{priceMax}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByPrice(@PathParam("priceMin") int priceMin, @PathParam("priceMax") int priceMax) {
		return em.createNamedQuery("product.filterPrice", Product.class).setParameter("priceMin", priceMin)
				.setParameter("priceMax", priceMax).getResultList();

	}

	/**
	 * Pobiera produkty na podstawie nazwy produktu
	 * 
	 * @param product
	 * @return JSON: { "name":"Intel i6-7900k", "price": 2315.3, "category":"CPU" }
	 */
	@GET
	@Path("/name/{productName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByName(@PathParam("productName") String product) {
		return em.createNamedQuery("product.filterName", Product.class).setParameter("productName", product)
				.getResultList();
	}

	/**
	 * Pobiera produkty na podstawie nazwy kategorii
	 * 
	 * @param product
	 * @return JSON: { "name":"Intel i6-7900k", "price": 2315.3, "category":"CPU" }
	 */
	@GET
	@Path("/category/{productCategory}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByCategory(@PathParam("productCategory") String product) {
		return em.createNamedQuery("product.filterCategory", Product.class).setParameter("productCategory", product)
				.getResultList();
	}

	/**
	 * Dodaje nowy produkt
	 * 
	 * JSON: { "name":"Intel i6-7900k", "price": 554.3, "category":"CPU" }
	 * 
	 * @param product
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Add(Product product) {
		em.persist(product);
		return Response.ok(product.getId()).build();
	}

	/**
	 * Pobiera produkt o podanym id
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		Product result = em.createNamedQuery("product.id", Product.class).setParameter("productId", id)
				.getSingleResult();
		if (result == null) {
			return Response.status(404).build();
		}
		return Response.ok(result).build();
	}

	/**
	 * Aktualizuje produkt o podanym id
	 * 
	 * JSON: { "name":"Intel i7-7900k", "price": 225.3, "category":"CPU" }
	 * 
	 * @param id
	 * @param p
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Product p) {
		Product result = em.createNamedQuery("product.id", Product.class).setParameter("productId", id)
				.getSingleResult();
		if (result == null) {
			return Response.status(404).build();
		}
		result.setName(p.getName());
		result.setCategory(p.getCategory());
		em.persist(result);
		return Response.ok().build();
	}

	/**
	 * Dodaje komentarz do produktu o podanym id
	 * 
	 * JSON: { "author":"Janusz", "text": "Komentarz..." }
	 * 
	 * @param id
	 * @param comment
	 * @return
	 */
	@POST
	@Path("/{id}/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(@PathParam("id") int id, Comment comment) {
		Product result = em.createNamedQuery("product.id", Product.class).setParameter("productId", id)
				.getSingleResult();
		if (result == null) {
			return Response.status(404).build();
		}
		result.getComments().add(comment);
		comment.setProduct(result);
		em.persist(comment);
		return Response.ok().build();
	}

	/**
	 * Pobiera wszystkie komentarze dla produktu o podanym id
	 * 
	 * @param id
	 * @return JSON: [{ "author":"Janusz", "text": "Komentarz..." }]
	 */
	@GET
	@Path("/{productId}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getComments(@PathParam("productId") int id) {
		Product result = em.createNamedQuery("product.id", Product.class).setParameter("productId", id)
				.getSingleResult();

		return result.getComments();
	}

	@DELETE
	@Path("/comment/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteComment(@PathParam("commentId") int id) {
		Comment comment = em.createNamedQuery("comment.id", Comment.class).setParameter("commentId", id)
				.getSingleResult();
		em.remove(comment);
		return Response.ok().build();
	}

}
