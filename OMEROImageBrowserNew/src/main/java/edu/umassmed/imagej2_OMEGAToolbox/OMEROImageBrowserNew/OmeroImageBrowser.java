package edu.umassmed.imagej2_OMEGAToolbox.OMEROImageBrowserNew;

import java.awt.BorderLayout;
import java.rmi.ServerError;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.DefaultOMEROService;
import net.imagej.omero.OMEROCredentials;
import net.imagej.omero.OMEROService;
import net.imagej.ops.OpService;
import omero.client;

import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.OmeroImporterUtilities;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageBrowser")
public class OmeroImageBrowser implements Command {

	@Parameter(label = "Hostname")
	private String host = "Localhost";
	@Parameter(label = "Port")
	private int port = 4064;
	@Parameter(label = "Username")
	private String name;
	@Parameter(label = "Password")
	private String psw;
	
	@Parameter
	private UIService ui;
	
	@Parameter
	private OpService op;

	@Parameter(type = ItemIO.OUTPUT)
	private Dataset dataset;
	
	private OmeroImageJPanel panel;
	private client client;

	@Override
	public void run() {
		OmeroGateway gateway = new OmeroGateway();
		
		JFrame frame = new JFrame();
		
		frame.setTitle("OMERO image importer");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		panel = new OmeroImageJPanel(frame, gateway, this);
		
		JMenuBar menuBar = panel.getMenu();
		frame.setJMenuBar(menuBar);
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		
		panel.updateStatus("Waiting for connection...");
		
		OmeroImporterUtilities.connectToGateway(frame, gateway, host, port, name, psw);
		boolean connected = gateway.isConnected();
		
		if(!connected) {
			//TODO Throw error dialog
			return;
		}
		
		client = gateway.getClient();
		panel.updateStatus("Connection estabilished!");
		
		try {
			panel.updateVisualizationMenu();
		} catch (ServerError ex) {
			//TODO Throw error dialog
			ex.printStackTrace();
		}
	}
	
	public void loadImage(Long id) {
		Dataset dataset = (Dataset) op.run(OmeroImageLoader.class, client, id);
		ui.show(dataset);
		
	}
	
	public static void main(final String... args) {
		Context context = new Context();
		ImageJ ij = new ImageJ(context);
		ij.launch(args);
		ij.command().run(OmeroImageBrowser.class, true);
	}
}
