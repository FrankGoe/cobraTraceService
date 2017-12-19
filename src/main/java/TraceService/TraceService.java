package TraceService;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TraceService
{
    private TraceFiles m_TraceFiles;

    private void ínitResponseHeader(HttpServletResponse p_Response)
    {
        p_Response.setHeader("Access-Control-Allow-Origin", "*");
        p_Response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        p_Response.setHeader("Access-Control-Max-Age", "3600");
        p_Response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }

    public TraceService()
    {
        m_TraceFiles = new TraceFiles();
    }

    @GetMapping("/traceFiles")
    public TraceFiles doTraceFilesRequest(HttpServletResponse p_Response)
    {
        ínitResponseHeader(p_Response);

        m_TraceFiles.loadFiles();
        return m_TraceFiles;
    }

    //value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml"
    @GetMapping("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id, HttpServletResponse p_Response)
    {
        ínitResponseHeader(p_Response);

        return m_TraceFiles.getTraceFile(p_Id);
    }

    //@PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    @PostMapping("/newTracePath/")
    public TraceFiles doNewTracePathRequest(@RequestBody String p_Data, HttpServletResponse p_Response)
    {
        ínitResponseHeader(p_Response);

        m_TraceFiles.setPath(p_Data);
        m_TraceFiles.loadFiles();

        return m_TraceFiles;
    }

    @PostMapping("/deleteTraceFiles/")
    public TraceFiles doNewTracePathRequest(@RequestBody Integer p_Data, HttpServletResponse p_Response)
    {
        ínitResponseHeader(p_Response);

        m_TraceFiles.deleteTraceFiles(p_Data);

        return m_TraceFiles;
    }
}