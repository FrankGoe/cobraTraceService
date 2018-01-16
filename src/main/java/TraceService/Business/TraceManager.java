package TraceService.Business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TraceManager {

    public ArrayList<TraceFile> traceFiles;
    public TraceConfig configs;

    public TraceManager()
    {
        configs = new TraceConfig();
        traceFiles = new ArrayList<TraceFile>();
    }

    public TraceUserConfig userConfig(String userId)
    {
        return configs.userConfig(userId);
    }

    public void loadFiles()
    {
        traceFiles.clear();

        File[] l_Files = new File(configs.userConfig("frank").getPath()).listFiles();

        if (l_Files == null)
            return;

        for (File l_File : l_Files)
        {
            if (l_File.isFile())
            {
                TraceFile l_TraceFile = new TraceFile(l_File.getName(), l_File.lastModified(), l_File.length());

                traceFiles.add(l_TraceFile);
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


        for (int i = traceFiles.size() - 1; i >= 0; i--)
        {
            TraceFile l_TraceFile = traceFiles.get(i);

            if (l_TraceFile.LastModifiedDate().before(l_DeleteDate))
            {
                try
                {
                    Files.deleteIfExists(Paths.get(configs.userConfig("frank").getPath() + l_TraceFile.getName()));
                    traceFiles.remove(l_TraceFile);
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

        Path l_Path = Paths.get(configs.userConfig("frank").getPath() + p_FileName);
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
        return configs.userConfig("frank").getPath();
    }

    public int getdaysDeleteOffset()
    {
        return configs.userConfig("frank").daysDeleteOffset;
    }


    public List<TraceFile> getItems()
    {
        return traceFiles.stream().sorted(Comparator.comparing(TraceFile::getLastModified).reversed()).collect(Collectors.toList());
        //return m_Items.stream().sorted(Comparator.comparing(TraceFile::getLastModified)).collect(Collectors.toList());
    }

    public String GetAsDelimitedString(String p_Delimiter)
    {
        return traceFiles.stream().map(x -> configs.userConfig("frank").getPath() + x).collect(Collectors.joining("\n"));
    }
}
