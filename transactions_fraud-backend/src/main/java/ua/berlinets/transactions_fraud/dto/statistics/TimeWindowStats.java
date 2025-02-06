package ua.berlinets.transactions_fraud.dto.statistics;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

public class TimeWindowStats {
    @Getter
    private final long windowStart;
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong blocked = new AtomicLong(0);
    private final AtomicLong approved = new AtomicLong(0);

    public TimeWindowStats(long windowStart) {
        this.windowStart = windowStart;
    }


    public long getTotal() {
        return total.get();
    }

    public long getBlocked() {
        return blocked.get();
    }

    public long getApproved() {
        return approved.get();
    }

    public void incrementTotal() {
        total.incrementAndGet();
    }

    public void incrementBlocked() {
        blocked.incrementAndGet();
    }

    public void incrementApproved() {
        approved.incrementAndGet();
    }

    public void decrementApproved() {
        approved.decrementAndGet();
    }

    public TimeWindowStats copy() {
        TimeWindowStats copy = new TimeWindowStats(windowStart);
        copy.total.set(this.total.get());
        copy.blocked.set(this.blocked.get());
        copy.approved.set(this.approved.get());
        return copy;
    }
}