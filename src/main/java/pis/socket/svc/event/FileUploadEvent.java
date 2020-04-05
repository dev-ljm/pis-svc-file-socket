package pis.socket.svc.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.Message;
import pis.socket.svc.service.EnteringCarService;

import javax.annotation.Resource;

@Slf4j
@Component
public class FileUploadEvent implements MessageEvent {

    @Resource
    private EnteringCarService enteringCarService;

    @Override
    public Message fire(Message message) {

        System.out.println("FileUploadEvent fire!!!! ");





        return null;
    }
}
