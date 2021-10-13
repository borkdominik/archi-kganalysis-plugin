package kganalysis.views;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;

// import com.archimatetool.editor.utils.PlatformUtils;

public class KGAnalysisViewer extends TreeViewer {
	
	private String[] columnNames = {
            "Smell",
            "Description",
            "Object"
    };
	
	private int[] columnWeights = {
            20,
            60,
            20
    };
	
	public KGAnalysisViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI | SWT.FULL_SELECTION | SWT.NONE);
        
        setContentProvider(new KGAnalysisViewerContentProvider());
        setLabelProvider(new KGAnalysisViewerLabelProvider());
        
        getTree().setHeaderVisible(true);
        getTree().setLinesVisible(true);
        
        // Use layout from parent container
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();
        
        for(int i = 0; i < columnNames.length; i++) {
            final TreeColumn column = new TreeColumn(getTree(), SWT.NONE);
            column.setText(columnNames[i]);
            column.setData(i);
            layout.setColumnData(column, new ColumnWeightData(columnWeights[i], true));
            
            // TODO: Add Listener for sorting
        }
    }
	
	
	class KGAnalysisViewerContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	class KGAnalysisViewerLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return "";
		}
		
	}
	
}
