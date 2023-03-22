package com.brahvim.nerd.math;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// It is better to extend this class than to have `java.util.Function`s in it.
// How else would we have stuff like `SineFunction::freqMult`, then?!

public abstract class EasingFunction {

    // region Fields.
    public float endTime = -1, aliveTime;
    public float parameter, parameterOffset;

    /**
     * Returned by {@link EasingFunction#get()} if
     * {@link EasingFunction#useInactValue} is
     * {@code true}.
     */
    public float inactValue = 0;

    /**
     * Determines if an {@link EasingFunction} object is doing calculations or not.
     * 
     * @apiNote {@code false} by default. Call {@link EasingFunction#start()} to
     *          make the function actively processing again.
     */
    public boolean active = true;

    /**
     * Makes {@link EasingFunction#get()} output {@link EasingFunction#inactValue}
     * when the
     * wave has ended - AKA, when {@link EasingFunction#active} turns {@code false}.
     *
     * @apiNote {@code true} by default. <br>
     *          <br>
     *          {@link EasingFunction#inactValue} is {@code 0}
     *          by default.
     */
    public boolean useInactValue = false;

    /**
     * Makes {@link EasingFunction#get()} output its absolute value. This may make
     * the
     * wave behave as if it has doubled in frequency. Toggling this {@code boolean}
     * while the wave is active may show unwantedly large, sudden changes in the
     * value returned by {@link EasingFunction#get()}.
     *
     * @apiNote {@code false} by default.
     */
    public boolean absoluteValue = false;

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

    public EasingFunction extendEndByMillis(int p_millis) {
        this.endTime += p_millis;
        return this;
    }
    // endregion

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

        if (this.endTime != -1)
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
        return this.absoluteValue ? Math.abs(this.lastValue) : this.lastValue;
    }
    // endregion

}
