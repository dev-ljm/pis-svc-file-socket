package pis.socket.svc.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.EnteringCarDto;
import pis.socket.svc.dto.Message;
import pis.socket.svc.service.EnteringCarService;

import javax.annotation.Resource;

@Slf4j
@Component
public class FileDownloadEvent implements MessageEvent {

    @Resource
    private EnteringCarService enteringCarService;

    @Override
    public Message fire(Message message) {
        log.debug("FileDownloadEvent fire!!!! ");
        log.debug("message >>"+ message);

        String car_no = message.getCarNo();
        // carNo로 차량 조회하기 쉽지 않다.
        // 가장 최근에 입차한 차량의 데이터 ?

        EnteringCarDto enteringCar = EnteringCarDto.builder()
                .car_no(car_no)
                .build();

        EnteringCarDto carImage = this.enteringCarService.findCarImage(enteringCar);

        log.debug("carImage: {} ", carImage);

        message.setDownloadImagePath(carImage.getIncar_img_path());
        return message;
    }
}
