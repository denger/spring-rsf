package org.springstack.rsf.example.rpc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springstack.rsf.example.dto.Product;

/**
 * 产品服务接口对外提供 .
 * 
 * @author denger
 *
 */
@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductService {

	@GET
	@Path("/{id}")
	public Product get(@PathParam("id") Long id);

	@DELETE
	@Path("/{id}")
	public boolean delete(@PathParam("id") Long id);

	@PUT
	public Product create(Product product);

	@GET
	public List<Product> findByCategory(@QueryParam("category") String category);
}
