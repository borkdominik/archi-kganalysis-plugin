package kganalysis;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import kganalysis.views.EASmellsView;
import com.archimatetool.editor.ui.services.ViewManager;


public class ShowEASmellsView extends AbstractHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // activate = false to keep originating part in focus so we can update current selection
        ViewManager.toggleViewPart(EASmellsView.ID, false); 
        return null;
    }
}
