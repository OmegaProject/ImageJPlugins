package edu.umassmed.imagej2_OMEGAToolbox.OMEROImageBrowserNew;
import java.io.IOException;
import java.rmi.ServerError;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.data.OmeroImageWrapper;
import edu.umassmed.omega.omero.commons.gui.OmeroPanel;

public class OmeroImageJPanel extends OmeroPanel {
	
	private long imageID;
	private OmeroImageBrowser browser;
	
	public OmeroImageJPanel(RootPaneContainer parent,OmeroGateway gateway, OmeroImageBrowser browser) {
		super(parent, gateway);
		this.browser = browser;
	}

	private static final long serialVersionUID = -5740459087763362607L;

	@Override
	public void loadData(final boolean hasToSelect)
			throws IOException, ServerError {
		for(OmeroImageWrapper imgWrapper : getImageWrapperToBeLoadedList()) {
			imageID = imgWrapper.getPixelsID();
		}
		
		this.getImageWrapperToBeLoadedList().clear();
		this.getProjectPanel().updateLoadedElements(null);
		this.getBrowserPanel().updateLoadedElements(null);
		
		browser.loadImage(imageID);
	}

	@Override
	public void onCloseOperation() {
		// TODO Auto-generated method stub
	}
}
