package kganalysis.views;


import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;




public class SmellsViewer extends TreeViewer {
	
	private String[] columnNames = {
            "Name / Object",
            "Description"
    };
	
	private int[] columnWeights = {
            30,
            70
    };

	public SmellsViewer(Composite parent, int style) {
		super(parent, style);
		
		
		getTree().setHeaderVisible(true);
        getTree().setLinesVisible(true);
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();
        
        for(int i = 0; i < columnNames.length; i++) {
            final TreeColumn column = new TreeColumn(getTree(), SWT.NONE);
            column.setText(columnNames[i]);
            column.setData(i);
            layout.setColumnData(column, new ColumnWeightData(columnWeights[i], true));
		
        }
	}
	
	
	
	

}
