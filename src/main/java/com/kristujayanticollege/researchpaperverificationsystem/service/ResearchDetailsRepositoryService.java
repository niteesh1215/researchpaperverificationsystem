package com.kristujayanticollege.researchpaperverificationsystem.service;

import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.model.RPVSEnums;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

@Service
public class ResearchDetailsRepositoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String createInsertQuery(List<ColumnMap> columnMaps) {
        String query = "insert into `research_details`(`group_id`,";
        String values = "values(?,";
        for (ColumnMap columnMap : columnMaps) {
            query += "`" + columnMap.getColumnName() + "`,";
            values += "?,";
        }

        query = query.substring(0, query.length() - 1) + ")";
        values = values.substring(0, values.length() - 1) + ")";
        return query + " " + values;
    }

    @Transactional
    public Boolean addReseachDetails(List<Map<String, Object>> researchDetailsRows, Long uploadId,
            List<ColumnMap> columnMaps) {

        final int batchSize = 500;
        String insertQuery = createInsertQuery(columnMaps);

        for (int j = 0; j < researchDetailsRows.size(); j += batchSize) {

            final List<Map<String, Object>> rowArray = researchDetailsRows.subList(j,
                    j + batchSize > researchDetailsRows.size() ? researchDetailsRows.size() : j + batchSize);

            jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Map<String, Object> row = rowArray.get(i);

                    ps.setLong(1, uploadId);
                    int j = 2;
                    for (ColumnMap columnMap : columnMaps) {
                        if (columnMap.getColumnName() == "year") {
                            ps.setInt(j, (Integer) row.get(columnMap.getMappedName().toLowerCase()));
                        } else
                            ps.setString(j, String.valueOf(row.get(columnMap.getMappedName().toLowerCase())).trim());

                        j++;
                    }

                }

                @Override
                public int getBatchSize() {
                    return rowArray.size();
                }
            });

        }
        return true;

    }

    @Transactional
    public boolean addNewResearchDetailsRow(Map<String, Object> row, List<ColumnMap> columnMaps) {
        String query = createInsertQuery(columnMaps);

        jdbcTemplate.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {

                ps.setLong(1, Long.parseLong((String) row.get("group_id")));
                int i = 2;
                for (ColumnMap columnMap : columnMaps) {
                    if (columnMap.getColumnName() == "year")
                        ps.setInt(i, (Integer) row.get(columnMap.getColumnName()));
                    else
                        ps.setString(i, String.valueOf(row.get(columnMap.getColumnName().toLowerCase())).trim());

                    i++;
                }
            }
        });
        return true;

    }

    private String createUpdateQuery(List<ColumnMap> columnMaps) {
        String query = "UPDATE `research_details` SET";
        for (ColumnMap columnMap : columnMaps) {
            query += " " + columnMap.getColumnName() + " = ? ,";
        }

        query += " verification_status = ?";

        query += " WHERE `id` = ?";

        return query;
    }

    @Transactional
    public boolean updateResearchDetailsRow(Map<String, Object> row, List<ColumnMap> columnMaps) {
        final String query = createUpdateQuery(columnMaps);

        jdbcTemplate.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                for (ColumnMap columnMap : columnMaps) {
                    if (columnMap.getColumnName() == "year")
                        ps.setInt(i, (Integer) row.get(columnMap.getColumnName()));
                    else
                        ps.setString(i, String.valueOf(row.get(columnMap.getColumnName().toLowerCase())).trim());

                    i++;
                }

                ps.setString(i++, (String) row.get("verification_status"));
                ps.setLong(i, Long.parseLong((String) row.get("id")));
            }
        });
        return true;
    }

    public List<Map<String, Object>> getResearchDetailsByGroupId(Long groupId) {

        String sql = "SELECT * FROM `research_details` WHERE `group_id` = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] { groupId });

        return rows;
    }

    public Map<String, Object> getResearchDetailsById(Long id) {
        String sql = "SELECT * FROM `research_details` WHERE `id` = ?";

        Map<String, Object> row = jdbcTemplate.queryForMap(sql, new Object[] { id });

        return row;
    }

    @Transactional
    public boolean deleteResearchDetailsById(Long id) {
        String sql = "DELETE FROM `research_details` WHERE `id` = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    @Transactional
    public void updateVerificationStatus(Long researchDetailsRowId, VerificationStatus status) {
        String sql = "UPDATE `research_details` SET `verification_status`= ? , `verification_timestamp` = NOW() WHERE `id`= ?";
        String verificationStatusValue = RPVSEnums.getVerificationStatusEnumValue(status);
        jdbcTemplate.update(sql, verificationStatusValue, researchDetailsRowId);
    }

}
