package hello.springtx.order;

/**
 * 결제 잔고가 부족할 때 발생하는 비즈니스(체크) 예외
 */
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
