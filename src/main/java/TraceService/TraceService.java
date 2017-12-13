package TraceService;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class TraceService
{
    //private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/traceFiles")
    public TraceFiles doTraceFilesRequest()
    {
        //return l_TraceFiles.GetAsDelimitedString(",");
        return new TraceFiles();
    }

    //@RequestMapping (value = "/traceFile/{id}", method = RequestMethod.GET, produces="text/xml")
    @RequestMapping ("/traceFile/{id}")
    public String doTraceFileRequest(@PathVariable("id") String p_Id)
    {
        try
        {
            return new TraceFiles().getTraceFile(p_Id);
        }
        catch(IOException e)
        {
            return e.getMessage();
        }
    }
}