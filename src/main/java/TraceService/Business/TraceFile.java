package TraceService.Business;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TraceFile {

    private String m_Name;
    private Long m_LastModifed;
    private Long m_Size;

    public TraceFile(String p_Name, Long p_LastModified, Long p_Size )
    {
        m_Name = p_Name;
        m_LastModifed = p_LastModified;
        m_Size = p_Size;
    }

    public String getLastModified()
    {
        Date l_LastModifed = new Date(m_LastModifed);
        Format l_Formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return l_Formatter.format(l_LastModifed);
    }

    public Date LastModifiedDate()
    {
        return new Date(m_LastModifed);
    }

    public String getTotalSpaceMb()
    {
        Double l_Size = m_Size / 1024.0 / 1000.0;
        return String.valueOf( String.format(Locale.ROOT, "%.4f", l_Size ) ) + " MB";
    }

    public String getName()
    {
        return m_Name;
    }
}
