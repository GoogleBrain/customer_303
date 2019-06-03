
package main;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.bazinga.service.ValidationService;
import org.bazinga.service.pojo.ValidationParameter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.RpcException;

public class ClientMain {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationConsumer.xml" });
		context.start();
		ValidationService service = (ValidationService) context.getBean("validationService");
		ValidationParameter parameter = new ValidationParameter();
		parameter.setName("ncc"); // not null
		parameter.setEmail("nucc163.com"); // email格式正确
		parameter.setAge(50); // [18,100]
		parameter.setLoginDate(new Date(System.currentTimeMillis() - 1000000)); // 时间必须是过去的时间
		parameter.setExpiryDate(new Date(System.currentTimeMillis() + 1000000));// 时间必须是未来的时间
		try {
			service.save(parameter);
		} catch (RpcException e) {
			System.out.println(e.getCause().getLocalizedMessage());
		}
		context.close();
	}
}