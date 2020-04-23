package pis.socket;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pis.socket.svc.FileUploaderServer;

@SpringBootApplication
@EnableEncryptableProperties
public class ServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceApplication.class, args);
        FileUploaderServer fileUploaderServer = context.getBean(FileUploaderServer.class);
        fileUploaderServer.start();
    }
}
