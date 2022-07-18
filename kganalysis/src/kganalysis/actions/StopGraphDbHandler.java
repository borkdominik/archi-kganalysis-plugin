package kganalysis.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import kganalysis.KGPlugin;
import kganalysis.db.KGDatabase;


public class StopGraphDbHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
		db.shutDown();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}

}
