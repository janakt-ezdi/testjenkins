package com.ezdi.cac.controller.hl7;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ezdi.cac.service.hl7.HL7Service;
import com.ezdi.cac.service.master.MasterService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-config.xml", "classpath:spring-config.xml" })
@WebAppConfiguration
public class HL7ControllerTest
{
	private MockMvc mockMvc;

	@Autowired
	private HL7Service hl7ServiceMock;

	@Autowired
	private MasterService masterServiceMock;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp()
	{
		Mockito.reset(hl7ServiceMock);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
}
