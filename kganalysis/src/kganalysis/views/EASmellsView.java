package kganalysis.views;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

import kganalysis.EASmell;
import kganalysis.SmellsProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;


public class EASmellsView extends ViewPart {
	
	public static String ID = "kganalysis.eaSmellsView"; //$NON-NLS-1$

	private TableViewer viewer;
	private SmellFilter filter;
	
	@Override
    public void createPartControl(Composite parent) {	
		
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		
		// -- Search --
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        createViewer(parent);
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                filter.setSearchText(searchText.getText());
                viewer.refresh();
            }

        });
        filter = new SmellFilter();
        viewer.addFilter(filter);
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(SmellsProvider.INSTANCE.getSmells());
        getSite().setSelectionProvider(viewer);
        
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
	}
	
	public TableViewer getViewer() {
        return viewer;
    }
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Name", "Description", "Solution" };
		int[] bounds = { 150, 300, 300};
		
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            
        	@Override
            public String getText(Object element) {
                EASmell smell = (EASmell) element;
                return smell.getName();
            }
        });
        
        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                EASmell p = (EASmell) element;
                return p.getDescription();
            }
        });
        createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                EASmell p = (EASmell) element;
                return p.getSolution();
            }
        });
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
                SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;

    }
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();	
	}
}
