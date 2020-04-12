package pis.socket.svc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pis.socket.svc.dto.EnteringCarDto;
import pis.socket.svc.mapper.EnteringCarMapper;
import pis.socket.svc.service.EnteringCarService;

import javax.annotation.Resource;

@Slf4j
@Service
public class EnteringCarServiceImpl implements EnteringCarService {

    @Resource
    private EnteringCarMapper enteringCarMapper;

    @Override
    public int evalDup(EnteringCarDto enteringCar) {
        return this.enteringCarMapper.evalDup(enteringCar);
    }

    @Override
    public int updateCarImage(EnteringCarDto enteringCar) {
        return this.enteringCarMapper.updateCarImage(enteringCar);
    }

    @Override
    public EnteringCarDto findCarImage(EnteringCarDto enteringCar) {
        return this.enteringCarMapper.findCarImage(enteringCar);
    }
}
