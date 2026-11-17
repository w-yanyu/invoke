package cn.iyanyu.invoke.api.route.enums;

public enum ZoneType {
    AZone("AZone"),
    BZone("BZone"),
    CZone("CZone");

    private final String code;

    private ZoneType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ZoneType getEnum(String code) {
        ZoneType[] var1 = values();
        int var2 = var1.length;

        for (ZoneType e : var1) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }

        return null;
    }
}