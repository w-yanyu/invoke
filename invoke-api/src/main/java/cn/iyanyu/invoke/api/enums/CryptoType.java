package cn.iyanyu.invoke.api.enums;

public enum CryptoType {
    RSA("rsa"),
    AES("aes"),
    DES("des"),
    SM4("SM4"),
    BASE64("BASE64"),
    NO("NO");

    private String text;

    private CryptoType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
