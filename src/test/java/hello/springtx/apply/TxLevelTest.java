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
 * 트랜잭션 적용 우선 순위
 * 1. 클래스의 메서드 레벨
 * 2. 클래스 레벨
 * 3. 인터페이스의 메서드 레벨
 * 4. 인터페이스 레벨
 *
 * 스프링에서는 항상 더 구체적이고 자세한 것이 높은 우선 순위를 가진다!
 */
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService service;

    @Test
    void orderTest() {
        service.write();
        service.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    static class LevelService {

        @Transactional(readOnly = false) // 읽기 쓰기 트랜잭션(default)
        public void write() {
            log.info("call write");

            printTxInfo();
        }

        public void read() {
            log.info("call read");

            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);

            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);
        }
    }
}
