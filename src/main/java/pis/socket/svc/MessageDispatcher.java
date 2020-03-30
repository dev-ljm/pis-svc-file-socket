package pis.socket.svc;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.Message;
import pis.socket.svc.event.FileDownloadEvent;
import pis.socket.svc.event.FileUploadEvent;

import javax.annotation.Resource;

@Component
@Slf4j
public class MessageDispatcher {

    @Resource
    private FileUploadEvent fileUploadEvent;

    @Resource
    private FileDownloadEvent fileDownloadEvent;

    public Message dispatch(ChannelHandlerContext ctx, Message message) {

        Message result = new Message();
        switch (message.getMsgId()) {
            case "A": // 파일 업로드
                result = this.fileUploadEvent.fire(message);

                break;
            case "B": // 파일 다운로드
                result = this.fileDownloadEvent.fire(message);

                break;
            default:
                break;
        }

        return result;

    }

}
