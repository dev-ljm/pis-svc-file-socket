package pis.socket.svc.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.Message;

@Slf4j
@Component
public class FileUploadEvent implements MessageEvent {
    @Override
    public Message fire(Message message) {
        return null;
    }
}
