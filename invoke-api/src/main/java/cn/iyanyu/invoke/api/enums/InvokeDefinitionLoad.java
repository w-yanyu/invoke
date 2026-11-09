package cn.iyanyu.invoke.api.enums;

public enum InvokeDefinitionLoad {
    FILE("file"),
    DB("db"),
    ANNOTATION("annotation");

    private String id;

    private InvokeDefinitionLoad(String id) {
        this.id = id;
    }

    public static InvokeDefinitionLoad getById(String id) {
        return switch (id) {
            case "file" -> FILE;
            case "db" -> DB;
            case "annotation" -> ANNOTATION;
            default -> throw new RuntimeException("Unknown service invoke definition load type[" + id + "]");
        };
    }

    public String getId() {
        return this.id;
    }
}
