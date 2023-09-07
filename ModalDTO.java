package com.adobe.aem.guides.wknd.core.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Getter
@Setter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ModalDTO {

    @Inject
    private String modalId;

    @Inject
    private String modalTitle;

    @Inject
    private String modalDescription;

    @Inject
    private String modalCtatext;

}
