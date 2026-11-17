package cn.iyanyu.invoke.api.route;

public class RouteResult {

    private boolean nullRouteResultFlag = false;
    private String targetZoneId;
    private String targetSystemId;
    private String targetApplicationName;

    public RouteResult(boolean nullRouteResultFlag) {
        this.nullRouteResultFlag = nullRouteResultFlag;
    }

    public boolean isNullRouteResultFlag() {
        return nullRouteResultFlag;
    }

    public void setNullRouteResultFlag(boolean nullRouteResultFlag) {
        this.nullRouteResultFlag = nullRouteResultFlag;
    }

    public String getTargetZoneId() {
        return targetZoneId;
    }

    public void setTargetZoneId(String targetZoneId) {
        this.targetZoneId = targetZoneId;
    }

    public String getTargetSystemId() {
        return targetSystemId;
    }

    public void setTargetSystemId(String targetSystemId) {
        this.targetSystemId = targetSystemId;
    }

    public String getTargetApplicationName() {
        return targetApplicationName;
    }

    public void setTargetApplicationName(String targetApplicationName) {
        this.targetApplicationName = targetApplicationName;
    }
}
