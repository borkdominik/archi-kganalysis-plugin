package kganalysis.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import kganalysis.EASmell;

public class SmellFilter extends ViewerFilter {
	private String searchString;

    public void setSearchText(String s) {
        // ensure that the value can be used for matching
        this.searchString = ".*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer,
            Object parentElement,
            Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        EASmell s = (EASmell) element;
        if (s.getName().matches(searchString)) {
            return true;
        }

        return false;
    }
}
