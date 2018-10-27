package ir.hossein_shemshadi.component;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/ir/hossein_shemshadi/component/features",
        format = {"html:src/test/report/component/html"})
public class ComponentTest {
}
