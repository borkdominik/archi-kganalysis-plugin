package kganalysis.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class KGAnalysisView extends ViewPart {
	
	
	public static String ID = "kganalysis.kgAnalysisView"; //$NON-NLS-1$
	
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
