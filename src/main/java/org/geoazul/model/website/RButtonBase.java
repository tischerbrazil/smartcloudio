package org.geoazul.model.website;

public class RButtonBase extends RHtmlOutcomeTargetButton {

	public static final String COMPONENT_FAMILY = "org.risteos.component";

	private String widgetVar;
	private String fragment;
	private String disabled;
	private String icon;
	private String iconPos;
	private String href;
	private String target;
	private String escape;
	private String inline;
	private String ariaLabel;
	private String disableClientWindow;

	public String getWidgetVar() {
		return widgetVar;
	}

	public void setWidgetVar(String widgetVar) {
		this.widgetVar = widgetVar;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconPos() {
		return iconPos;
	}

	public void setIconPos(String iconPos) {
		this.iconPos = iconPos;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getEscape() {
		return escape;
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}

	public String getInline() {
		return inline;
	}

	public void setInline(String inline) {
		this.inline = inline;
	}

	public String getAriaLabel() {
		return ariaLabel;
	}

	public void setAriaLabel(String ariaLabel) {
		this.ariaLabel = ariaLabel;
	}

	public String getDisableClientWindow() {
		return disableClientWindow;
	}

	public void setDisableClientWindow(String disableClientWindow) {
		this.disableClientWindow = disableClientWindow;
	}

}
