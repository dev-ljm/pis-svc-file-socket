package pis.socket.svc;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.Message;
import pis.socket.svc.event.FileDownloadEvent;
import pis.socket.svc.event.FileUploadEvent;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.RandomAccessFile;

@Component
@Slf4j
public class MessageDispatcher {

    @Resource
    private FileUploadEvent fileUploadEvent;

    @Resource
    private FileDownloadEvent fileDownloadEvent;


    @Async
    public Message dispatch(ChannelHandlerContext ctx, Message message) throws Exception {

        Message result = new Message();
        switch (message.getMsgId()) {
            case "A": // 파일 업로드
                log.debug("파일 업로드: {}", message);
                result = this.fileUploadEvent.fire(message);

                break;
            case "B": // 파일 다운로드
                log.debug("파일 다운로드: {}", message);
                // 입차중인 차량이 있다면 조회해서 응답으로 내려준다.
                String fileDir = message.getFileDir(); // C:/works/pis/file/upload/0/20200412/

                result = this.fileDownloadEvent.fire(message);
                // /static/upload/0/20200412/20200412165827.jpg
                String downloadImagePath = result.getDownloadImagePath();
                downloadImagePath = downloadImagePath.replace(message.getLinkDir(), message.getFileDir());

                log.debug("downloadImagePath: {}", downloadImagePath);

                final RandomAccessFile raf = new RandomAccessFile(downloadImagePath, "r");
                final ChunkedFile chunkedFile = new ChunkedFile(raf);
                ctx.writeAndFlush(chunkedFile).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        try {
                            chunkedFile.close();
                            raf.close();
                        } catch (Exception e) {
                            // ignore
                        }
                        ctx.close();
                    }
                });


                break;
            default:
                break;
        }

        return result;

    }

}
