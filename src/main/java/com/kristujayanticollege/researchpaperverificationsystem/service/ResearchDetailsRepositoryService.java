package com.kristujayanticollege.researchpaperverificationsystem.service;

import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.model.RPVSEnums;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

@Service
public class ResearchDetailsRepositoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addResearchDetailsRow(JSONObject row) {
        
    }

    private String createQuery(List<ColumnMap> columnMaps) {
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
    public Boolean addReseachDetails(JSONArray researchDetails, Long uploadId, List<ColumnMap> columnMaps) {

        final int batchSize = 500;
        String insertQuery = createQuery(columnMaps);

        for (int j = 0; j < researchDetails.size(); j += batchSize) {

            @SuppressWarnings("unchecked")
            final List<JSONObject> rowArray = researchDetails.subList(j,
                    j + batchSize > researchDetails.size() ? researchDetails.size() : j + batchSize);

            jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    JSONObject row = (JSONObject) rowArray.get(i);

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

    public List<Map<String, Object>> getResearchDetailsByGroupId(Long groupId) {

        String sql = "SELECT * FROM `research_details` WHERE `group_id` = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] { groupId });

        return rows;
    }

    public Map<String, Object> getResearchDetailsById(Long id){
        String sql = "SELECT * FROM `research_details` WHERE `id` = ?";
        
        Map<String, Object> row = jdbcTemplate.queryForMap(sql, new Object[] { id });

        return row;
    }

    @Transactional
    public boolean deleteResearchDetailsById(Long id){
        String sql = "DELETE FROM `research_details` WHERE `id` = ?";
        return jdbcTemplate.update(sql,id) == 1;
    }
 
    @Transactional 
    public void updateVerificationStatus(Long researchDetailsRowId, VerificationStatus status){
        String sql = "UPDATE `research_details` SET `verification_status`= ? , `verification_timestamp` = NOW() WHERE `id`= ?";
        String verificationStatusValue = RPVSEnums.getVerificationStatusEnumValue(status);
        jdbcTemplate.update(sql, verificationStatusValue,researchDetailsRowId);
    } 

    
}
