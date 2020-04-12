package pis.socket.svc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarImageDto {

    private String fileDir;
    private String baseUploadDir;
    private String baseDownloadDir;
    private String fileType;
    private String linkDir;

}
