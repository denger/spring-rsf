package org.springcn.rsf.example.rpc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springcn.rsf.example.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations="classpath:applicationContext-rpc-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest  {

	@Autowired
	private ProductService productService;

	@Test
	public void testInvokeRpcMethod(){
		Product product = productService.get(1L);

		assertNotNull(product);
	}
}
