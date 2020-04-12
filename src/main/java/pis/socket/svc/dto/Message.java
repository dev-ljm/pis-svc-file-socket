package pis.socket.svc.dto;

import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String msgId;
    private String flag;
    private String carNo;
    private String imageName;
    private String linkDir;
    private String fileDir;
    private String imageType;
    private String imageDateDir;

    private String downloadImagePath;

}
