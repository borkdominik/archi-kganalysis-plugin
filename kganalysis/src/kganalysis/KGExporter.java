package kganalysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateModel;

public class KGExporter {

	private IArchimateModel fModel;
	
	public static File PREVIEW_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-preview");	
	
	
	 public KGExporter(IArchimateModel model) {
	        fModel = model;
	 }
	 
	 
	 public void showExternalBrowser() throws Exception {
		 PREVIEW_FOLDER.mkdirs();
		 //createKG(PREVIEW_FOLDER);
	        
	        	try {
	                //File file = createKG(PREVIEW_FOLDER); //$NON-NLS-1$
	        		
	            	//File file = new File(PREVIEW_FOLDER, "index.html");
	                
	            	//BrowserEditorInput input = new BrowserEditorInput("file:///" + file.getPath() + "index.html");
	        		// Open it in external Browser
	                IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
	                IWebBrowser browser = support.getExternalBrowser();
	                // This method supports network URLs
	                browser.openURL(new URL("file", null, "/Users/philipp/" + "index.html"));
	        		
	        		/*
	        		BrowserEditorInput input = new BrowserEditorInput("file:///Users/philipp/" + "index.html");
	            	IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
	                 if(editor != null && editor.getBrowser() != null) {
	                     editor.getBrowser().refresh();
	                 }
	                 */
	            }
	            catch(Exception ex) { // Catch OOM and SWT exceptions
	                System.err.println("Exception");
	            }
	        
	 }
	 
	 public void showInternalBrowser() throws Exception {
		 PREVIEW_FOLDER.mkdirs();
		 //createKG(PREVIEW_FOLDER);
	        
	        	try {
	                

	        		BrowserEditorInput input = new BrowserEditorInput("file:///Users/philipp/" + "index.html");
	            	IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
	                if(editor != null && editor.getBrowser() != null) {
	                     editor.getBrowser().refresh();
	                }

	            }
	            catch(Exception ex) { // Catch OOM and SWT exceptions
	                System.err.println("Exception");
	            }
	        
	 }
	 
	 
	 
	 public void createKG(File targetFolder) throws IOException {
		 File srcDir = new File(KGAnalysisPlugin.INSTANCE.getTemplatesFolder(), "html"); //$NON-NLS-1$
	     FileUtils.copyFolder(srcDir, targetFolder);
	     
	 }
	 
	 
	 
}
