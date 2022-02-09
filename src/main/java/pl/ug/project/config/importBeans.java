package pl.ug.project.config;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class importBeans {
    public importBeans() {
    }

    public List<String[]> getData(String file){
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();
            return allData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeDataFromCsvToXml(String file, String option) {

        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();

                Element rootElement = doc.createElement("beans");
                doc.appendChild(rootElement);

                Attr attr1 = doc.createAttribute("xmlns");
                attr1.setValue("http://www.springframework.org/schema/beans");
                rootElement.setAttributeNode(attr1);

                Attr attr2 = doc.createAttribute("xmlns:xsi");
                attr2.setValue("http://www.w3.org/2001/XMLSchema-instance");
                rootElement.setAttributeNode(attr2);

                Attr attr3 = doc.createAttribute("xsi:schemaLocation");
                attr3.setValue("http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd");
                rootElement.setAttributeNode(attr3);

                if(option.equals("com")){
                    for (String[] row : allData) {

                        Element bean = doc.createElement("bean");
                        rootElement.appendChild(bean);

                        Attr beanId = doc.createAttribute("id");
                        beanId.setValue(row[0]);
                        bean.setAttributeNode(beanId);

                        Attr beanClass = doc.createAttribute("class");
                        beanClass.setValue("pl.ug.project.domain.Comment");
                        bean.setAttributeNode(beanClass);


                        List<String> commentParams = List.of("id","username", "id_post", "comment_content");
                        commentParams.forEach(parameter -> {
                            Element constructor = doc.createElement("constructor-arg");
                            bean.appendChild(constructor);

                            Attr constructorName = doc.createAttribute("name");
                            constructorName.setValue(parameter);
                            constructor.setAttributeNode(constructorName);

                            Attr constructor1Value = doc.createAttribute("value");
                            constructor1Value.setValue(row[commentParams.indexOf(parameter)]);
                            constructor.setAttributeNode(constructor1Value);
                        });


                    }
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File("./src/main/resources/beansComments.xml"));
                    transformer.transform(source, result);
                }
                if(option.equals("post")){
                    for (String[] row : allData) {

                        Element bean = doc.createElement("bean");
                        rootElement.appendChild(bean);

                        Attr beanId = doc.createAttribute("id");
                        beanId.setValue(row[0] + 100000);
                        bean.setAttributeNode(beanId);

                        Attr beanClass = doc.createAttribute("class");
                        beanClass.setValue("pl.ug.project.domain.Post");
                        bean.setAttributeNode(beanClass);


                        List<String> postParams = List.of("id","authors", "content", "tags");
                        postParams.forEach(parameter -> {
                            Element constructor = doc.createElement("constructor-arg");
                            bean.appendChild(constructor);

                            Attr constructorName = doc.createAttribute("name");
                            constructorName.setValue(parameter);
                            constructor.setAttributeNode(constructorName);

                            if(parameter.equals("tags")){
                                Attr constructor1Value = doc.createAttribute("value");
                                constructor1Value.setValue(row[postParams.indexOf(parameter)]);
                                constructor.setAttributeNode(constructor1Value);
                            }
                            else{
                                Attr constructor1Value = doc.createAttribute("value");
                                constructor1Value.setValue(row[postParams.indexOf(parameter)]);
                                constructor.setAttributeNode(constructor1Value);
                            }
                        });


                    }
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File("./src/main/resources/beansPosts.xml"));
                    transformer.transform(source, result);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
