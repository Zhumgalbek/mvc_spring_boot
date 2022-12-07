package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.Company;
import com.example.peaksoft.repository.CompanyRepository;
import com.example.peaksoft.service.CompanyService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository repository;

    public CompanyServiceImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return repository.findAll();
    }

    @Override
    public void addCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        repository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return repository.getById(id);
    }

    @Override
    public void updateCompany(Company company) throws IOException {
        validator(company.getCompanyName(), company.getLocatedCountry());
        repository.save(company);
    }

    @Override
    public void deleteCompany(Company company) {
        repository.delete(company);
    }

    private void validator(String companyName, String locatedCountry) throws IOException {
        if (companyName.length()>2 && locatedCountry.length()>2) {
            for (Character i : companyName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В названи компании нельзя вставлять цифры");
                }
            }
            for (Character i : locatedCountry.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В названии страны нельзя вставлять цифры");
                }
            }
        }else {
            throw new IOException("В название компании или страны должно быть как минимум 3 буквы");
        }
    }


}
