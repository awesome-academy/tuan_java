package com.tuanhv.tripgoapi.service.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminTourForm;
import com.tuanhv.tripgoapi.dto.admin.AdminTourFormOptions;
import com.tuanhv.tripgoapi.entity.Tour;
import org.springframework.data.domain.Page;

public interface AdminTourService {

    Page<Tour> search(String q, int page, int size);

    AdminTourFormOptions getFormOptions();

    Tour create(AdminTourForm form);

    AdminTourForm getEditForm(Long id);

    Tour update(Long id, AdminTourForm form);

    void delete(Long id);
}
