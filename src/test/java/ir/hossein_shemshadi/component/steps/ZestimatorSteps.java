package ir.hossein_shemshadi.component.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ir.hossein_shemshadi.ZestimatorApplication;
import ir.hossein_shemshadi.clients.ZillowClient;
import ir.hossein_shemshadi.objects.Property;
import ir.hossein_shemshadi.objects.Zestimate;
import ir.hossein_shemshadi.spreadsheet.SpreadsheetWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = ZestimatorApplication.class)
@SpringBootTest(classes = ZestimatorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZestimatorSteps {
    private static int id = 1;

    @LocalServerPort
    int port;

    Property property = new Property();

    @Autowired
    private SpreadsheetWriter spreadsheetWriter;
    @Autowired
    private ZillowClient zillowClient;

    @Given("^we have a property in (.*) city$")
    public void systemIsNewlyStartedAndThereIsNoDataInDatabases(String city) {
        property.setCity(city);
    }

    @And("^it's located in (.*) state$")
    public void itSLocatedInStateState(String state) {
        property.setState(state);
    }

    @And("^it's zip-code is (.*)$")
    public void itSZipCodeIsZip_code(String zipCode) {
        property.setZipCode(zipCode);
    }

    @And("^it's full address is (.*)$")
    public void itSFullAddressIsAddress(String address) {
        property.setAddress(address);
    }

    @When("^we put this property in our zestimator application$")
    public void wePutThisPropertyInOurZestimatorApplication() throws Throwable {
        property.setPropertyId(id);
        Zestimate zestimate = zillowClient.getZestimateFromZillow(property);
        property.setZestimate(zestimate);
        spreadsheetWriter.persist(property);
        id++;
    }

    @Then("^we can find the zestimate in the resulted excel file$")
    public void weCanFindTheZestimateInTheResultedExcelFile() throws Throwable {
    }
}