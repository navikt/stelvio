package no.stelvio.esb.models.transformation.servicemodel2html;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class ServicemodelToHtmlTransformationRunner {

	private Logger logger = Logger
			.getLogger(ServicemodelToHtmlTransformationRunner.class);

	public void run(File inputFile, File outputDirectory) throws IOException, SAXException,
			ParserConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, URISyntaxException {
		logger.debug("**************** Starting servicemodel-to-html transformation ****************");

		// 0. validate that input model file exists
		//logger.debug("0. Validating");
		// TODO: validate

		// 1. create output directory if it does not exists
		logger.debug("1. Creating output directory " + outputDirectory.getAbsolutePath());
		outputDirectory.mkdir();

		// 2. copy shared stylesheet directory to the output directory
		logger.debug("2. Copy shared stylesheet directory to the output directory ");
		PublishFileUtils.copyCssDirectory(outputDirectory);

		// 3. copy shared images directory to the output directory
		logger.debug("3. Copy shared images directory to the output directory");
		PublishFileUtils.copyImagesDirectory(outputDirectory);
		
		// 4. copy shared images directory to the output directory
		logger.debug("4. Copy shared javascript directory to the output directory");
		PublishFileUtils.copyJavascriptDirectory(outputDirectory);

		// 5. publish frontpage to the root of the output directory
		logger.debug("5. Publish frontpage to the root of the output directory.");
		WebPublishFrontpage.run(inputFile, outputDirectory);
		
		// 6. publish service operations to output directory
		logger.debug("6. Publish all service operations to the output directory");
		WebPublishOperations.run(inputFile, outputDirectory);

		// 7. publish complex types to output directory
		logger.debug("7. Publish all complex types to the output directory");
		WebPublishComplexTypes.run(inputFile, outputDirectory);
		
		logger.debug("**************** Finished servicemodel-to-html transformation ****************");
	}

}
