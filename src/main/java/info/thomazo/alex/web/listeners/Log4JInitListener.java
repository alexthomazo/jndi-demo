package info.thomazo.alex.web.listeners;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Load log4j.xml based on JNDI log4jConf resource.
 * 
 * @author Alexandre THOMAZO
 */
public class Log4JInitListener implements ServletContextListener {

	/** JNDI variable name for log4j.xml config file */
	public static final String JNDI_LOG4J_URL = "java:/comp/env/log4jConf";
	
	/** Logger */
	private static Logger logger = Logger.getLogger(Log4JInitListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String path = null;
		try {
			//getting JNDI value
			Context ctx = new InitialContext();
			path = (String) ctx.lookup(JNDI_LOG4J_URL);
			
			if (path == null) {
				logger.error("JNDI var [" + JNDI_LOG4J_URL + "] is null");
				return;
			}
						
			//checking if local file exists
			if (!new File(path).exists()) {
				logger.info("Log config file [" + path + "] does not exists");
				return;
			}
			
			//as Log4J do not provide a way to validate a config file
			//before applying or either to check if the load is sucessful
			//we do a reset, load the file and cross fingers
			BasicConfigurator.resetConfiguration();
			DOMConfigurator.configureAndWatch(path);  
			logger.info("Loaded log config from file: [" + path + "]");  
			
		} catch (NamingException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't find JNDI var [" + JNDI_LOG4J_URL + "]", e);
			} else {
				logger.info("Can't find JNDI var [" + JNDI_LOG4J_URL + "]: " + e.getMessage());
			}
			logger.info("Using embed log4j configuration");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
