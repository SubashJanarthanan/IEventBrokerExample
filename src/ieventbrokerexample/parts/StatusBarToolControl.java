package ieventbrokerexample.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusBarToolControl extends Composite {
	
	private Label lbltext;
	
	@Inject
	private UISynchronize uiSynchronize;

	@Inject
	public StatusBarToolControl(final Composite parent) {
		super(parent, SWT.BORDER);
	}
	
	@Inject
	@Optional
	public void getEvent(@UIEventTopic(IEventConstants.STATUS_BAR) final String message) {
		updateInterface(message);
	}

	@PostConstruct
	public void createGui() {
		GridLayoutFactory.fillDefaults().applyTo(this.getParent());
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(this);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(this);
		this.lbltext = createLabel(this);
	}
	
	private Label createLabel(final Composite parent) {
		final Composite body = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(body);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(body);
		final Label label = new Label(body, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).indent(5, 0).grab(true, true).applyTo(label);
		return label;
	}
	
	public void updateInterface(final String message) {
		try {
			this.uiSynchronize.asyncExec(new Runnable() {
				/**
				 * Run method
				 */
				@Override
				public void run() {
					try {
						if (lbltext != null && !lbltext.isDisposed()) {
							lbltext.setText(message);
							lbltext.requestLayout();
							lbltext.getParent().redraw();
							lbltext.getParent().getParent().update();
						}
					} catch (Exception exc) {
						System.err.println(exc.getMessage());
					}
				}
			});
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
	}
}