package org.springstack.rsf.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springstack.rsf.example.dto.Product;
import org.springstack.rsf.example.rpc.ProductService;

/**
 * 产品业务实现
 * 
 * @author denger
 *
 */
public class ProductServiceImpl implements ProductService{

	public Product get(Long id) {
		Product product = new Product();
		product.setName("iPad 32G");
		product.setId(id);
		product.setDesc("Design by Apple!");

		return product;
	}

	public boolean delete(Long id) {
		// do something 
		return true;
	}

	public Product create(Product product) {
		product.setId(1L);
		return product;
	}

	public List<Product> findByCategory(String category) {
		List<Product> products = new ArrayList<Product>();
		products.add(get(1L));
		products.add(get(2L));
		products.add(get(3L));
		return products;
	}

}
