package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.rule.AccidentRuleService;
import ru.job4j.accidents.service.type.AccidentTypeService;

import java.util.HashSet;
import java.util.List;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final AccidentRuleService accidentRuleService;

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("rules", accidentRuleService.findAll());
        model.addAttribute("accident", new Accident());
        return "createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident, @RequestParam("typeId") int typeId, @RequestParam("ruleIds") List<Integer> ruleIds) {
        accident.setType(accidentTypeService.findById(typeId).orElse(null));
        accident.setRules(new HashSet<>(accidentRuleService.findByIds(ruleIds)));
        accidentService.save(accident);
        return "redirect:/index";
    }

    @GetMapping("/editAccident/{id}")
    public String viewEditAccident(Model model, @PathVariable int id) {
        var accidentOptional = accidentService.findById(id);
        if (accidentOptional.isEmpty()) {
            model.addAttribute("message", "The accident with such \"id\" is not found");
            return "templates/errors/404";
        }
        model.addAttribute("accident", accidentOptional.get());
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("rules", accidentRuleService.findAll());
        return "editAccident";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident, @RequestParam("typeId") int typeId, @RequestParam("ruleIds") List<Integer> ruleIds) {
        accident.setType(accidentTypeService.findById(typeId).orElse(null));
        accident.setRules(new HashSet<>(accidentRuleService.findByIds(ruleIds)));
        accidentService.update(accident);
        return "redirect:/index";
    }
}
