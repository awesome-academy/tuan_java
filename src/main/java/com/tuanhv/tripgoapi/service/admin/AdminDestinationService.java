package com.tuanhv.tripgoapi.service.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminDestinationForm;
import com.tuanhv.tripgoapi.dto.admin.AdminDestinationListItem;
import org.springframework.data.domain.Page;

public interface AdminDestinationService {

    Page<AdminDestinationListItem> search(String q, int page, int size);

    AdminDestinationForm getEditForm(Long id);

    void create(AdminDestinationForm form);

    void update(Long id, AdminDestinationForm form);

    void delete(Long id);
}
