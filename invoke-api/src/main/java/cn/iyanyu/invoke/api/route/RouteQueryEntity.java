package cn.iyanyu.invoke.api.route;

import cn.iyanyu.invoke.api.route.enums.ZoneType;
import io.micrometer.common.util.StringUtils;

public class RouteQueryEntity {
    private String routeElementsType;
    private String routeElementsValue;
    private String legalCode = "9999";
    private String systemId;
    private ZoneType zoneType;

    public RouteQueryEntity() {
    }

    public String getRouteElementsType() {
        return this.routeElementsType;
    }

    public void setRouteElementsType(String routeElementsType) {
        this.routeElementsType = routeElementsType;
    }

    public String getRouteElementsValue() {
        return this.routeElementsValue;
    }

    public void setRouteElementsValue(String routeElementsValue) {
        this.routeElementsValue = routeElementsValue;
    }

    public String getLegalCode() {
        return this.legalCode;
    }

    public void setLegalCode(String legalCode) {
        this.legalCode = StringUtils.isBlank(legalCode) ? "9999" : legalCode;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public ZoneType getZoneType() {
        return this.zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public String toString() {
        return "RouteQueryEntity{routeElementsType='" + this.routeElementsType + "', routeElementsValue='" + this.routeElementsValue + "', legalCode='" + this.legalCode + "', systemId='" + this.systemId + "', zoneType=" + this.zoneType + "}";
    }
}