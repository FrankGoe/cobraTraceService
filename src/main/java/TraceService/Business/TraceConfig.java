package TraceService.Business;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@XmlRootElement
public class TraceConfig {

    public ArrayList<TraceUserConfig> userConfigs;

    public TraceConfig()
    {
        userConfigs = new ArrayList<TraceUserConfig>();
    }

    public TraceUserConfig userConfig(String p_UserId)
    {
        for (TraceUserConfig userConfig : userConfigs)
        {
            if (userConfig.UserId.equals(p_UserId))
                return userConfig;
        }

        TraceUserConfig userConfig = new TraceUserConfig(p_UserId);
        userConfigs.add(userConfig);

        return userConfig;
    }

    public void save()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File l_File = new File(classLoader.getResource(".").getFile() + "xml/TraceConfig.xml");
        saveXml(l_File);
    }

    public void load()  {

        ClassLoader classLoader = getClass().getClassLoader();
        File l_File = new File(classLoader.getResource(".").getFile() + "xml/TraceConfig.xml");

        try {
            if (l_File.createNewFile() || (l_File.length() == 0)) {
                saveXml(l_File);
            } else {
                readXml(l_File);
            }
        }

        catch (IOException x)
        {
            x.printStackTrace();
        }
    }

    private void readXml(File p_File )
    {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TraceConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            TraceConfig l_ReadedConfig = (TraceConfig) jaxbUnmarshaller.unmarshal(p_File);

            if (l_ReadedConfig.userConfigs == null)
                return;

            userConfigs.clear();

            for (TraceUserConfig l_ReadedUser : l_ReadedConfig.userConfigs)
            {
                TraceUserConfig l_User = userConfig(l_ReadedUser.UserId);

                l_User.setPath(l_ReadedUser.getPath());
                l_User.daysDeleteOffset = l_ReadedUser.daysDeleteOffset;
            }
        }

        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private void saveXml(File p_File)
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(TraceConfig.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(this, p_File);
        }

        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}



