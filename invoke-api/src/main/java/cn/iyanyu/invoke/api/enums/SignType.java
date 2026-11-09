package cn.iyanyu.invoke.api.enums;

public enum SignType {
    RSA("rsa"),
    SHA256("SHA256"),
    SM3("SM3"),
    NO("NO");

    private String text;

    private SignType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}