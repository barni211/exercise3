package wdsr.exercise3.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.logging.Logger;

import wdsr.exercise3.model.Product;
import wdsr.exercise3.model.ProductType;

public class ProductService extends RestClientBase {
	protected ProductService(final String serverHost, final int serverPort, final Client client) {
		super(serverHost, serverPort, client);
	}
	
	Logger logger = Logger.getLogger(ProductService.class);
	
	/**
	 * Looks up all products of given types known to the server.
	 * @param types Set of types to be looked up
	 * @return A list of found products - possibly empty, never null.
	 */
	public List<Product> retrieveProducts(Set<ProductType> types) {
		WebTarget productTarget = baseTarget.path("products");
		
		List<Product> response = productTarget
		.queryParam("type", types.toArray())
		.request(MediaType.APPLICATION_JSON )
		.get(new GenericType<List<Product>>() {});
				
		return response;
	}
	
	/**
	 * Looks up all products known to the server.
	 * @return A list of all products - possibly empty, never null.
	 */
	public List<Product> retrieveAllProducts() {
		//WebTarget baseTarget = client.target("http://localhost:8090/" + String.valueOf(id));
		WebTarget productTarget = baseTarget.path("products");
		
		List<Product> response = productTarget
		.request(MediaType.APPLICATION_JSON )
		.get(new GenericType<List<Product>>() {});
				
		return response;
		
	}
	
	/**
	 * Looks up the product for given ID on the server.
	 * @param id Product ID assigned by the server
	 * @return Product if found
	 * @throws NotFoundException if no product found for the given ID.
	 */
	
	public Product retrieveProduct(int id) {
		//WebTarget baseTarget = client.target("http://localhost:8090/" + String.valueOf(id));
		WebTarget productTarget = baseTarget.path("products/" + String.valueOf(id) );
		
		Response response = productTarget
				.request(MediaType.APPLICATION_JSON )
				.get(Response.class);
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
		{
			throw new NotFoundException();
		}
		return response.readEntity(Product.class);
	}	
	
	/**
	 * Creates a new product on the server.
	 * @param product Product to be created. Must have null ID field.
	 * @return ID of the new product.
	 * @throws WebApplicationException if request to the server failed
	 */
	public int storeNewProduct(Product product) {
		//WebTarget baseTarget = client.target("http://localhost:8090/");
		WebTarget productTarget = baseTarget.path("products" );
		
		Response postResponse = productTarget
				.request()
				.post(Entity.entity(product, MediaType.APPLICATION_JSON), Response.class);
				
		
		//logger.info(String.valueOf(postResponse.getStatus()));
		if(postResponse.getStatus() != 201)
		{
			throw new WebApplicationException();
		}
		
		logger.info(String.valueOf(postResponse.readEntity(Product.class).getId()));
	
		
		return postResponse.readEntity(Product.class).getId();
	}
	
	/**
	 * Updates the given product.
	 * @param product Product with updated values. Its ID must identify an existing resource.
	 * @throws NotFoundException if no product found for the given ID.
	 */
	public void updateProduct(Product product) {
		WebTarget productTarget = baseTarget.path("products/" + String.valueOf(product.getId()));
		
		Response response = productTarget
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.json(product), Response.class);
		
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
		{
			throw new NotFoundException();
		}
	}

	
	/**
	 * Deletes the given product.
	 * @param product Product to be deleted. Its ID must identify an existing resource.
	 * @throws NotFoundException if no product found for the given ID.
	 */
	public void deleteProduct(Product product) {
		WebTarget productTarget = baseTarget.path("products/" + String.valueOf(product.getId()) );
		
		Response response = productTarget
		.request(MediaType.APPLICATION_JSON )
		.delete(Response.class);
		
		if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
		{
			throw new NotFoundException();
		}
			
	}
	
}
