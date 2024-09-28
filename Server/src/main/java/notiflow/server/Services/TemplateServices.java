package notiflow.server.Services;

import notiflow.server.Entities.TemplateEntity;
import notiflow.server.Repository.TemplateRepository;
import notiflow.server.Requests.TemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateServices {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServices(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public TemplateEntity saveTemplateEntity(TemplateRequest templateRequest) {
        if (templateRequest == null) {
            return null;
        }
        TemplateEntity templateEntity = new TemplateEntity();
        templateEntity.setCoverImageUrl(templateRequest.getCoverImageUrl());
        templateEntity.setCompanyLogoUrl(templateRequest.getCompanyLogoUrl());

        return templateRepository.save(templateEntity);
    }
}
