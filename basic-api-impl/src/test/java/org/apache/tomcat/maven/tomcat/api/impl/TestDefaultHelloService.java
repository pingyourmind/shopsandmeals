package org.apache.tomcat.maven.tomcat.api.impl;

import junit.framework.TestCase;

import org.apache.catalina.Context;
import org.apache.catalina.deploy.ApplicationParameter;
import org.apache.catalina.startup.Tomcat;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.pingyourmind.shopsandmeals.HelloService;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author Francisco J Cano - franciscojavier.cano@lumatagroup.com
 */
@RunWith(JUnit4.class)
public class TestDefaultHelloService extends TestCase {
	int port;

	private Tomcat tomcat;

	@Before
	public void startTomcat() throws Exception {
		tomcat = new Tomcat();
		tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
		tomcat.setPort(0);

		Context context = tomcat.addContext("",
				System.getProperty("java.io.tmpdir"));

		ApplicationParameter applicationParameter = new ApplicationParameter();
		applicationParameter.setName("contextConfigLocation");
		applicationParameter.setValue(getSpringConfigLocation());
		context.addApplicationParameter(applicationParameter);

		context.addApplicationListener(ContextLoaderListener.class.getName());

		Tomcat.addServlet(context, "cxf", new CXFServlet());
		context.addServletMapping("/" + getRestServicesPath() + "/*", "cxf");

		tomcat.start();

		port = tomcat.getConnector().getLocalPort();

		System.out.println("Tomcat started on port:" + port);
	}

	@After
	public void stopTomcat() throws Exception {
		tomcat.stop();
	}

	protected String getRestServicesPath() {
		return "foo";
	}

	protected String getSpringConfigLocation() {
		return "classpath*:META-INF/spring-context.xml";
	}

	@Test
	public void testSayHello() {
		HelloService service = JAXRSClientFactory.create("http://localhost:"
				+ port + "/" + getRestServicesPath() + "/testServices/",
				HelloService.class);
		String who = "foo";
		assertTrue(service.sayHello(who).contains("Hello " + who));
	}
}
