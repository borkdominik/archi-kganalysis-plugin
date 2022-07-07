package kganalysis;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateModel;
import kganalysis.db.KGDatabase;
import kganalysis.db.KGExporter;

/**
 * Wizard used to create a new knowledge graph 
 *
 */
public class KnowledgeGraphWizard extends Wizard {
	
	private KnowledgeGraphWizardPage page;
	private IArchimateModel model;
	
	public KnowledgeGraphWizard() {
		setWindowTitle("New Knowledge Graph");
	}
	
	
	@Override
    public void addPages() {
        page = new KnowledgeGraphWizardPage();
        addPage(page);
    }
	
	@Override
	public boolean canFinish() {
		return page.getSelectedModel() != null;
	}
	
	@Override
	public boolean performFinish() {
		model = page.getSelectedModel();
		return true;
	}
	
	public void runWithProgress() throws InvocationTargetException, InterruptedException {
		IRunnableWithProgress runnable = monitor -> {
			try {
				monitor.beginTask("Creating Knowledge Graph", IProgressMonitor.UNKNOWN);
				
				// 1) Create and start new neo4j Graph DB
				KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
				db.createDb();

				// 2) Export model as CSV (used for relationships)
				CSVExporter csvExporter = new CSVExporter(model);
				csvExporter.export(KGPlugin.KG_FOLDER);
				
				// 3) Copy files for browser
				copyFiles();

				// 4) Transform ArchiMate model and store in DB
				KGExporter kgExporter = KGPlugin.INSTANCE.getExporter();
				kgExporter.export(model);
				
			} catch (Throwable ex) {
				Display.getCurrent().asyncExec( new Runnable() {
                    @Override
                    public void run() {
                        // TODO: Cancel 
                    	/*
                    	if (ex instanceof CancelledException) {
                            MessageDialog.openInformation(getShell(), Messages.ExtendedReportsWizard_0, ex.getMessage());
                        }
                        */
                        Logger.log(IStatus.ERROR, "Exception while creating Knowledge Graph.", ex);
                        MessageDialog.openError(getShell(), "Error! Could not create Knowledge Graph", ex.getMessage());
                    }
                });
			} finally {
				monitor.done();
			}
		};
		
		PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
	}
	
	// TODO: Improve this
	private void copyFiles() throws IOException {
		URL url = new URL("platform:/plugin/kganalysis/files/index.html");
		File indexFile = new File(KGPlugin.KG_FOLDER, "index.html");
		FileUtils.copyURLToFile(url, indexFile);
		url = new URL("platform:/plugin/kganalysis/files/smells.js");
		File smellsFile = new File(KGPlugin.KG_FOLDER, "smells.js");
		FileUtils.copyURLToFile(url, smellsFile);
		url = new URL("platform:/plugin/kganalysis/files/index.js");
		File jsFile = new File(KGPlugin.KG_FOLDER, "index.js");
		FileUtils.copyURLToFile(url, jsFile);
		url = new URL("platform:/plugin/kganalysis/files/styles.css");
		File cssFile = new File(KGPlugin.KG_FOLDER, "styles.css");
		FileUtils.copyURLToFile(url, cssFile);
	}

}
