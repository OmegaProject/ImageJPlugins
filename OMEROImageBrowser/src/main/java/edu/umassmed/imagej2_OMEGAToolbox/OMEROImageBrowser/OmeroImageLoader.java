package edu.umassmed.imagej2_OMEGAToolbox.OMEROImageBrowser;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import net.imagej.ops.Op;
import omero.client;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, headless = true)
public class OmeroImageLoader implements Op {
	
	@Parameter
	private client client;
	
	@Parameter
	private Long id;
	
	@Parameter
	private OMEROService ome;
	
	@Parameter(type = ItemIO.OUTPUT)
	Dataset dataset;

	@Override
	public void run() {
		try {
			dataset = ome.downloadImage(client, id);
		} catch (omero.ServerError ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
}
