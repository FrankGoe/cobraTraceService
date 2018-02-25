package TraceService.Controller;

import TraceService.Business.TraceUserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import TraceService.Business.TraceManager;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
public class TraceService
{
    @Autowired
    private TraceManager traceManager;

    @GetMapping("/traceFiles")
    public TraceManager doTraceFilesRequest(Principal principal) {
        traceManager.userId = principal.getName();
        traceManager.loadFiles();

        return traceManager;
    }

    //value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml"
    @GetMapping("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id, Principal principal) {
        traceManager.userId = principal.getName();

        return traceManager.getTraceFile(p_Id);
    }

    //@PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    //@CrossOrigin(origins = "*")
    @PostMapping("/newTracePath/")
    public TraceManager doNewTracePathRequest(@RequestBody String p_Data, HttpServletResponse response, Principal principal) {
        traceManager.userId = principal.getName();
        traceManager.setPath(p_Data);
        traceManager.loadFiles();

        return traceManager;
    }
  
    @PostMapping("/deleteTraceFiles/")
    public TraceManager doNewTracePathRequest(@RequestBody Integer p_Data, HttpServletResponse response, Principal principal) {
        traceManager.userId = principal.getName();
        traceManager.setDaysDeleteOffset(p_Data);
        traceManager.deleteTraceFiles(p_Data);

        return traceManager;
    }
}