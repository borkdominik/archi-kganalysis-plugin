package kganalysis.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;
import kganalysis.KGPlugin;
import kganalysis.views.KnowledgeGraphWizard;


public class CreateGraphHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
		if (model != null) {
        	KnowledgeGraphWizard wizard = new KnowledgeGraphWizard();
        	WizardDialog dialog = new ExtendedWizardDialog(HandlerUtil.getActiveShell(event), wizard, "KnowledgeGraphWizard");
        	if (dialog.open() == Window.OK) {
        		try {
        			wizard.runWithProgress();
        		} catch (InvocationTargetException | InterruptedException ex) {
        			ex.printStackTrace();
        		}
        	}
        }
        
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return !(KGPlugin.getDefault().isGraphDbStarted());
	}

}
