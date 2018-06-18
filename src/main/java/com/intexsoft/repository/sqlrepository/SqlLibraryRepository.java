package com.intexsoft.repository.sqlrepository;

import com.intexsoft.model.Library;
import com.intexsoft.repository.LibraryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@ConditionalOnProperty(name = "datasource.name", havingValue = "dbSql", matchIfMissing = false)
public class SqlLibraryRepository extends SqlCommonRepository<Library, UUID> implements LibraryRepository {

    @Override
    protected String sqlGetById() {
        return "SELECT library.id AS library_id, library.name AS library_name, library.address, library.version AS library_version FROM library WHERE library.id = :id";
    }

    @Override
    protected String sqlSave() {
        return "INSERT INTO library (id, name, address, version)VALUES (:id, :name, :address, :version)";
    }

    @Override
    protected String sqlSearch() {
        return "SELECT library.id AS library_id, library.name AS library_name, library.address, library.version AS library_version FROM library WHERE name = COALESCE(:name,name) and address = COALESCE(:address,address)";
    }

    @Override
    protected String sqlUpdate() {
        return "UPDATE library SET id = :id, name = :name, address = :address, version = :version WHERE id = :id";
    }

    @Override
    protected String sqlDelete() {
        return "DELETE FROM library WHERE id = :id";
    }

    @Override
    protected MapSqlParameterSource getCommonParametersSource(Library library) {
        return super.getCommonParametersSource(library)
                .addValue("name", library.getName())
                .addValue("address", library.getAddress());
    }

    @Override
    public List<Library> searchLibrary(String name, String address) {
        Map<String, Object> searchCriterias = new HashMap<>();
        searchCriterias.put("name", name);
        searchCriterias.put("address", address);
        return search(searchCriterias);
    }
}
