package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class StatisticsResult<T> {

    private final Status status;
    private final String reason;
    private final T value;

    private StatisticsResult(Status status, String reason, T value) {
        this.status = status;
        this.reason = reason;
        this.value = value;
    }

    public static <T> StatisticsResult<T> undetermined() {
        return new StatisticsResult<>(Status.undetermined, null, null);
    }

    public static <T> StatisticsResult<T> acceptedOf(T value) {
        return new StatisticsResult<>(Status.accepted, null, value);
    }

    public static <T> StatisticsResult<T> alreadyAccepted() {
        return new StatisticsResult<>(Status.alreadyAccepted, null, null);
    }

    public static <T> StatisticsResult<T> rejectedOf(String reason) {
        return new StatisticsResult<>(Status.rejected, reason, null);
    }

    public static <T> StatisticsResult<T> alreadyRejected() {
        return new StatisticsResult<>(Status.alreadyRejected, null, null);
    }

    public Status getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public T getValue() {
        return value;
    }

    public enum Status {

        /**
         * not enough data has been received to make a decision
         */
        undetermined,

        /**
         * accepted, meaning that majority member response for true
         */
        accepted,

        /**
         * if receiving majority+1 votes for true, return this status in order to forbidding multiply accept
         */
        alreadyAccepted,

        /**
         * rejected, meaning that sub-majority member response for false
         */
        rejected,

        /**
         * if receiving sub-majority+1 votes for false, return this status in order to forbidding multiply reject
         */
        alreadyRejected,
        ;

        public boolean isUndetermined() {
            return undetermined.equals(this);
        }

        public boolean isAccepted() {
            return accepted.equals(this);
        }

        public boolean isAlreadyAccepted() {
            return alreadyAccepted.equals(this);
        }

        public boolean isRejected() {
            return rejected.equals(this);
        }

        public boolean isAlreadyRejected() {
            return alreadyRejected.equals(this);
        }
    }
}
