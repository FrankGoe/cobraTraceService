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

    public void LoadFiles()
    {
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


    public String getTraceFile(String p_FileName) throws IOException
    {
        p_FileName += ".txt";

        Path l_Path = Paths.get(m_Path + p_FileName);

        if (!Files.exists(l_Path))
            return "Datei " + p_FileName + " ist nicht vorhanden.";

         return new String(Files.readAllBytes(l_Path));
    }

    public String getPath()
    {
        return m_Path;
    }

    public List<TraceFile> getItems()
    {
        //return m_Items.stream().sorted(Comparator.comparing(TraceFile::getLastModifed).reversed()).collect(Collectors.toList());
        return m_Items.stream().sorted(Comparator.comparing(TraceFile::getLastModifed)).collect(Collectors.toList());
    }

    public String GetAsDelimitedString(String p_Delimiter)
    {
        return m_Items.stream().map(x -> m_Path + x).collect(Collectors.joining("\n"));
    }

    public TraceFiles()
    {
        m_Items = new ArrayList<TraceFile>();
        LoadFiles();
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

        public String getLastModifed()
        {
            Date l_LastModifed = new Date(m_LastModifed);
            Format l_Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return l_Formatter.format(l_LastModifed);
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
