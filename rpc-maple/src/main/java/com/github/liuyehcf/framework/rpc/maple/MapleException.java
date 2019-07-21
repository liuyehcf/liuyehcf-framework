package com.github.liuyehcf.framework.rpc.maple;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public class MapleException extends RuntimeException {

    public static final String BIZ_SCOPE = "BIZ";
    public static final String MAPLE_SCOPE = "MAPLE";

    private Code code;

    public MapleException(final Code code) {
        this.code = code;
    }

    public MapleException(final Code code, final String message) {
        super(message);
        this.code = code;
    }

    public MapleException(final Code code, final Throwable cause) {
        super(cause);
        this.code = code;
    }

    public MapleException(final Code code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public enum Code {
        IO(MAPLE_SCOPE),
        SERIALIZE(MAPLE_SCOPE),
        REGISTER_SERVICE_FAILED(MAPLE_SCOPE),
        FETCH_SERVICE_FAILED(MAPLE_SCOPE),
        NETWORK(MAPLE_SCOPE),
        UNKNOWN(MAPLE_SCOPE),


        TIMEOUT(BIZ_SCOPE),
        CANNOT_FIND_TARGET_SERVICE(BIZ_SCOPE);

        private String scope;

        Code(final String scope) {
            this.scope = scope;
        }

        public String getScope() {
            return scope;
        }
    }
}
