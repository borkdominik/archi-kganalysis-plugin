package kganalysis.views;

import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import kganalysis.KGAnalysisPlugin;

public class SmellsView extends ViewPart {
	
	public static String ID = KGAnalysisPlugin.PLUGIN_ID + ".smellsView"; //$NON-NLS-1$
	private Label label;
	

	@Override
	public void createPartControl(Composite parent) {
		label = new Label(parent, 0);
		label.setText("Hello World");

	}

	@Override
	public void setFocus() {
		label.setFocus();
	}

}
