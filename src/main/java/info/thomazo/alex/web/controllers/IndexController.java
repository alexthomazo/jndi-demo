package info.thomazo.alex.web.controllers;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serve main page
 * 
 * @author Alexandre THOMAZO
 */
@Controller
public class IndexController {

	/** Spring JdbcTemplate for helping requesting database */
	private JdbcTemplate jdbcTemplate;
	
	/** Environment name retrieve by JNDI */
	private String envName;
	
	/**
	 * Display index page
	 * @param model Model to fill for the view
	 * @return View name to display
	 */
	@RequestMapping("/")
	public String index(ModelMap model) {
		model.put("envName", envName);
		
		//getting database version
		String version = jdbcTemplate.queryForObject("select version()", String.class);
		model.put("version", version);
		
		return "index";
	}
	
	/**
	 * Set the datasource in order to access the database
	 * and create the Spring JdbcTemplate
	 * @param dataSource Datasource to use
	 */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public void setEnvName(String envName) {
		this.envName = envName;
	}
}
