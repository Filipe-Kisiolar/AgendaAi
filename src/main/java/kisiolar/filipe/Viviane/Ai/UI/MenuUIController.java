package kisiolar.filipe.Viviane.Ai.UI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/menu")
public class MenuUIController {

    @GetMapping
    public String menu() {
        return "menu";
    }
}
