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

    public String userId;
    private ArrayList<TraceFile> traceFiles;
    private TraceConfig configs;

    public TraceManager()
    {
        configs = new TraceConfig();
        configs.load();

        traceFiles = new ArrayList<TraceFile>();
    }

    public TraceUserConfig userConfig()
    {
        return configs.userConfig(userId);
    }

    public void loadFiles()
    {
        traceFiles.clear();

        File[] l_Files = new File(configs.userConfig(userId).getPath()).listFiles();

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

    public String getTraceFile(String fileName)
    {
        fileName += ".txt";

        Path l_Path = Paths.get(configs.userConfig(userId).getPath() + fileName);
        if (!Files.exists(l_Path))
            return "Datei " + fileName + " ist nicht vorhanden.";

        try
        {
            return new String(Files.readAllBytes(l_Path));
        }
        catch(IOException e)
        {
            return e.getMessage();
        }
    }

    public void setPath(String path)
    {
        configs.userConfig(userId).setPath(path);
        configs.save();
    }

    public String getPath()
    {
        return configs.userConfig(userId).getPath();
    }

    public void setDaysDeleteOffset(int daysDeleteOffset)
    {
        configs.userConfig(userId).daysDeleteOffset = daysDeleteOffset;
        configs.save();
    }

    public int getdaysDeleteOffset()
    {
        return configs.userConfig(userId).daysDeleteOffset;
    }

    public List<TraceFile> getTraceFiles()
    {
        return traceFiles.stream().sorted(Comparator.comparing(TraceFile::getLastModified)).collect(Collectors.toList());
    }

    public String GetAsDelimitedString(String p_Delimiter)
    {
        return traceFiles.stream().map(x -> configs.userConfig(userId).getPath() + x).collect(Collectors.joining("\n"));
    }
}
