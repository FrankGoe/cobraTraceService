package TraceService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import TraceService.Business.TraceManager;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TraceService
{
    private TraceManager traceManager;

    public TraceService()
    {
        traceManager = new TraceManager();
        traceManager.configs.load();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/traceFiles")
    public TraceManager doTraceFilesRequest(HttpServletResponse p_Response)
    {
        traceManager.loadFiles();
        return traceManager;
    }

    //value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml"
    @CrossOrigin(origins = "*")
    @GetMapping("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id, HttpServletResponse p_Response)
    {
        return traceManager.getTraceFile(p_Id);
    }

    //@PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = "*")    
    @PostMapping("/newTracePath/")
    public TraceManager doNewTracePathRequest(@RequestBody String p_Data, HttpServletResponse p_Response)
    {
        traceManager.userConfig("frank").setPath(p_Data);
        traceManager.configs.save();

        traceManager.loadFiles();

        return traceManager;
    }
  
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteTraceFiles/")
    public TraceManager doNewTracePathRequest(@RequestBody Integer p_Data, HttpServletResponse p_Response)
    {
        traceManager.userConfig("frank").daysDeleteOffset = p_Data;
        traceManager.configs.save();

        traceManager.deleteTraceFiles(p_Data);

        return traceManager;
    }
}