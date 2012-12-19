package com.socialone.utils.synonyms.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.socialone.utils.synonyms.SynonymWordDataRepository;

public class JdbcSynonymWordDataRepository implements SynonymWordDataRepository {

    final private String GET_SYNONYMS_SEQUENCE_QUERY = "SELECT SEQUENCE FROM UTILS_SYNONYMS WHERE WORD = ?";
    final private String GET_ALL_SYNONYMS_QUERY = "SELECT WORD FROM UTILS_SYNONYMS WHERE SEQUENCE = ?";
    final private String ADD_SEQUENCE = "INSERT INTO UTILS_SYNONYMS(WORD, SEQUENCE) VALUES(?, ?)";
    final private String UPDATE_SEQUENCE = "UPDATE UTILS_SYNONYMS SET SEQUENCE = ? WHERE SEQUENCE = ?";

    final private JdbcTemplate jdbcTemplate;

    final private RowMapper<String> stringMapper = new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    };

    @Inject
    public JdbcSynonymWordDataRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(checkNotNull(dataSource));
    }

    @Override
    public Collection<String> getSynonyms(String word) {
        // Step 1. Query for sequence names
        List<String> sequenceNames = jdbcTemplate.query(GET_SYNONYMS_SEQUENCE_QUERY, stringMapper, word);
        // Step 2. Check returned result
        if (sequenceNames.size() == 0)
            return Collections.emptyList();
        // Step 3. Retrieve all values of the sequence
        String sequence = sequenceNames.get(0);
        return jdbcTemplate.query(GET_ALL_SYNONYMS_QUERY, stringMapper, sequence);
    }

    @Override
    public void addSynonyms(String word, String synonym) {
        // Attempt to add twice
        try {
            tryAddSynonyms(word, synonym);
        } catch (DuplicateKeyException duplicateKeyException) {
            tryAddSynonyms(word, synonym);
        }
    }

    private void tryAddSynonyms(String word, String synonym) {
        // Step 1. Query for sequence names
        List<String> wordSequence = jdbcTemplate.query(GET_SYNONYMS_SEQUENCE_QUERY, stringMapper, word);
        List<String> synonymSequence = jdbcTemplate.query(GET_SYNONYMS_SEQUENCE_QUERY, stringMapper, synonym);
        // Step 2. Making appropriate updates
        if (wordSequence.size() == 0) {
            if (synonymSequence.size() == 0) {
                // Create new records for this synonyms
                String randomSequence = UUID.randomUUID().toString();
                jdbcTemplate.update(ADD_SEQUENCE, word, randomSequence);
                jdbcTemplate.update(ADD_SEQUENCE, synonym, randomSequence);
            } else {
                // Add word to synonym sequence
                jdbcTemplate.update(ADD_SEQUENCE, word, synonymSequence.get(0));
            }
        } else {
            if (synonymSequence.size() == 0) {
                // Add synonym to original word sequence
                jdbcTemplate.update(ADD_SEQUENCE, synonym, wordSequence.get(0));
            } else {
                // Merge sequences into one
                jdbcTemplate.update(UPDATE_SEQUENCE, synonymSequence.get(0), wordSequence.get(0));
            }
        }
    }

}
