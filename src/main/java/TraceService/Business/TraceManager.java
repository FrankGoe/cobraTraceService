package TraceService.Business;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TraceManager {
    public String userId;
    private ArrayList<TraceFile> traceFiles;
    private TraceConfig traceConfig;

    public TraceManager(TraceConfig traceConfig) {
        traceFiles = new ArrayList<TraceFile>();

        this.traceConfig = traceConfig;
        this.traceConfig.load();
    }

    public TraceUserConfig userConfig() {
        return traceConfig.userConfig(userId);
    }

    public void loadFiles() {
        traceFiles.clear();

        File[] l_Files = new File(traceConfig.userConfig(userId).getPath()).listFiles();

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

    public void deleteTraceFiles(Integer daysDeleteOffset) {
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
                    Files.deleteIfExists(Paths.get(traceConfig.userConfig("frank").getPath() + l_TraceFile.getName()));
                    traceFiles.remove(l_TraceFile);
                }
                catch (IOException x)
                {
                    System.err.println(x);
                }
            }
        }
    }

    public String getTraceFile(String fileName) {
        fileName += ".txt";

        Path l_Path = Paths.get(traceConfig.userConfig(userId).getPath() + fileName);
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

    public void setPath(String path) {
        traceConfig.userConfig(userId).setPath(path);
        traceConfig.save();
    }

    public String getPath() {
        return traceConfig.userConfig(userId).getPath();
    }

    public void setDaysDeleteOffset(int daysDeleteOffset) {
        traceConfig.userConfig(userId).daysDeleteOffset = daysDeleteOffset;
        traceConfig.save();
    }

    public int getdaysDeleteOffset() {
        return traceConfig.userConfig(userId).daysDeleteOffset;
    }

    public List<TraceFile> getTraceFiles() {
        return traceFiles.stream().sorted(Comparator.comparing(TraceFile::getLastModified)).collect(Collectors.toList());
    }

    public String GetAsDelimitedString(String p_Delimiter) {
        return traceFiles.stream().map(x -> traceConfig.userConfig(userId).getPath() + x).collect(Collectors.joining("\n"));
    }
}
