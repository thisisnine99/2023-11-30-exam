package com.korea.test.notebook;


import com.korea.test.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {
}
