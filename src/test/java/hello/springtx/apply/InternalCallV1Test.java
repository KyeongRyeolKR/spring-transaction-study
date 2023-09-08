package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 프록시 방식의 AOP 한계
 *
 * 자바는 메서드 앞에 별도의 참조가 없으면 'this' 라는 뜻으로 자기 자신의 인스턴스를 가리킨다.
 * 아래 CallService.external() 메서드를 보면 internal()를 내부 호출한다.
 * 이때, internal()은 this가 생략된 것이므로 결국 실제 객체의 인스턴스를 사용하게 된다.
 * 결과적으로 프록시를 거치지 않고 internal() 메서드를 직접 사용했기 때문에 트랜잭션이 적용되지 않는다!!!
 */
@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService class = {}", callService.getClass());
    }

    @Test
    void internalCall() {
        callService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {

        public void external() {
            log.info("call external");

            printTxInfo();

            internal();
        }

        @Transactional
        public void internal() {
            log.info("call internal");

            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
        }
    }
}
