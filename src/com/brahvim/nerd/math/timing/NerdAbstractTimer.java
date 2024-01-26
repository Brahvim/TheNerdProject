package com.brahvim.nerd.math.timing;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public abstract class NerdAbstractTimer implements AutoCloseable {

    public final Supplier<Long> TIME_PROVIDER_FUNCTION;
    public final String TRACKED_EVENT_NAME, UNIT_NAME;
    public final PrintStream LOG_STREAM;

    protected long startTime, endTime;
    protected AtomicBoolean active = new AtomicBoolean();

    public static <TimerT extends NerdAbstractTimer> TimerT performBenchmark(
            Runnable p_codeToBench,
            final Class<TimerT> p_timerClass) {
        TimerT timer = null;

        try {
            timer = p_timerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        if (timer == null)
            return null;

        p_codeToBench.run();
        timer.close();

        return timer;
    }

    public static <TimerT extends NerdAbstractTimer> TimerT performBenchmark(
            Runnable p_codeToBench,
            final Class<TimerT> p_timerClass,
            final String p_eventNameToLog) {
        TimerT timer = null;

        try {
            timer = p_timerClass.getConstructor(String.class).newInstance(p_eventNameToLog);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        if (timer == null)
            return null;

        p_codeToBench.run();
        timer.close();

        return timer;
    }

    public static <TimerT extends NerdAbstractTimer> TimerT performBenchmark(
            Runnable p_codeToBench,
            final Class<TimerT> p_timerClass,
            final String p_eventNameToLog,
            final PrintStream p_streamToLogTo) {
        TimerT timer = null;

        try {
            timer = p_timerClass.getConstructor(String.class, PrintStream.class)
                    .newInstance(p_eventNameToLog, p_streamToLogTo);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        if (timer == null)
            return null;

        p_codeToBench.run();
        timer.close();
        return timer;
    }

    // region Constructors.
    protected NerdAbstractTimer() {
        this.TIME_PROVIDER_FUNCTION = this.supplyTimeProviderFunction();
        this.UNIT_NAME = this.supplyUnitName();
        this.TRACKED_EVENT_NAME = "";
        this.LOG_STREAM = null;
        this.restart();
    }

    protected NerdAbstractTimer(final String p_eventNameToLog) {
        this.TIME_PROVIDER_FUNCTION = this.supplyTimeProviderFunction();
        this.UNIT_NAME = this.supplyUnitName();
        this.TRACKED_EVENT_NAME = p_eventNameToLog;
        this.LOG_STREAM = System.out;
        this.restart();
    }

    protected NerdAbstractTimer(final String p_eventNameToLog, final PrintStream p_streamToLogTo) {
        this.TIME_PROVIDER_FUNCTION = this.supplyTimeProviderFunction();
        this.UNIT_NAME = this.supplyUnitName();
        this.TRACKED_EVENT_NAME = p_eventNameToLog;
        this.LOG_STREAM = p_streamToLogTo;
        this.restart();
    }
    // endregion

    public abstract Supplier<Long> supplyTimeProviderFunction();

    public abstract String supplyUnitName();

    // region State manipulation!
    public void close() {
        // First, prevent race conditions from letting `get()` return incorrect values
        // based on `endTime`!
        this.active.set(false);
        this.endTime = System.currentTimeMillis();

        if (this.LOG_STREAM == null)
            return;

        this.LOG_STREAM.printf(
                "%s took `%s` %s.%n",
                this.TRACKED_EVENT_NAME, this.get(), this.UNIT_NAME);
    }

    public void restart() {
        this.active.set(true);
        this.startTime = System.currentTimeMillis();
    }
    // endregion

    // region Duration getters.
    public long get() {
        return this.active.get() ? System.currentTimeMillis() - this.startTime : this.endTime - this.startTime;
    }

    public int getInt() {
        return (int) this.get();
    }

    public float getFloat() {
        return this.get();
    }
    // endregion

}
