package TraceService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TraceNavigation
{
    // redirect
    /*
    @GetMapping("/cobraTraceAnalyzer")
    public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes)
    {
        attributes.addAttribute("attribute", "cobraTraceAnalyzer");
        return new RedirectView("/index.html");
    }
    */

    // forward
    @GetMapping("/cobraTraceAnalyzer")
    public ModelAndView redirectWithUsingForwardPrefix(ModelMap model)
    {
        return new ModelAndView("forward:/index.html", model);
    }

}
