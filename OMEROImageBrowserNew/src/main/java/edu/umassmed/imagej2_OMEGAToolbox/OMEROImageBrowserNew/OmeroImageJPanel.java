package edu.umassmed.imagej2_OMEGAToolbox.OMEROImageBrowserNew;
import java.io.IOException;
import java.rmi.ServerError;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.gui.dialogs.GenericMessageDialog;
import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.data.OmeroImageWrapper;
import edu.umassmed.omega.omero.commons.gui.OmeroPanel;

public class OmeroImageJPanel extends OmeroPanel {
	
	private OmeroImageBrowser browser;
	
	public OmeroImageJPanel(RootPaneContainer parent,OmeroGateway gateway, OmeroImageBrowser browser) {
		super(parent, gateway);
		this.browser = browser;
	}

	private static final long serialVersionUID = -5740459087763362607L;

	@Override
	public void loadData(final boolean hasToSelect)
			throws IOException, ServerError {
		List<OmeroImageWrapper> imgWrappers = getImageWrapperToBeLoadedList();
		if(imgWrappers.size() > 5) {
			if(!browser.getConfirmation())
				setLoadingCanceled();
				return;
		}
		for(OmeroImageWrapper imgWrapper : imgWrappers) {
			browser.loadImage(imgWrapper.getPixelsID());
		}
		
		this.getImageWrapperToBeLoadedList().clear();
		this.getProjectPanel().updateLoadedElements(null);
		this.getBrowserPanel().updateLoadedElements(null);
	}

	@Override
	public void onCloseOperation() {
		// TODO Auto-generated method stub
	}
}
