package kganalysis;

import com.archimatetool.editor.ui.ImageFactory;

public interface IKGAnalysisImages {

    ImageFactory ImageFactory = new ImageFactory(KGPlugin.INSTANCE);
    String IMG_PATH = "icons/";
    String ICON_START = IMG_PATH + "start.png";
    String ICON_STOP = IMG_PATH + "stop.png";
    String ICON_LOGO = IMG_PATH + "logo.png";
    String WIZARD_LOGO = IMG_PATH + "logo_wizard.png";
    String ICON_SMELLS = IMG_PATH + "smells.png";
    String ICON_RELOAD = IMG_PATH + "reload.png";

}
