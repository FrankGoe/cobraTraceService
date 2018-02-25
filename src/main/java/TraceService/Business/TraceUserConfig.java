package TraceService.Business;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TraceUserConfig {

    private String path = "C:\\DevelopWeb\\cobraTraceAnalyze\\examples\\";

    public TraceUserConfig() {
    }

    public TraceUserConfig(String p_UserId)
    {
        UserId = p_UserId;
    }

    public int daysDeleteOffset = 8;

    public String UserId;

    public String getPath() { return path; }
    public void setPath(String p_Path)
    {
        if (!p_Path.endsWith("\\"))
            p_Path = p_Path + "\\";

        path = p_Path;
    }
}
