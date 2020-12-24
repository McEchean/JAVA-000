package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.proxy.ByteBuddyProxy;
import io.kimmking.rpcfx.client.proxy.CglibProxy;
import io.kimmking.rpcfx.client.proxy.JDKProxy;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {

		CglibProxy cglibProxy = new CglibProxy();
		UserService userService1 = cglibProxy.create(UserService.class, "127.0.0.1",8081);
		User user1 = userService1.findById(1);

		System.out.println("find user id=1 from server: " + user1.getName());

		ByteBuddyProxy byteBuddyProxy = new ByteBuddyProxy();
		UserService userService2 = byteBuddyProxy.create(UserService.class, "127.0.0.1", 8081);
		User user2 = userService2.findById(2);

		System.out.println("find user id=2 from server: " + user2.getName());

		JDKProxy jdkProxy = new JDKProxy();
		UserService userService3 = jdkProxy.create(UserService.class, "127.0.0.1", 8081);
		User user3 = userService3.findById(3);

		System.out.println("find user id=3 from server: " + user3.getName());


		OrderService orderService1 = cglibProxy.create(OrderService.class, "127.0.0.1",8081);
		Order order1 = orderService1.findOrderById(1);
		System.out.printf("find order1 name=%s, amount=%f%n",order1.getName(),order1.getAmount());

		OrderService orderService2 = byteBuddyProxy.create(OrderService.class, "127.0.0.1", 8081);
		Order order2 = orderService2.findOrderById(2);
		System.out.printf("find order2 name=%s, amount=%f%n",order2.getName(),order2.getAmount());

		OrderService orderService3 = jdkProxy.create(OrderService.class, "127.0.0.1", 8081);
		Order order3 = orderService3.findOrderById(3);
		System.out.printf("find order3 name=%s, amount=%f%n",order3.getName(),order3.getAmount());
	}

}
