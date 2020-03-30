package pis.socket.svc.dto;

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

}
