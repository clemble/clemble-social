package com.clemble.social.utils.soundmatch.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.clemble.social.utils.soundmatch.SoundMatchAlgorithm;
import com.clemble.social.utils.soundmatch.SoundMatchDataRepository;

public class JdbcSoundMatchDataRepository implements SoundMatchDataRepository {
    final private String ADD_MATCH = "INSERT INTO UTILS_SOUND_MATCHES(WORD, PRESENTATION, ALGORITHM) VALUES(?,?,?)";
    final private String GET_MATCH = "SELECT PRESENTATION FROM UTILS_SOUND_MATCHES WHERE WORD = ? AND ALGORITHM = ?";
    
    final private RowMapper<String> SOUND_MATCH_MAPPER = new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    };
    
    final private JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcSoundMatchDataRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(checkNotNull(dataSource));
    }

    public JdbcSoundMatchDataRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = checkNotNull(jdbcTemplate);
    }

    @Override
    public String get(String word, SoundMatchAlgorithm matchAlgorithm) {
        // Step 1. Query for the sound match
        List<String> data = jdbcTemplate.query(GET_MATCH, SOUND_MATCH_MAPPER, word, matchAlgorithm.name());
        // Step 2. Return appropriate value
        return data.size() > 0 ? data.get(0) : null;
    }

    @Override
    public void put(String word, String presentation, SoundMatchAlgorithm matchAlgorithm) {
        try {
            jdbcTemplate.update(ADD_MATCH, word, presentation, matchAlgorithm.name());
        } catch(RuntimeException exception) {
            exception.printStackTrace();
        }
    }

}
