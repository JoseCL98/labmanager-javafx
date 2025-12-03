package com.myorg.labmanager.infrastructure.adapter;

import com.myorg.labmanager.domain.model.Equipo;
import java.io.IOException;
import java.util.List;

public interface DataImporter {
    List<Equipo> importarEquipos(String source) throws IOException;
}
