package cn.iyanyu.invoke.api.mq.route;

import java.util.List;

public interface MQZoneToDcMapper {
    List<String> getDcIdByZoneId(String zoneId);
}
