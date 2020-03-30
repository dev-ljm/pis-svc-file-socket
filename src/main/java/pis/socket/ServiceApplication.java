package pis.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pis.socket.svc.FileUploaderServer;

@SpringBootApplication
public class ServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceApplication.class, args);
        FileUploaderServer fileUploaderServer = context.getBean(FileUploaderServer.class);
        fileUploaderServer.start();
    }
}
