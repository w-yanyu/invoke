package cn.iyanyu.invoke.core.protocol.mq;

import cn.iyanyu.base.spec.util.SystemPropertiesUtil;
import cn.iyanyu.invoke.api.config.InvokeDefinition;
import cn.iyanyu.invoke.api.model.InvokeContext;
import cn.iyanyu.invoke.api.model.InvokeRequest;
import cn.iyanyu.invoke.api.model.InvokeResponse;
import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.enums.SendStatus;
import cn.iyanyu.invoke.api.mq.producer.MqMessageProducer;
import cn.iyanyu.invoke.api.pkg.InvokePkgConverter;
import cn.iyanyu.invoke.api.pkg.MockPkgLoader;
import cn.iyanyu.invoke.api.protocol.InvokeProtocolHandler;
import cn.iyanyu.invoke.api.route.RouteResult;
import cn.iyanyu.invoke.comm.util.SpringUtils;
import cn.iyanyu.invoke.error.exception.support.AppOnlError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

public class MqProtocolHandler implements InvokeProtocolHandler {

    @Override
    public <RES> void invoke(InvokeContext<RES> invokeContext) {
        InvokeDefinition invokeDefinition = invokeContext.getInvokeDefinition();
        InvokeRequest invokeRequest = invokeContext.getInvokeRequest();
        InvokeResponse<RES> invokeResponse = invokeContext.getInvokeResponse();
        RouteResult routeResult = invokeRequest.getRouteResult();
        Map<String, String> requestHeader = invokeRequest.getRequestHeader();
        String topic = "";
        if (StringUtils.hasLength(routeResult.getTargetSystemId())) {
            // 标记为重试主题
            topic = "%RETRY%" + routeResult.getTargetSystemId() + "_" + routeResult.getTargetApplicationName() + "_" + routeResult.getTargetZoneId() + "_" + SystemPropertiesUtil.getEnvId();
        } else {
            topic = invokeDefinition.getExternalSvcApplicationId();
        }

        Map<String, Object> requestParams = this.getPkgConverter(invokeDefinition.getPkgConverterType()).convertRequest(invokeRequest.getRequestParams(), requestHeader);

        Object resultBody = null;
        if (invokeDefinition.isMockFlag()) {
            resultBody = this.doMock(invokeDefinition);
        } else {
            try {
                MqMessageProducer producer = SpringUtils.getBean(MqMessageProducer.class);
                if (producer == null) {
                    throw AppOnlError.InvokeError.E0005();
                }

                MqMessage mqMessage = new MqMessage();
                mqMessage.setMessageTopic(topic);
                mqMessage.setMessageBody(requestParams);
                mqMessage.setProperties(requestHeader);

                String globalBusinessTrackNo = requestParams.get("globalBusinessTrackNo").toString();
                mqMessage.setGlobalBusinessTrackNo(globalBusinessTrackNo);
                MqSendResult sendResult = producer.syncSend(mqMessage);
                if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    throw new RuntimeException("[Service Invoke] [MQ] Send MQ message failed, send status is[" + sendResult + "]");
                }

                OnlineResponse onlineResponse = new OnlineResponse();
                onlineResponse.putIntoSys(SysMappingKey.globalBusinessTrackNo, globalBusinessTrackNo);
                onlineResponse.putIntoSys(SysMappingKey.responseTypeCode, "01");
            } catch (Exception var16) {
                throw AppOnlError.InvokeError.E0006(var16.getMessage(), var16);
            }
        }
    }

    @Override
    public InvokePkgConverter getPkgConverter(String converterType) {
        InvokePkgConverter converter = SpringUtils.getBean(converterType, InvokePkgConverter.class);
        if (converter == null) {
            throw AppOnlError.InvokeError.E0004(converterType);
        } else {
            return converter;
        }
    }

    @Override
    public String getSpiId() {
        return "mq";
    }

    private ResponseEntity<Object> doMock(InvokeDefinition invokeDefinition) {
        MockPkgLoader mockPkgLoader = SpringUtils.getBean(MockPkgLoader.class);
        String mockPkg = mockPkgLoader.loadMockPkg(invokeDefinition);
        return new ResponseEntity<>(mockPkg, HttpStatus.OK);
    }

}