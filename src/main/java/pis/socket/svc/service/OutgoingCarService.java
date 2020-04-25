package pis.socket.svc.service;

import pis.socket.svc.dto.EnteringCarDto;
import pis.socket.svc.dto.OutgoingCarDto;

public interface OutgoingCarService {
    int updateCarImage(OutgoingCarDto outgoingCar);
}
