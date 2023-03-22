package com.brahvim.nerd.math;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * @deprecated
 *             <h1>Use {@link SineEase} now!</h1>
 *             I though, will perhaps, forever, remember
 *             {@code SineWave}'s beautiful, beautiful legacy...
 */
@Deprecated
public class SineWave {

    // region Fields.
    public float freq;
    public float angleOffset;

    /**
     * Returned by {@code SineWave::get()} if {@code SineWave::useInactValue} is
     * {@code true}.
     */
    public float inactValue = 0;

    /**
     * @apiNote {@code 0.001f} by default.
     */
    public float freqMult = 0.001f;

    /**
     * Determines if a {@code SineWave} object is doing calculations or not.
     * {@code public} so that you can change it! (...and stop the sine wave
     * calculations if they're unnecessary.)
     * 
     * @apiNote {@code false} by default. Call {@code SineWave::start()} to
     *          make the wave active!
     */
    public boolean active = true;

    /**
     * Makes {@code SineWave::get()} output {@code SineWave::inactValue} when the
     * wave has ended - AKA, when {@code SineWave::active} turns {@code false}.
     *
     * @apiNote {@code true} by default. <br>
     *          <br>
     *          {@code SineWave::inactValue} is {@code 0}
     *          by default.
     */
    public boolean useInactValue = false;

    /**
     * Makes {@code SineWave::get()} output its absolute value. This may make the
     * wave behave as if it has doubled in frequency. Toggling this {@code boolean}
     * while the wave is active may show unwantedly large, sudden changes in the
     * value returned by {@code SineWave::get()}.
     *
     * @apiNote {@code false} by default.
     */
    public boolean absoluteValue = false;

    private Runnable onEnd;
    private final Sketch SKETCH;
    private boolean pactive = false;
    private float endTime = -1, aliveTime, lastValue;
    // endregion

    // region Constructors.
    public SineWave(Sketch p_parentSketch) {
        this.SKETCH = p_parentSketch;
    }

    public SineWave(Sketch p_parentSketch, float p_freqMult) {
        this.freqMult = p_freqMult;
        this.SKETCH = p_parentSketch;
    }

    // No matter what order you put them in, the API Note comes after the Author
    // name. Oof.
    /**
     * @author Brahvim
     * @apiNote {@code p_angleOffset} must be in radians!
     */
    public SineWave(Sketch p_sketch, float p_freqMult, float p_angleOffset) {
        this.SKETCH = p_sketch;
        this.freqMult = p_freqMult;
        this.angleOffset = p_angleOffset;
        // this.angleOffset = Math.abs((float) Math.toRadians(p_angleOffset));
    }
    // endregion

    // region `start()` overloads and `setAngleOffset()`.
    public SineWave start() {
        this.aliveTime = 0;
        return this;
    }

    public SineWave start(float p_angleOffset) {
        this.aliveTime = 0;
        this.angleOffset = p_angleOffset;
        return this;
    }

    public SineWave start(Runnable p_onEnd) {
        this.onEnd = p_onEnd;
        return this;
    }

    public SineWave start(float p_angleOffset, Runnable p_onEnd) {
        this.angleOffset = p_angleOffset;
        this.onEnd = p_onEnd;
        this.aliveTime = 0;
        return this;
    }

    public SineWave setAngleOffset(float p_angleOffset) {
        this.angleOffset = p_angleOffset;
        return this;
    }
    // endregion

    // region End and extend!
    public SineWave end() {
        this.endTime = 0;
        return this;
    }

    public SineWave endIn(float p_millis) {
        this.endTime = this.aliveTime + p_millis;
        return this;
    }

    public SineWave endWhenAngleIncrementsBy(float p_angle) {
        // Don't ask me why it took me `10` months to get this to work, even with the
        // right formula in my head, since day `1`!
        // ...totally didn't forget to convert to radians or something!

        p_angle = PApplet.radians(PApplet.abs(p_angle));
        this.endTime = this.aliveTime + (p_angle - this.angleOffset) / this.freqMult;

        // "Debug Laag":
        // System.out.printf("Alive-time: `%f`, end-time: `%f`, angle: `%f`.\n",
        // this.aliveTime, this.endTime, PApplet.degrees(this.freq % PConstants.TAU));

        return this;
    }

    /**
     * Method to put the wave at a given angle within given time.
     */
    public SineWave endWhenAngleIncrementsToWithin(float p_angle, float p_before) {
        p_angle = PApplet.radians(PApplet.abs(p_angle));
        this.freqMult = (p_angle - this.angleOffset) / p_before;
        return this;

        // if (this.endTime < this.aliveTime + p_before) {
        // this.endTime -= p_before;
        // return this;
        // } else // If they're equal, this still takes place!
        // return this.end();
    }

    public SineWave extendEndByAngle(float p_angle) {
        float endTime = this.endTime; // Benefits of using `this.`!~
        this.endWhenAngleIncrementsBy(p_angle);
        this.endTime += endTime;
        return this;
    }

    public SineWave extendEndByMillis(float p_millis) {
        this.endTime += p_millis;
        return this;
    }
    // endregion

    // region Getters.
    public float getStartTime() {
        return this.SKETCH.millis() - this.aliveTime;
    }

    public float getTimeSinceStart() {
        return this.aliveTime;
    }

    public float getEndTime() {
        return this.endTime;
    }

    public float get() {
        this.pactive = this.active;

        if (this.endTime != -1)
            this.active = this.aliveTime <= this.endTime;

        if (this.active)
            this.aliveTime += this.SKETCH.frameTime;
        // ^^^ `frameTime` comes from "the Engine" by the way. (Hey - that's "Nerd"!)
        else { // If no longer active,
            if (this.pactive) {
                System.out.printf("Ended at angle: `%f`.\n",
                        PApplet.degrees(this.freq % PConstants.TAU));

                if (this.onEnd != null)
                    this.onEnd.run();
            }

            return this.useInactValue ? this.inactValue : this.lastValue;
        }

        this.freq = this.aliveTime * this.freqMult + this.angleOffset;
        // That looked like a matrix calculation LOL.
        this.lastValue = (float) Math.sin(this.freq);

        return this.absoluteValue ? Math.abs(this.lastValue) : this.lastValue;
    }
    // endregion

}
