package TraceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TraceFiles
{
    private ArrayList<TraceFile> m_Items;
    private String m_Path = "C:\\DevelopWeb\\cobraTraceAnalyze\\examples\\";

    public void loadFiles()
    {
        m_Items.clear();

        File[] l_Files = new File(m_Path).listFiles();

        if (l_Files == null)
            return;

        for (File l_File : l_Files)
        {
            if (l_File.isFile())
            {
                TraceFile l_TraceFile = new TraceFile(l_File.getName(), l_File.lastModified(), l_File.length());

                m_Items.add(l_TraceFile);
            }
        }
    }

    public void deleteTraceFiles(Integer daysDeleteOffset)
    {
        loadFiles();

        Date l_Today = new Date();
        Calendar l_Calendar = Calendar.getInstance();
        l_Calendar.setTime(l_Today);
        l_Calendar.add(Calendar.DAY_OF_MONTH, daysDeleteOffset * -1);
        Date l_DeleteDate = l_Calendar.getTime();


        for (int i = m_Items.size() - 1; i >= 0; i--)
        {
            TraceFile l_TraceFile = m_Items.get(i);

            if (l_TraceFile.LastModifiedDate().before(l_DeleteDate))
            {
                try
                {
                    Files.deleteIfExists(Paths.get(m_Path + l_TraceFile.getName()));
                    m_Items.remove(l_TraceFile);
                }
                catch (IOException x)
                {
                    System.err.println(x);
                }
            }
        }

    }

    public String getTraceFile(String p_FileName)
    {
        p_FileName += ".txt";

        Path l_Path = Paths.get(m_Path + p_FileName);
        if (!Files.exists(l_Path))
            return "Datei " + p_FileName + " ist nicht vorhanden.";

        try
        {
            return new String(Files.readAllBytes(l_Path));
        }
        catch(IOException e)
        {
            return e.getMessage();
        }
    }

    public String getPath()
    {
        return m_Path;
    }

    public void setPath(String p_Path)
    {
        if (!p_Path.endsWith("\\"))
            p_Path = p_Path + "\\";

        m_Path = p_Path;
    }

    public List<TraceFile> getItems()
    {
        return m_Items.stream().sorted(Comparator.comparing(TraceFile::getLastModified).reversed()).collect(Collectors.toList());
        //return m_Items.stream().sorted(Comparator.comparing(TraceFile::getLastModified)).collect(Collectors.toList());
    }

    public String GetAsDelimitedString(String p_Delimiter)
    {
        return m_Items.stream().map(x -> m_Path + x).collect(Collectors.joining("\n"));
    }

    public TraceFiles()
    {
        m_Items = new ArrayList<TraceFile>();
    }

    private class TraceFile {

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
            Format l_Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
}
