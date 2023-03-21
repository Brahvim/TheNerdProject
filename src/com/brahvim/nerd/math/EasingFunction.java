package com.brahvim.nerd.math;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public abstract class EasingFunction {

    // region Fields.
    public float parameter, parameterOffset;
    public float endTime = Float.MAX_VALUE - 1, aliveTime;

    /**
     * Returned by {@code SineFunction::get()} if
     * {@code SineFunction::useInactValue} is
     * {@code true}.
     */
    public float inactValue = 0;

    /**
     * Makes {@code SineFunction::get()} output {@code SineFunction::inactValue}
     * when the
     * wave has ended - AKA, when {@code SineFunction::active} turns {@code false}.
     *
     * @apiNote {@code true} by default. <br>
     *          <br>
     *          {@code SineFunction::inactValue} is {@code 0}
     *          by default.
     */
    public boolean useInactValue = false;

    /**
     * Determines if an {@link EasingFunction} object is doing calculations or not.
     * {@code public} so you can change it!
     * (...and stop calculations if they're unnecessary.)
     *
     * @apiNote {@code false} by default. Call {@link EasingFunction#start()} to
     *          make the function actively processing again.
     */
    public boolean active = true;

    protected final Sketch SKETCH;
    protected boolean pactive = false;
    protected float lastValue;
    protected Runnable onEnd;
    // endregion

    protected EasingFunction(Sketch p_parentSketch) {
        this.SKETCH = p_parentSketch;
    }

    // region `start()` overloads and `setParameterOffset()`.
    public EasingFunction start() {
        this.aliveTime = 0;
        return this;
    }

    public EasingFunction start(Runnable p_onEnd) {
        this.onEnd = p_onEnd;
        this.aliveTime = 0;
        return this;
    }

    public EasingFunction start(float p_paramOffset) {
        this.parameterOffset = p_paramOffset;
        this.aliveTime = 0;
        return this;
    }

    public EasingFunction start(float p_paramOffset, Runnable p_onEnd) {
        this.parameterOffset = p_paramOffset;
        this.onEnd = p_onEnd;
        this.aliveTime = 0;
        return this;
    }
    // endregion

    // region End and extend!
    public EasingFunction stop() {
        this.endTime = 0;
        return this;
    }

    public EasingFunction endIn(int p_millis) {
        this.endTime = this.aliveTime + p_millis;
        return this;
    }

    public EasingFunction extendEndBy(int p_millis) {
        this.endTime += p_millis;
        return this;
    }
    // endregion

    public EasingFunction setOffset(float p_paramOffset) {
        this.parameterOffset = p_paramOffset;
        return this;
    }

    public EasingFunction addOffset(float p_paramOffset) {
        this.parameterOffset += p_paramOffset;
        return this;
    }

    // *Abstractness!:tm:*
    protected abstract float apply();

    // region Getters.
    public float getStartTime() {
        return this.SKETCH.millis() - this.aliveTime;
    }

    public float getTimeSinceStart() {
        return this.aliveTime;
    }

    public float getLastValue() {
        return this.lastValue;
    }

    public float getEndTime() {
        return this.endTime;
    }

    public boolean wasActive() {
        return this.pactive;
    }

    public float get() {
        this.pactive = this.active;
        this.active = this.aliveTime <= this.endTime;

        if (this.active)
            this.aliveTime += this.SKETCH.frameTime;
        // ^^^ `frameTime` comes from "the Engine" by the way. (Hey - that's "Nerd"!)
        else { // If no longer active,
            if (this.pactive)
                if (this.onEnd != null)
                    this.onEnd.run();

            return this.useInactValue ? this.inactValue : this.lastValue;
        }

        this.lastValue = this.apply();
        return this.lastValue;
    }
    // endregion

}
