package com.brahvim.nerd.math;

import java.util.function.Function;

import com.brahvim.nerd.papplet_wrapper.Sketch;

/**
 * Might even be removed later :|<br>
 * <br>
 * Brought to you, *from* my other (currently supa'-duper secret!) project,
 * "AGC"!:
 *
 * @deprecated ...for now.
 */
@Deprecated
public class EasingFunction {
    // region Fields.
    public float parameter, parameterOffset;
    public float endTime = Float.MAX_VALUE - 1, aliveTime;

    /**
     * Determines if an `EasingFunction` object is doing calculations or not.
     * `public` so that you can change it!
     * (...and stop calculations if they're unnecessary.)
     *
     * @apiNote `false` by default. Call `.start()` to make the function's
     *          processing active!
     */
    public boolean active = true;
    private boolean pactive = false;

    /**
     * Makes `get()` output `0` when processing time
     * has ended - AKA, when `active` turns `false`.
     *
     * @apiNote `false` by default.
     */
    public boolean zeroWhenInactive;

    /**
     * Takes in how many more milliseconds the {@linkplain EasingFunction}`
     * will run for, then sets it to inactive.
     */
    private Function<Float, Float> extensionFunction;

    /**
     * Takes in the parameter of the {@linkplain EasingFunction}
     * (which many then be processed using {@linkplain EasingFunction::apply(Float[]
     * p_parameters)},
     * to determine if the function can end. <b>The first parameter is the amount of
     * time the
     * {@linkplain EasingFunction} has been executing for.</b>
     */
    private Function<Float, Boolean> endCheck;

    private Function<Float, Float> function;

    private Sketch parentSketch;
    private Runnable onEnd;
    // endregion

    // region Constructors.
    public EasingFunction(Sketch p_parentSketch) {
        this.parentSketch = p_parentSketch;
    }

    public EasingFunction(Sketch p_parentSketch, Function<Float, Float> p_function) {
        this.function = p_function;
        this.parentSketch = p_parentSketch;
    }

    // No matter what order you put them in, the API Note comes after the Author
    // name. Oof.
    // endregion

    // region `start()` overloads and `setParameterOffset()`.
    public void start() {
        this.aliveTime = 0;
    }

    public void start(float p_parameteroffset) {
        this.aliveTime = 0;
        this.parameterOffset = p_parameteroffset;
    }

    public void start(Runnable p_onEnd) {
        this.onEnd = p_onEnd;
    }

    public void start(float p_parameterOffset, Runnable p_onEnd) {
        this.aliveTime = 0;
        this.onEnd = p_onEnd;
        this.parameter = p_parameterOffset;
    }
    // endregion

    // region End and extend!
    public void end() {
        this.endTime = 0;
    }

    public void endIn(float p_millis) {
        this.endTime = this.aliveTime + p_millis;
    }

    public void setEndCheck(Function<Float, Boolean> p_endCheckFunction) {
        this.endCheck = p_endCheckFunction;
    }

    public void extendUsing(Function<Float, Float> p_extensionFunction) {
        this.extensionFunction = p_extensionFunction;
    }

    public void extendEndBy(float p_millis) {
        this.endTime += this.extensionFunction.apply(p_millis);
    }
    // endregion

    // region Getters.
    public float getStartTime() {
        return this.parentSketch.millis() - this.aliveTime;
    }

    public float getTimeSinceStart() {
        return this.aliveTime;
    }

    public float getEndTime() {
        return this.endTime;
    }

    public boolean wasActive() {
        return this.pactive;
    }

    public float apply(/* float p_paramters */) {
        return this.function.apply(this.parameter);
    }

    public float get() {
        this.pactive = this.active;
        this.active = this.endCheck.apply(this.parameter);

        if (this.active)
            this.aliveTime += this.parentSketch.frameTime;
        // ^^^ `frameTime` comes from "the Engine" by the way. (Hey - that's "Nerd"!)
        else { // If no longer active,
            if (this.pactive)
                if (this.onEnd != null)
                    this.onEnd.run();

            if (this.zeroWhenInactive)
                return 0;
        }

        return this.function.apply(this.parameter + this.parameterOffset);
    }
    // endregion
}
