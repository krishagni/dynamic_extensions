package edu.common.dynamicextensions.query;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.DigesterLoader;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;

public class PathConfig {   
    private static DigesterLoader loader = DigesterLoader.newLoader(new PathConfigRulesModule()); 

    private static PathConfig instance;
    
    private Map<String, Path> paths;
	
    public static void intialize(String pathConfig) {
        instance = new PathConfig(pathConfig);
    }

    public static void initialize(InputStream in) {
        instance = new PathConfig(in);
    }

    public static PathConfig getInstance() {
        return instance;
    }

    public static PathConfig from(List<Path> paths) {
		PathConfig config = new PathConfig();
		config.paths = new HashMap<>();

		for (Path path : paths) {
			config.paths.put(config.getPathStr(path.getStartForm(), path.getEndForm()), path);
		}

		return config;
	}

	private PathConfig() {

	}

    private PathConfig(String pathConfig) {
        FileInputStream fin = null;
        
        try {
            fin = new FileInputStream(pathConfig);
            init(fin);
        } catch(Exception e) {
            throw new FormException("Failed to initialize path configuration", e);
        } finally {
            IoUtil.close(fin);
        }
    }

    private PathConfig(InputStream in) {
        init(in);
    }

    private void init(InputStream in) {
        try {
        	Digester digester = loader.newDigester();
            List<Path> paths = (List<Path>)digester.parse(in);

            this.paths = new HashMap<String, Path>();
            for (Path path : paths) {
            	if (path.getEndForm() == null) {
            		path.setWildCard(true);
            	}
            	this.paths.put(getPathStr(path.getStartForm(), path.getEndForm()), path);
            }
        } catch(Exception e) {
            throw new FormException("Failed to initialize path configuration", e);
        }
    }

    public Path getPath(String start, String end) {
        String pathStr = getPathStr(start, end);
        Path result = paths.get(pathStr);
        
        if(result == null && end != null) {
            pathStr = getPathStr(start, null);
            result = paths.get(pathStr);
        }
        
        return result;
    }

    private String getPathStr(String start, String end) {
        StringBuilder pathStr = new StringBuilder();
        pathStr.append(start).append(":");
        pathStr.append(end != null ? ((Object) (end)) : "*");
        return pathStr.toString();
    }
    
	public static class PathConfigRulesModule extends AbstractRulesModule {

        protected void configure()
        {
            forPattern("paths")
            	.createObject().ofType(ArrayList.class);
            
            forPattern("paths/path")
            	.createObject().ofType(Path.class)
            	.then().setNext("add");
            
            forPattern("paths/path/from")
            	.setBeanProperty().withName("startForm");
            forPattern("paths/path/to")
            	.setBeanProperty().withName("endForm");
            forPattern("paths/path/start-field")
            	.setBeanProperty().withName("startField");
            
            forPattern("paths/path/links")
            	.createObject().ofType(ArrayList.class)
            	.then().setNext("setLinks");
            
            forPattern("paths/path/links/link")
            	.createObject().ofType(PathLink.class)
            	.then().setNext("add");
            
            forPattern("paths/path/links/link/key")
            	.setBeanProperty().withName("key");
            forPattern("paths/path/links/link/ref-tab")
            	.setBeanProperty().withName("refTab");
            forPattern("paths/path/links/link/ref-tab-key")
            	.setBeanProperty().withName("refTabKey");
        }
    }
}