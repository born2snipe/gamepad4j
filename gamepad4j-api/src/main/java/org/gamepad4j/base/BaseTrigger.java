/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.base;

import org.gamepad4j.IAxis;
import org.gamepad4j.IController;
import org.gamepad4j.ITrigger;
import org.gamepad4j.TriggerID;

/**
 * Abstract base class for trigger wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseTrigger implements ITrigger {

	/** Stores the axis for a trigger trigger. */
	private IAxis axis = null;

	/** Stores the deviceID of this trigger. */
	protected TriggerID ID = TriggerID.UNKNOWN;
	
	/** Stores the label of this trigger. */
	protected String label = null;

	/** Stores the label resource bundle key of this trigger. */
	protected String labelKey = null;

	/** The numeric code of the controller trigger. */
	protected int code = -1;
	
	/** Stores the controller to which this trigger belongs. */
	protected IController controller = null;
	
	/**
	 * Creates a trigger wrapper.
	 * 
	 * @param code The numeric code of the trigger.
	 * @param isAnalog Set to true if this is an analog trigger.
	 * @param label The text label (may be null).
	 * @param labelKey The text label key (may be null).
	 */
	public BaseTrigger(IController controller, int code, IAxis axis, String label, String labelKey) {
		this.controller = controller;
		this.code = code;
		this.axis = axis;
		this.label = label;
		this.labelKey = labelKey;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getID()
	 */
	@Override
	public TriggerID getID() {
		return this.ID;
	}

	/**
	 * Sets the ID of this trigger as defined by the mapping.
	 * 
	 * @param ID The ID for this trigger.
	 */
	public void setID(TriggerID ID) {
		this.ID = ID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IButton#getIndex()
	 */
	@Override
	public int getCode() {
		return this.code;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.base.BaseButton#analogValue()
	 */
	@Override
	public float analogValue() {
		return this.axis.getValue();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getLabelKey()
	 */
	@Override
	public String getLabelKey() {
		return this.labelKey;
	}

	/**
	 * @param label the label to set
	 */
	public void setDefaultLabel(String label) {
		this.label = label;
	}


	/**
	 * @param labelKey the labelKey to set
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}


	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getDefaultLabel()
	 */
	@Override
	public String getDefaultLabel() {
		return this.label;
	}

}
