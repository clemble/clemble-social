package com.socialone.service.translation.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.socialone.service.translation.Translation;
import com.socialone.service.translation.TranslationDataRepository;
import com.socialone.service.translation.TranslationProvider;

public class JdbcTranslationDataRepository implements TranslationDataRepository {

    final private static RowMapper<Translation> TRANSLATION_MAPPER = new RowMapper<Translation>() {
        @Override
        public Translation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Translation(rs.getString(1), TranslationProvider.valueOf(rs.getString(2)));
        }
    };

    final private static String PUT_TRANSLATION = "INSERT INTO UTILS_NAME_VOCABULARY (WORD, TRANSLATION, PROVIDER) VALUES (?, ?, ?)";
    final private static String GET_TRANSLATION = "SELECT TRANSLATION, PROVIDER FROM UTILS_NAME_VOCABULARY WHERE WORD = ?";

    final private JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcTranslationDataRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(checkNotNull(dataSource));
    }

    public JdbcTranslationDataRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = checkNotNull(jdbcTemplate);
    }

    @Override
    public void put(String word, Translation translation) {
        jdbcTemplate.update(PUT_TRANSLATION, word, translation.getTranslation(), translation.getProvider().name());
    }

    @Override
    public Collection<Translation> get(String word) {
        return jdbcTemplate.query(GET_TRANSLATION, TRANSLATION_MAPPER, word);
    }

}
