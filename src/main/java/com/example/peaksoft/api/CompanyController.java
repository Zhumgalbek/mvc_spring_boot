package com.example.peaksoft.api;
import com.example.peaksoft.model.Company;
import com.example.peaksoft.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class CompanyController {
    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("/getAllCompanies")
    public String getCompanies(Model model) {
        List<Company> companies = service.getAllCompanies();
        model.addAttribute("companies", companies);
        return "/company/allCompanies";
    }

    @GetMapping("/addCompany")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        return "/company/add_company";
    }

    @PostMapping("/saveCompany")
    public String saveCompany(@ModelAttribute("company") Company company) throws IOException {
        service.addCompany(company);
        return "redirect:/getAllCompanies";
    }

    @GetMapping("/updateCompany")
    public String updateCompany(@RequestParam("companyId") Long id, Model model) {
        Company company = service.getCompanyById(id);
        model.addAttribute("company", company);
        model.addAttribute("companyId", id);
        return "/company/updateCompany";
    }

    @PostMapping("/saveUpdateCompany")
    public String saveUpdateCompany(@ModelAttribute("company") Company company,
                                    @RequestParam("id") Long id) throws IOException {
        service.updateCompany(company);
        return "redirect:/getAllCompanies";
    }

    @RequestMapping("/deleteCompany")
    public String deleteCompany(@RequestParam("companyId") Long id) {
        service.deleteCompany(service.getCompanyById(id));
        return "redirect:/getAllCompanies";
    }
}
