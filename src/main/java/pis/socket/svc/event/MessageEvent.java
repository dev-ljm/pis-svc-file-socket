package pis.socket.svc.event;

import pis.socket.svc.dto.Message;

public interface MessageEvent {
    Message fire(Message message);
}
