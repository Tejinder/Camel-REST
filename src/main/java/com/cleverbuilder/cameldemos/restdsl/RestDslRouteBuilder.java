package com.cleverbuilder.cameldemos.restdsl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.restlet.data.MediaType;
import org.restlet.representation.InputRepresentation;
import org.springframework.stereotype.Component;


@Component
public class RestDslRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        /**
         * Use 'restlet', which is a very simple component for providing REST
         * services. Ensure that camel-restlet or camel-restlet-starter is
         * included as a Maven dependency first.
         */
        restConfiguration()
                .component("restlet")
                .host("localhost").port("8080")
                .bindingMode(RestBindingMode.off);

        /**
         * Configure the REST API (POST, GET, etc.)
         */
      /*  rest().path("/api").consumes("application/json")
                .get()
                    .to("bean:helloBean")
                .post().type(PostRequestType.class)
                    .to("bean:postBean");
      */  
       //rest().path("/apiFile").consumes("multipart/form-data").post().type(FilePostRequestType.class).to("bean:fileBean");
        
        
        rest("/images").description("Image Upload Service")
        .consumes("multipart/form-data").produces("application/json")
        .post().description("Uploads image")
                .to("direct:uploadImage");

        from("direct:uploadImage")
        .process(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {

                MediaType mediaType = 
                    exchange.getIn().getHeader(Exchange.CONTENT_TYPE, MediaType.class);
                String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
                InputRepresentation representation =
                    new InputRepresentation(
                        exchange.getIn().getBody(InputStream.class), mediaType);
                
               System.out.println("Size ==>"+ representation.getSize());
              // copyFile(representation.getStream(),fileName);
               
               InputStream inputStream = representation.getStream();
               Path destination = Paths.get("MyFile.txt");
               Files.copy(inputStream, destination,
                           StandardCopyOption.REPLACE_EXISTING);
               /*
               try {
                    List<FileItem> items = 
                        new RestletFileUpload(
                            new DiskFileItemFactory()).parseRepresentation(representation);

                    for (FileItem item : items) {
                        if (!item.isFormField()) {
                            InputStream inputStream = item.getInputStream();
                            Path destination = Paths.get("MyFile.jpg");
                            Files.copy(inputStream, destination,
                                        StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

*/
            }
            
            public void copyFile(InputStream fileInputStream , String fileName) 
            {
            	try
            	{
        	    	File dest = new File("E:\\APACHE-CAMEL");
        	    	
        	    	//File sourceFile = new File("C:\\Users\\Demo\\Downloads\\employee\\"+img);
        	    	File destinationFile = new File("E:\\\\APACHE-CAMEL\\");
        	
        	    	//FileInputStream fileInputStream = new FileInputStream(sourceFile);
        	    	FileOutputStream fileOutputStream = new FileOutputStream(
        	    	                destinationFile);
        	
        	    	int bufferSize;
        	    	byte[] bufffer = new byte[512];
        	    	while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
        	    	    fileOutputStream.write(bufffer, 0, bufferSize);
        	    	}
        	    	fileInputStream.close();
        	    	fileOutputStream.close();
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            	}
            }

        });
        
        
        
    }
}
