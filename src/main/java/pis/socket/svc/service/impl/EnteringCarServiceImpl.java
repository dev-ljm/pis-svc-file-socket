package pis.socket.svc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pis.socket.svc.service.EnteringCarService;

@Slf4j
@Service
public class EnteringCarServiceImpl implements EnteringCarService {


    @Override
    public int call() {

try {
    Thread.sleep(1000 * 10);
} catch (InterruptedException e) {
    e.printStackTrace();
}



        return 0;
    }
}
