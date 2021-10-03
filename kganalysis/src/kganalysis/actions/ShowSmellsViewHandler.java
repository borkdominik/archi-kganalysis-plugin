package kganalysis.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import com.archimatetool.editor.ui.services.ViewManager;
import kganalysis.views.SmellsView;

public class ShowSmellsViewHandler extends AbstractHandler {
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // activate = false to keep originating part in focus so we can update based on current selection
        ViewManager.toggleViewPart(SmellsView.ID, false);
        return null;
    }
}
