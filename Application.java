package hello;

import org.apache.zookeeper.KeeperException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	ZKManagerImpl zk = null;

	Application(){
		zk = new ZKManagerImpl();
	}

	@RequestMapping("/")
	public String home() {
		return "Hello Docker World";
	}

	@RequestMapping("/checkZnode")
	public Object checkZnode() {
		System.out.println("You chosed to show the node");
		return zk.getZNodeData("/prova", true);
	}

	@RequestMapping("/createZnode/{value}")
	public String createZnode(@RequestParam String value) {
		System.out.println("You chosed to create a node!");
		try {
			zk.create("/value", "Creation successful!".getBytes());
		} catch (KeeperException e) {
			e.printStackTrace();
			return "ERROR";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "ERROR";
		}
		return "Creation successful!";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
