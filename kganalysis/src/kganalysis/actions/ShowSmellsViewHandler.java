package kganalysis.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;

import kganalysis.KGPlugin;
import kganalysis.db.KGDatabase;
import kganalysis.views.ISmellsView;

public class ShowSmellsViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	ViewManager.toggleViewPart(ISmellsView.ID, false);
	return null;
    }

    @Override
    public boolean isEnabled() {
	KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
	if (db == null || db.isStarted() == false) {
	    return false;
	}
	return true;
    }

}
