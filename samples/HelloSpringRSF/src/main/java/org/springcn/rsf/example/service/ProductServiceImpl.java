package org.springcn.rsf.example.service;

import java.util.List;

import org.springcn.rsf.example.dto.Product;
import org.springcn.rsf.example.rpc.ProductService;

/**
 * 产品业务实现
 * 
 * @author denger
 *
 */
public class ProductServiceImpl implements ProductService{

	public Product get(Long id) {
		Product product = new Product();
		return product;
	}

	public boolean delete(Long id) {
		return false;
	}

	public Product create(Product product) {
		return null;
	}

	public List<Product> findByCategory(String category) {
		return null;
	}

}
