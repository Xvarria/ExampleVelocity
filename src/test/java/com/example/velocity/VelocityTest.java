package com.example.velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class VelocityTest {

	@Test
	public void testRenderTemplateFile() {
		VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();

        try {
	        Template template = ve.getTemplate("templates/template.vm");	        
	        this.renderTemplateIntoFile("target/output/rederedUsingFile.txt", "target/output", template);
				
		} catch (IOException e) {
			e.printStackTrace(); //DO Something
		}
            
		Assert.assertTrue(true);
	}
	
	@Test
	public void testRenderTemplateString() {

		VelocityEngine ve = new VelocityEngine();
		ve.init();
		
		try {
			StringReader reader = new StringReader("Template via String / Entity : ${entity} rendered");
			Template template = new Template();
			
			RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
			template.setRuntimeServices(runtimeServices);
			template.setData(runtimeServices.parse(reader, "Template name"));
			template.initDocument();
			
			this.renderTemplateIntoFile("target/output/rederedUsingString.txt", "target/output", template);
			
			Assert.assertTrue(true);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadModel(VelocityContext vc) {
		vc.put("entity", "WORLD!");
	}
	
	private void renderTemplateIntoFile(String fileName, String dirPath, Template template) throws IOException {
		VelocityContext vc = new VelocityContext();
		this.loadModel(vc);
		
		StringWriter sw = new StringWriter();
		template.merge(vc, sw);

		new File(dirPath).mkdirs();
		Path file = Paths.get(fileName);
		Files.write(file,Arrays.asList(sw.toString()),Charset.forName("UTF-8"));
	}
}
