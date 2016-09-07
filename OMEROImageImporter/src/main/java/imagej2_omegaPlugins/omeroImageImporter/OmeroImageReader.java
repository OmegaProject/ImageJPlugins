package imagej2_omegaPlugins.omeroImageImporter;

import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.ServerError;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.OmeroImporterUtilities;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import net.imagej.ops.Op;
import omero.client;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageReader")
public class OmeroImageReader implements Op {
	
	@Parameter
	client omeroClient;
	
	@Parameter(label = "ID of OMERO image")
	private Long omeroImageID;
	
	@Parameter
	private OMEROService ome;
	
	@Parameter
	private ConvertService cs;
	
	@Parameter(type = ItemIO.OUTPUT)
	Dataset dataset;

	@Override
	public void run() {
		
		try {
			dataset = ome.downloadImage(omeroClient, omeroImageID);
		} catch (omero.ServerError ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public static void main(final String... args) {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Launch our "Hello World" command right away.
		ij.command().run(OmeroImageReader.class, true);
	}
}
