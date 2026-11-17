package cn.iyanyu.invoke.api.mq.common;

import cn.iyanyu.invoke.api.mq.enums.SendStatus;

public class MqSendResult {
    private SendStatus sendStatus;
    private String msgId;
    private String transactionId;

    public MqSendResult() {
    }

    public MqSendResult(SendStatus sendStatus, String msgId) {
        this.sendStatus = sendStatus;
        this.msgId = msgId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public SendStatus getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String toString() {
        return "MqSendResult{sendStatus=" + this.sendStatus + ", msgId='" + this.msgId + "', transactionId='" + this.transactionId + "'}";
    }
}