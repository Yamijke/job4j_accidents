package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        List<AccidentType> types = accidentService.getAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = accidentService.getAllRules();
        model.addAttribute("rules", rules);
        return "createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        String[] ids = req.getParameterValues("rIds");
        if (ids != null) {
            Set<Rule> rules = new HashSet<>();
            for (String id : ids) {
                rules.add(new Rule(Integer.parseInt(id), ""));
            }
            accident.setRules(rules);
        } else {
            accident.setRules(new HashSet<>()); // Инициализация пустым набором правил
        }
        accidentService.save(accident);
        return "redirect:/index";
    }

    @GetMapping("/{id}")
    public String viewEditAccident(Model model, @PathVariable int id) {
        var accidentOptional = accidentService.findById(id);
        if (accidentOptional.isEmpty()) {
            model.addAttribute("message", "The accident with such \"id\" is not found");
            return "templates/errors/404";
        }
        model.addAttribute("accident", accidentOptional.get());
        List<AccidentType> types = accidentService.getAllAccidentTypes();
        model.addAttribute("types", types);
        List<Rule> rules = accidentService.getAllRules();
        model.addAttribute("rules", rules);
        return "editAccident";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident, HttpServletRequest req, Model model) {
        String[] ids = req.getParameterValues("rIds");
        if (ids != null) {
            Set<Rule> rules = new HashSet<>();
            for (String id : ids) {
                rules.add(new Rule(Integer.parseInt(id), ""));
            }
            accident.setRules(rules);
        } else {
            accident.setRules(new HashSet<>());
        }
        String typeId = req.getParameter("type.id");
        if (typeId != null && !typeId.isEmpty()) {
            AccidentType type = new AccidentType();
            type.setId(Integer.parseInt(typeId));
            accident.setType(type);
        }
        var isUpdated = accidentService.update(accident);
        if (!isUpdated) {
            model.addAttribute("message", "Accident with the specified identifier not found");
            return "templates/errors/404";
        }
        return "redirect:/index";
    }
}
