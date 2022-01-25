package kganalysis.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;

public class InitKnowledgeGraph extends AbstractModelSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {

			IRunnableWithProgress initKG = new InitKGThread(10);
            new ProgressMonitorDialog(new Shell()).run(true, true, initKG);
            
         } catch (InvocationTargetException ex) {
             ex.printStackTrace();
         } catch (InterruptedException ex) {
             ex.printStackTrace();
         }
        
        return null;
	}
	
	private static class InitKGThread implements IRunnableWithProgress {
		
		private int workload;
		
		public InitKGThread(int workload) {
            this.workload = workload;
        }

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			
			monitor.beginTask("Initialize Knowledge Graph", workload);
			
			
			for(int i = 0; i < workload; i++)
            {
                // Optionally add subtasks
                monitor.subTask("Copying file " + (i+1) + " of "+ workload + "...");

                Thread.sleep(2000);

                // Tell the monitor that you successfully finished one item of "workload"-many
                monitor.worked(1);

                // Check if the user pressed "cancel"
                if(monitor.isCanceled())
                {
                    monitor.done();
                    return;
                }
            }

            // You are done
            monitor.done();
			
		}
		
	}

}
