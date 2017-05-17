
package org.neogroup.sparks.views.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.neogroup.sparks.views.ViewException;
import org.neogroup.sparks.views.ViewFactory;
import org.neogroup.sparks.views.ViewNotFoundException;

import java.io.File;

/**
 * Veloity view factory
 */
public class VelocityViewFactory extends ViewFactory<VelocityView> {

    public static final String TEMPLATE_NAMESPACE_SEPARATOR = ".";

    private final VelocityEngine engine;

    /**
     * Constructor of the view factory
     * @param engine velocity engine
     */
    public VelocityViewFactory(VelocityEngine engine) {
        this.engine = engine;
    }

    /**
     * Constructor of the view factory
     */
    public VelocityViewFactory() {
        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
        engine.setProperty(RuntimeConstants.RUNTIME_LOG, "/dev/null");
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.setProperty("file.resource.loader.cache", "false");
        engine.setProperty("velocimacro.library.autoreload", "true");
        engine.setProperty(RuntimeConstants.VM_PERM_ALLOW_INLINE_REPLACE_GLOBAL, true);
    }

    /**
     * Set a property for the view factory
     * @param key name of property
     * @param value value of property
     */
    public void setProperty(String key, Object value) {
        engine.setProperty(key, value);
    }

    /**
     * Get a property from thte view factory
     * @param key name of property
     * @return value of property
     */
    public Object getProperty(String key) {
        return engine.getProperty(key);
    }

    /**
     * Creates a view
     * @param viewName name of the view
     * @return velocity view
     * @throws ViewException
     */
    @Override
    public VelocityView createView(String viewName) throws ViewException {
        try {
            String templateFilename = viewName.replace(TEMPLATE_NAMESPACE_SEPARATOR, File.separator) + ".vm";
            Template template = engine.getTemplate(templateFilename);
            return new VelocityView(template);
        }
        catch (ResourceNotFoundException ex) {
            throw new ViewNotFoundException(ex);
        }
        catch (Exception ex) {
            throw new ViewException(ex);
        }
    }
}
