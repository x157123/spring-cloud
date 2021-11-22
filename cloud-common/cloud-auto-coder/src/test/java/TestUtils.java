import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {


    @Test
    public void create() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setCacheStorage(new freemarker.cache.NullCacheStorage());
        StringTemplateLoader stl = new StringTemplateLoader();
        stl.putTemplate("myTemplate", "${obj.name?cap_first} ${user?cap_first} ${name?uncap_first}  World!");
        cfg.setTemplateLoader(stl);
        Template template = cfg.getTemplate("myTemplate","utf-8");

        Map<String,Object> root = new HashMap<String,Object>();
        root.put("user", "liulei");
        root.put("name", "Liulei");

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println(writer.toString());
    }
}
