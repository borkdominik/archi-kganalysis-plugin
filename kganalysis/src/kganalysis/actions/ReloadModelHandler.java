package kganalysis.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateModel;

import kganalysis.KGPlugin;
import kganalysis.db.KGDatabase;
import kganalysis.db.KGExporter;

public class ReloadModelHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;

	if (model != null) {
	    try {
		IRunnableWithProgress initKG = new ReloadModelThread(model);
		new ProgressMonitorDialog(new Shell()).run(true, true, initKG);

	    } catch (Exception ex) {

		Logger.log(IStatus.ERROR, "Error reloading model", ex);
		MessageDialog.openError(HandlerUtil.getActiveShell(event), "Error reloading Model",
			(ex.getMessage() == null ? ex.toString() : ex.getMessage()));
	    }

	    IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
		    .getService(IHandlerService.class);
	    try {
		handlerService.executeCommand("kganalysis.command.showKGGraph", null);
	    } catch (Exception ex) {
		Logger.log(IStatus.ERROR, "Error opening Knowledge Graph", ex);
		MessageDialog.openError(HandlerUtil.getActiveShell(event), "Knowledge Graph Preview",
			(ex.getMessage() == null ? ex.toString() : ex.getMessage()));
	    }
	}

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

    private static class ReloadModelThread implements IRunnableWithProgress {
	private IArchimateModel activeModel;

	public ReloadModelThread(IArchimateModel activeModel) {
	    this.activeModel = activeModel;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
	    monitor.beginTask("Reloading active ArchiMate model into database", 100);

	    // 1 - Delete all nodes
	    deleteElements(monitor);
	    // 2 - Export Archi Model as CSV
	    exportAsCSV(monitor);
	    // 3 - Load CSV into db
	    transformModel(monitor);

	    // TODO: Cancel operation
	    if (monitor.isCanceled()) {
		monitor.done();
		return;
	    }

	    monitor.done();

	}

	private void deleteElements(IProgressMonitor monitor) throws InvocationTargetException {
	    monitor.subTask("Removing current model...");
	    KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
	    db.removeData();
	    monitor.worked(30);
	}

	private void exportAsCSV(IProgressMonitor monitor) throws InvocationTargetException {
	    monitor.subTask("Exporting active ArchiMate model to CSV");
	    CSVExporter exporter = new CSVExporter(activeModel);
	    try {
		exporter.export(KGPlugin.KG_FOLDER);
	    } catch (IOException e) {
		throw new InvocationTargetException(e, "Could not export archi model to user folder");
	    }
	    monitor.worked(10);
	}

	private void transformModel(IProgressMonitor monitor) throws InvocationTargetException {
	    KGExporter kgExporter = new KGExporter();
	    kgExporter.export(activeModel);
	    monitor.worked(60);
	}

    }

}
