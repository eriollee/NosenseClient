package sample.properties;

import java.io.IOException;
import java.io.InputStream;

public class PropUtils {

    public static LinkedProperties loadLinkedProperties(String proName) throws IOException{
        LinkedProperties prop = new LinkedProperties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(proName);
        prop.load(inputStream);
        return prop;
    }


}

