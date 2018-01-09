package TraceService.Controller;

import TraceService.Business.TraceFiles;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TraceService
{
    private TraceFiles m_TraceFiles;

    public TraceService()
    {
        m_TraceFiles = new TraceFiles();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/traceFiles")
    public TraceFiles doTraceFilesRequest(HttpServletResponse p_Response)
    {
        m_TraceFiles.loadFiles();
        return m_TraceFiles;
    }

    //value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml"
    @CrossOrigin(origins = "*")
    @GetMapping("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id, HttpServletResponse p_Response)
    {
        return m_TraceFiles.getTraceFile(p_Id);
    }

    //@PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = "*")    
    @PostMapping("/newTracePath/")
    public TraceFiles doNewTracePathRequest(@RequestBody String p_Data, HttpServletResponse p_Response)
    {
        m_TraceFiles.setPath(p_Data);
        m_TraceFiles.loadFiles();

        return m_TraceFiles;
    }
  
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteTraceFiles/")
    public TraceFiles doNewTracePathRequest(@RequestBody Integer p_Data, HttpServletResponse p_Response)
    {
        m_TraceFiles.deleteTraceFiles(p_Data);

        return m_TraceFiles;
    }
}