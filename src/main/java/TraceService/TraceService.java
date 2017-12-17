package TraceService;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;

@RestController
public class TraceService
{
    private TraceFiles m_TraceFiles;

    public TraceService()
    {
        m_TraceFiles = new TraceFiles();
    }

    @GetMapping("/traceFiles")
    public TraceFiles doTraceFilesRequest()
    {
        m_TraceFiles.loadFiles();
        return m_TraceFiles;
    }

    //value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml"
    @GetMapping("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id)
    {
        return m_TraceFiles.getTraceFile(p_Id);
    }

    //@PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    @PostMapping("/newTracePath/")
    public TraceFiles doNewTracePathRequest(@RequestBody String p_Data)
    {
        m_TraceFiles.setPath(p_Data);
        m_TraceFiles.loadFiles();

        return m_TraceFiles;
    }

    @PostMapping("/deleteTraceFiles/")
    public TraceFiles doNewTracePathRequest(@RequestBody Integer p_Data)
    {
        m_TraceFiles.deleteTraceFiles(p_Data);

        return m_TraceFiles;
    }
}