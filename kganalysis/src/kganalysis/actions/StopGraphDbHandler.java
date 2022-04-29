package kganalysis.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import kganalysis.KGAnalysisPlugin;
import kganalysis.db.KGDatabase;


public class StopGraphDbHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
	KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
	if (db != null && db.isStarted()) {
	    db.shutDown();
	}
	return null;
    }

    @Override
    public boolean isEnabled() {
	KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
	if (db == null || db.isStarted() == false) {
	    return false;
	}
	return true;
    }

}
