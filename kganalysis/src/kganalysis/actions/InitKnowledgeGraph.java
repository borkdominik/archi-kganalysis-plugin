package kganalysis.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.model.IArchimateModel;
import kganalysis.KGAnalysisPlugin;
import kganalysis.db.KGDatabase;


public class InitKnowledgeGraph extends AbstractModelSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IArchimateModel model = getActiveArchimateModel();
		
		if (model != null) {
			try {

				IRunnableWithProgress initKG = new InitKGThread(model);
				new ProgressMonitorDialog(new Shell()).run(true, true, initKG);
			} catch (Exception ex) {
				Logger.log(IStatus.ERROR, "Error opening Knowledge Graph", ex);
	         	MessageDialog.openError(HandlerUtil.getActiveShell(event),
	                     "Knowledge Graph Preview",
	                     (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
			}
			
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			try {
				handlerService.executeCommand("kganalysis.command.showKGGraph", null);
			} catch (Exception ex) {
				Logger.log(IStatus.ERROR, "Error opening Knowledge Graph", ex);
	         	MessageDialog.openError(HandlerUtil.getActiveShell(event),
	                     "Knowledge Graph Preview",
	                     (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
			}
		}
		
		return null;
	}
	
	
	private static class InitKGThread implements IRunnableWithProgress {
		
		private IArchimateModel activeModel;
		
		public InitKGThread(IArchimateModel activeModel) {
			this.activeModel = activeModel;
        }

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			
			monitor.beginTask("Initializing Knowledge Graph with current ArchiMate model...", 100);

			// 1 - Export Archi Model as CSV
			exportAsCSV(monitor);
			// 2 - Copy index.html 
			copyIndexFile(monitor);
			// 3. Start neo4j db
			startDb(monitor);
			// 4. Load CSV into db
			loadCSV(monitor);
			
			// TODO: Cancel operation
			if(monitor.isCanceled()) {
                monitor.done();
                return;
            }
			
            monitor.done();
			
		}
		
		private void exportAsCSV(IProgressMonitor monitor) throws InvocationTargetException {
			monitor.subTask("Exporting active ArchiMate model to CSV");
			CSVExporter exporter = new CSVExporter(activeModel);
			try {
				exporter.export(KGAnalysisPlugin.KG_FOLDER);
			} catch (IOException e) {
				throw new InvocationTargetException(e, "Could not export archi model to user folder");
			}
			monitor.worked(10);
		}
		
		
		private void copyIndexFile(IProgressMonitor monitor) throws InvocationTargetException {
			monitor.subTask("Copying necessary files");
			try {
				URL url = new URL("platform:/plugin/kganalysis/files/index.html");
				File indexFile = new File(KGAnalysisPlugin.KG_FOLDER, "index.html");
				FileUtils.copyURLToFile(url, indexFile);
			} catch (IOException e) {
				throw new InvocationTargetException(e, "Could not copy files to user folder");
			}
			
			monitor.worked(20);
		}
		
		private void startDb(IProgressMonitor monitor) throws InvocationTargetException {
			monitor.subTask("Starting neo4j database... (this may take a while)");
			KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
			try {
				db.createDb();
			} catch (IOException e) {
				throw new InvocationTargetException(e, "Could not start Neo4j database");
			}
			monitor.worked(50);
		}
		
		private void loadCSV(IProgressMonitor monitor) throws InvocationTargetException {
			monitor.subTask("Importing model into database...");
			KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
			db.importCSVModel(KGAnalysisPlugin.KG_FOLDER);
			monitor.worked(20);
		}
		
	}

}
