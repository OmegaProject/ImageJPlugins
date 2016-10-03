package imagej2_omegaPlugins.omeroImageImporter;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROCredentials;
import net.imagej.omero.OMEROService;
import net.imagej.omero.OMEROSession;
import net.imagej.ops.Op;
import omero.client;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

@Plugin(type = Op.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageReader_V11.3")
public class OmeroImageReader3 implements Op {
	
	@Parameter
	private client client;
	
	@Parameter
	private Long id;
	
//	@Parameter(label = "Show image?")
//	private boolean show;
	
	//private List<Long> ids;
	
	@Parameter
	private OMEROService ome;
	
//	@Parameter
//	private ConvertService cs;
	
	@Parameter
	private UIService ui;
	
	@Parameter(type = ItemIO.OUTPUT)
	Dataset dataset;

	@Override
	public void run() {
		//for(Long id : ids) {
		try {
			dataset = ome.downloadImage(client, id);
		} catch (omero.ServerError ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
//		if(show && dataset != null)
//			ui.show(dataset);
		//}
	}
	
	public static void main(final String... args) {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Launch our "Hello World" command right away.
		ij.command().run(OmeroImageReader3.class, true);
		//ij.op().run(OmeroImageReader.class, true);
	}
}
