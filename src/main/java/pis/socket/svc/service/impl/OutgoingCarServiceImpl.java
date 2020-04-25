package pis.socket.svc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pis.socket.svc.dto.OutgoingCarDto;
import pis.socket.svc.mapper.OutgoingCarMapper;
import pis.socket.svc.service.OutgoingCarService;

import javax.annotation.Resource;

@Slf4j
@Service
public class OutgoingCarServiceImpl implements OutgoingCarService {

    @Resource
    private OutgoingCarMapper outgoingCarMapper;

    @Override
    public int updateCarImage(OutgoingCarDto outgoingCar) {
        return this.outgoingCarMapper.updateCarImage(outgoingCar);
    }
}
