package ir.hossein_shemshadi.clients;

import ir.hossein_shemshadi.objects.Property;
import ir.hossein_shemshadi.objects.Zestimate;
import jdk.internal.org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ZillowClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZillowClient.class);

    @Value("${zillow.api.deep-search.url}")
    private String zillowDeepSearchAPIUrl;

    @Value("${zillow.zws-id}")
    private String zillowWebserviceId;

    private static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("UTF-8 encoding is not supported by the host!!");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    private Zestimate parse(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException, org.xml.sax.SAXException, ParseException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);

        Zestimate zestimate = new Zestimate();
        if (Integer.parseInt(doc.getElementsByTagName("message").item(0).getChildNodes().item(1).getTextContent()) == 508){
            LOGGER.info("There was no zestimate found!!!");
            return zestimate;
        }
        zestimate.setAmount(Long.parseLong(doc.getElementsByTagName("zestimate").item(0)
                .getChildNodes().item(0).getTextContent()));
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date lastUpdated = df.parse(doc.getElementsByTagName("zestimate").item(0).getChildNodes().item(1)
                .getTextContent());
        zestimate.setLastUpdated(lastUpdated);
        zestimate.setValueChange(Long.parseLong(doc.getElementsByTagName("zestimate").item(0)
                .getChildNodes().item(3).getTextContent()));
        zestimate.setLowValuationRange(Long.parseLong(doc.getElementsByTagName("zestimate").item(0)
                .getChildNodes().item(4).getChildNodes().item(0).getTextContent()));
        zestimate.setHighValuationRange(Long.parseLong(doc.getElementsByTagName("zestimate").item(0)
                .getChildNodes().item(4).getChildNodes().item(1).getTextContent()));
        zestimate.setPercentile(Float.parseFloat(doc.getElementsByTagName("zestimate").item(0)
                .getChildNodes().item(5).getTextContent()));
        return zestimate;
    }

    private HttpURLConnection sendGetRequest(String url, String parametersString) {
        HttpURLConnection connection = null;
        try {
            //initiation
            URL obj = new URL(url + "?" + parametersString);
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            //configuration
//            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
        } catch (MalformedURLException e) {
            System.out.println("There was a problem calling the URL " + url + " with parameters: " + parametersString);
        } finally {
            return connection;
        }
    }

    private String extractResponse(HttpURLConnection connection) {
        StringBuffer response = new StringBuffer();
        try {
//            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            LOGGER.error("There was a problem extracting response");
        }
        return response.toString();
    }

    public Zestimate getZestimateFromZillow(Property property) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("zws-id", zillowWebserviceId);
        parameters.put("address", property.getAddress());
        parameters.put("citystatezip", property.getCity() + ", " + property.getState());
        String parametersString = getParamsString(parameters);
        HttpURLConnection connection = sendGetRequest(zillowDeepSearchAPIUrl, parametersString);
        String responseXML = extractResponse(connection);
        try {
            return parse(new DataInputStream(new ByteArrayInputStream(responseXML.getBytes())));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
