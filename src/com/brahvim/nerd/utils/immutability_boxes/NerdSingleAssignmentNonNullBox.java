package com.brahvim.nerd.utils.immutability_boxes;

public class NerdSingleAssignmentNonNullBox<ValueT> {

    protected ValueT value;
    protected boolean assigned;

    public NerdSingleAssignmentNonNullBox() {
    }

    public NerdSingleAssignmentNonNullBox(final ValueT p_value) {
        this.assign(p_value);
    }

    /**
     * @param p_value is the value to put in this box!
     * @return Does the box already <i>have</i> something in it?
     */
    public boolean assign(final ValueT p_value) {
        // Assignment is more likely in most cases:
        if (!(this.assigned || p_value == null)) {
            this.value = p_value;
            this.assigned = true;
        }

        return this.assigned;
    }

    /** @return Does the box already <i>have</i> something in it? */
    public boolean isAssigned() {
        return this.assigned;
    }

    public ValueT getValue() {
        return this.value;
    }

}
