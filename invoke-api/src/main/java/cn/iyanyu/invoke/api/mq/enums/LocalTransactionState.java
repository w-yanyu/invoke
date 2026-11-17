package cn.iyanyu.invoke.api.mq.enums;

public enum LocalTransactionState {
    COMMIT_MESSAGE,
    ROLLBACK_MESSAGE,
    UNKNOW;

    private LocalTransactionState() {
    }
}