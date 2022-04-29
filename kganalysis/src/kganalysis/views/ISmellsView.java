package kganalysis.views;

import org.eclipse.ui.IViewPart;

public interface ISmellsView extends IViewPart {
	
    String ID = "kganalysis.smellsView";
    String HELP_ID = "kganalysis.smellsViewHelp";
    String NAME = "EA Smells";
    void detectSmells();
}
