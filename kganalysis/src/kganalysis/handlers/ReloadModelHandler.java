package kganalysis.handlers;

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


/**
 * Handler to reload the currently selected model into the existing graph database
 * 
 * TODO: Refactor this (similar to wizard)
 */
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
		return KGPlugin.getDefault().isGraphDbStarted();
	}

	private static class ReloadModelThread implements IRunnableWithProgress {
		private IArchimateModel activeModel;

		public ReloadModelThread(IArchimateModel activeModel) {
			this.activeModel = activeModel;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Reloading active ArchiMate model into database", IProgressMonitor.UNKNOWN);
			
			try {
				KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
				db.removeData();
				CSVExporter csvExporter = new CSVExporter(activeModel);
				csvExporter.export(KGPlugin.KG_FOLDER);
				KGExporter kgExporter = KGPlugin.INSTANCE.getExporter();
				kgExporter.export(activeModel);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// TODO: Cancel operation
			if (monitor.isCanceled()) {
				monitor.done();
				return;
			}

			monitor.done();

		}

	}

}
