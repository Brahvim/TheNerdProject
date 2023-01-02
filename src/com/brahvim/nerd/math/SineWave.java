package com.brahvim.nerd.math;

import com.brahvim.nerd.processing_wrapper.Sketch;

public class SineWave {
    // region Fields.
    public float angleOffset, freqMult, freq;
    private float endTime = Float.MAX_VALUE - 1, aliveTime;

    /**
     * Determines if a {@code SineWave} object is doing calculations or not.
     * {@code public} so that you can change it! (...and stop the sine wave
     * calculations if they're unnecessary.)
     * 
     * @apiNote {@code false} by default. Call {@code SineWave::start()} to
     *          make the wave active!
     */
    public boolean active = true;
    private boolean pactive = false;

    /**
     * Makes {@code SineWave::get()} output {@code 0} when the wave has
     * ended - AKA, when {@code SineWave::active} turns {@code false}.
     *
     * @apiNote {@code false} by default.
     */
    public boolean zeroWhenInactive;

    /**
     * Makes {@code SineWave::get()} output its absolute value. This may make the
     * wave behave as if it has doubled in frequency. Toggling this {@code boolean}
     * while the wave is active may show unwantedly large, sudden changes in the
     * value returned by {@code SineWave::get()}.
     *
     * @apiNote {@code false} by default.
     */
    public boolean absoluteValue;

    private Runnable onEnd;
    private Sketch parentSketch;
    // endregion

    // region Constructors.
    public SineWave(Sketch p_parentSketch) {
        this.parentSketch = p_parentSketch;
    }

    public SineWave(Sketch p_parentSketch, float p_freqMult) {
        this.freqMult = p_freqMult;
        this.parentSketch = p_parentSketch;
    }

    // No matter what order you put them in, the API Note comes after the Author
    // name. Oof.
    /**
     * @author Brahvim
     * @apiNote {@code p_angleOffset} must be in radians!
     */
    public SineWave(float p_freqMult, float p_angleOffset) {
        this.freqMult = p_freqMult;
        this.angleOffset = p_angleOffset;
        // this.angleOffset = Math.abs((float) Math.toRadians(p_angleOffset));
    }
    // endregion

    // region `start()` overloads and `setAngleOffset()`.
    public void start() {
        this.aliveTime = 0;
    }

    public void start(float p_angleOffset) {
        this.aliveTime = 0;
        this.angleOffset = p_angleOffset;
    }

    public void start(Runnable p_onEnd) {
        this.onEnd = p_onEnd;
    }

    public void start(float p_angleOffset, Runnable p_onEnd) {
        this.aliveTime = 0;
        this.onEnd = p_onEnd;
        this.angleOffset = p_angleOffset;
    }

    public void setAngleOffset(float p_angleOffset) {
        this.angleOffset = p_angleOffset;
    }
    // endregion

    // region End and extend!
    public void end() {
        this.endTime = 0;
    }

    public void endIn(float p_millis) {
        this.endTime = this.aliveTime + p_millis;
    }

    public void endWhenAngleIs(float p_angle) {
        // Of course this magic-number stuff won't work everytime! REPLACE THIS?!
        // ~~(Also because... any multiple of `0.25` less than `1` works...)~~
        // Oh hey! That looks perfect!:
        this.endTime = 0.9f * // <--- The magic number!.
                Math.abs(((float) Math.toRadians(p_angle) - this.angleOffset) / this.freqMult);

        // Was an idea to avoid I don't get negative values and the frequency doesn't
        // double - but I didn't use it.. don't ask me why :joy::
        // this.endTime = ((float) Math.toRadians(p_angle) - this.angleOffset)
        // / this.freqMult; // PS This was what I calculated on paper.
        // this.endTime = Math.abs((this.endTime - this.aliveTime) * 0.5f);

        // The calculation I used in the frametime version of this class, as seen in
        // the Nerd Game Engine:
        // this.endTime = this.aliveTime + ((float) (p_angle) * ((float)
        // Math.toRadians(p_angle) *
        // this.freqMult)) - this.angleOffset;

        // From the early days - the framecount calculation!:
        // this.endTime = this.aliveTime + (int) (p_angle * (p_angle * this.freqMult)
        // - this.angleOffset);

        // System.out.println(this.aliveTime);
        // System.out.println(this.endTime);
    }

    public void extendEndBy(float p_millis) {
        this.endTime += p_millis;
    }

    public void extendEndByAngle(float p_angle) {
        this.endTime += 0.9f *
                Math.abs(((float) Math.toRadians(p_angle) - this.angleOffset) / this.freqMult)
                - this.endTime;

        // this.endTime += (p_angle * (p_angle * this.freqMult) - this.angleOffset);
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

    public float get() {
        this.pactive = this.active;
        this.active = this.aliveTime <= this.endTime;

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

        this.freq = this.aliveTime * this.freqMult + this.angleOffset;
        // That looked like a matrix calculation LOL.

        float toRet = (float) Math.sin(this.freq);
        return this.absoluteValue ? Math.abs(toRet) : toRet;
    }
    // endregion

}
