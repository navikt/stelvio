package no.stelvio.esb.models.transformation.servicemodel2html.utils;

import java.io.File;
import java.net.URISyntaxException;

import junit.framework.Assert;
import no.stelvio.esb.models.transformation.servicemodel2html.utils.PublishFileUtils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

public class PublishFileUtilsTest {
	@Test
	public void testGetComplextypeFilePath() throws URISyntaxException 
	{
		File outputDirectory = new File("C:/www3");
		String complextypeNamespace = "http://nav.no/virksomhet/grunnlag/opptjening/grunnlag/v1";
		String complextypeName = "Inntekt";
		
		IPath actualPath = PublishFileUtils.getComplextypeFilePath(outputDirectory, 
				complextypeNamespace, complextypeName);
		
		IPath expectedPath = new Path("C:/www3/nav.no/virksomhet/grunnlag/opptjening/grunnlag/v1/Inntekt.html");
		
		Assert.assertEquals(expectedPath, actualPath);
	}

	@Test
	public void testGetLinkToComplextype() throws URISyntaxException
	{
		String outputDirPath = "C:/www3";
		String currentFilePath = "C:/www3/nav.no/virksomhet/grunnlag/opptjening/grunnlag/v1/Inntekt.html";
		
		// #1
		String toComplextypeNamespace = "http://nav.no/virksomhet/grunnlag/opptjening/grunnlag/v1";
		String toComplextypeName = "InntektType";
		
		String actualRelativePath = PublishFileUtils.getLinkToComplextype(outputDirPath, currentFilePath, 
				toComplextypeNamespace, toComplextypeName);
		
		String expectedRelativePath = toComplextypeName + ".html";
		
		Assert.assertEquals(expectedRelativePath, actualRelativePath);
		
		// #2
		toComplextypeNamespace = "http://nav.no/virksomhet/grunnlag/opptjening/";
		toComplextypeName = "InntektType";
		
		actualRelativePath = PublishFileUtils.getLinkToComplextype(outputDirPath, currentFilePath, 
				toComplextypeNamespace, toComplextypeName);
		
		expectedRelativePath = "../../" + toComplextypeName + ".html";
		
		// #3
		toComplextypeNamespace = "http://nav.no/virksomhet/grunnlag/opptjening/v2";
		toComplextypeName = "InntektType";
		
		actualRelativePath = PublishFileUtils.getLinkToComplextype(outputDirPath, currentFilePath, 
				toComplextypeNamespace, toComplextypeName);
		
		expectedRelativePath = "../../v2/" + toComplextypeName + ".html";
		
		Assert.assertEquals(expectedRelativePath, actualRelativePath);
	}
	
	@Test
	public void testGetLinkToOperation() throws URISyntaxException
	{
		String outputDirPath = "C:/www3";
		String currentFilePath = "C:/www3/nav.no/virksomhet/tjenester/arkiv/feil/v1/AdresseIkkeRegistrert.html";
		
		// #1
		String interfaceNamespace = "http://nav.no/virksomhet/tjenester/arkiv/v1";
		String interfaceName = "Arkiv";
		String operationName = "bestillBrev";
		
		String actualRelativePath = PublishFileUtils.getLinkToOperation(outputDirPath, currentFilePath, interfaceNamespace, interfaceName, operationName);
		
		String expectedRelativePath = "../../v1/Arkiv/" + operationName + ".html";
		
		Assert.assertEquals(expectedRelativePath, actualRelativePath);
	}
	
	@Test
	public void testGetLinkToDiagram() throws URISyntaxException
	{
		String outputDirPath = "C:/www3";
		String currentFilePath = "C:/www3/nav.no/virksomhet/tjenester/arkiv/feil/v1/AdresseIkkeRegistrert.html";
		
		// #1
		String diagramUUID = "_XtR1AG5dEd-83-Pa-yC_Pw";
		
		String actualRelativePath = PublishFileUtils.getLinkToDiagram(outputDirPath, currentFilePath, diagramUUID);
		
		String expectedRelativePath = "../../../../../../diagrams/" + diagramUUID + "." + PublishFileUtils.DIAGRAM_IMAGE_FILE_FORMAT;
		
		Assert.assertEquals(expectedRelativePath, actualRelativePath);
	}
	

}
