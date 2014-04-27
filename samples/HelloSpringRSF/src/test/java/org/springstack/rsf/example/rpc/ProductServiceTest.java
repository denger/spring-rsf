package org.springstack.rsf.example.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springstack.rsf.example.dto.Product;
import org.springstack.rsf.example.rpc.ProductService;

@ContextConfiguration(locations = "classpath:applicationContext-rpc-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Test
	public void testInvokeRpcMethod() {
		Product product = productService.get(1L);
		assertNotNull(product);
		assertEquals(product.getId(), new Long(1L));

		assertTrue(productService.delete(1L));

		List<Product> products = productService.findByCategory("all");
		assertTrue(products.size() > 0);
	}
	
	@Test
	public void testHttpPing() throws UnknownHostException, IOException {
		for(int i = 0; i < 200; i++){
//			long s1 = System.currentTimeMillis();
//			boolean s2 = pingByURL("http://191.2.3.2:81");
//			long s3 = System.currentTimeMillis();
//			
			long f1 = System.currentTimeMillis();
			boolean f2 = InetAddress.getByName("127.0.0.1").isReachable(10);
			long f3 = System.currentTimeMillis();

			
			System.out.println(" Socket:" + (f3-f1) + " " + f2);
		}
	}
	
	public static boolean pingByURL(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	         (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	    	//e.printStackTrace();
	       return false;
	    }
	  }
	
	public static boolean pingBySocket(String hostnameOrIP, int port) {
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket(hostnameOrIP, port);
		    reachable = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {            
		    if (socket != null) try { socket.close(); } catch(IOException e) {}
		}
		return reachable;
	}
	



}
