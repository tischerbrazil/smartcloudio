package org.geoazul.model.website;

import jakarta.faces.component.UIOutcomeTarget;

public class RUIOutcomeTarget extends RUIOutput {

	/**
	 * <p>
	 * The standard component type for this component.
	 * </p>
	 */
	public static final String COMPONENT_TYPE = "org.risteos.OutcomeTarget";

	/**
	 * <p>
	 * The standard component family for this component.
	 * </p>
	 */
	public static final String COMPONENT_FAMILY = "org.risteos.OutcomeTarget";

	

	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}

	private String includeViewParams;
	private String outcome;
	private String disableClientWindow;
	private int column;
	private Long blockId;
	

	/**
	 * <p>
	 * Create a new {@link UIOutcomeTarget} instance with default property values.
	 * </p>
	 */
	public RUIOutcomeTarget() {
		super();
		setRendererType("jakarta.faces.Link");
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public void setColumn(int column) {
		this.column = column;
	}

	public String getIncludeViewParams() {
		return includeViewParams;
	}

	public void setIncludeViewParams(String includeViewParams) {
		this.includeViewParams = includeViewParams;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getDisableClientWindow() {
		return disableClientWindow;
	}

	public void setDisableClientWindow(String disableClientWindow) {
		this.disableClientWindow = disableClientWindow;
	}
	
	@Override
	public Long getBlockId() {
		return blockId;
	}

	@Override
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}
	
	

}
