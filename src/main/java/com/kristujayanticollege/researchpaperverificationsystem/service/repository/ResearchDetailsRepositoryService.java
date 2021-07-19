package com.kristujayanticollege.researchpaperverificationsystem.service.repository;

import com.kristujayanticollege.researchpaperverificationsystem.model.ColumnMap;
import com.kristujayanticollege.researchpaperverificationsystem.model.RPVSEnums;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

@Service
public class ResearchDetailsRepositoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // applied while retrieving details for the dashboard
    Map<String, Object> filters;

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;

        if (filters == null) {
            queryFilters = null;
            return;
        }

        generateQueryFilters();
    }

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

    public List<Map<String, Object>> getUnformattedRows() {
        String sql = "SELECT * FROM `research_details` where `isFormatted=0";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        return rows;
    }

    public List<Map<String, Object>> getResearchDetailsByGroupId(Long groupId) {

        String sql = "SELECT * FROM `research_details` WHERE `group_id` = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] { groupId });

        return rows;
    }

    public List<Map<String, Object>> getResearchDetailsByGroupIdWithMappedColumnName(Long groupId,
            List<ColumnMap> columnMaps) {

        String sql = "SELECT ";
        for (ColumnMap columnMap : columnMaps) {
            sql += "`" + columnMap.getColumnName() + "` as `" + columnMap.getMappedName() + "`, ";
        }

        sql += "`verification_status` as `Verification Status` ";

        sql += "FROM `research_details` WHERE `group_id` = ?";
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

    public List<Integer> getDistinctYears() {
        String sql = "SELECT DISTINCT year FROM `research_details`";
        return jdbcTemplate.queryForList(sql, Integer.class);
    }

    // dashboard board details
    String queryFilters;

    void generateQueryFilters() {
        String[] columnsNamesToApplyFilters = { "department", "year", "indexing", "verification_status" };

        queryFilters = " WHERE";

        for (int index = 0; index < columnsNamesToApplyFilters.length; index++) {
            String columnName = columnsNamesToApplyFilters[index];

            if (filters.containsKey(columnName) && filters.get(columnName) != null
                    && ((List) filters.get(columnName)).size() > 0) {

                queryFilters += " `" + columnName + "` IN (";

                if (columnName == "year") {
                    List<Integer> years = ((List<Integer>) filters.get(columnName));

                    for (int i = 0; i < years.size(); i++) {
                        queryFilters += " " + years.get(i);

                        if (i != years.size() - 1)
                            queryFilters += ", ";

                    }

                } else {
                    List<String> years = ((List<String>) filters.get(columnName));

                    for (int i = 0; i < years.size(); i++) {
                        queryFilters += " '" + years.get(i) + "'";

                        if (i != years.size() - 1)
                            queryFilters += ", ";

                    }
                }

                queryFilters += " ) AND";

            }

        }

        queryFilters = queryFilters.substring(0, queryFilters.length() - 3);

    }

    public Long getCount() {

        String sql = "SELECT COUNT(id) as count FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<Map<String, Object>> getGroupByIndexingCount() {

        String sql = "SELECT COUNT(id) as count, `indexing` FROM `research_details`";
        if (queryFilters != null)
            sql += queryFilters;

        sql += " GROUP BY `indexing`";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getGroupByDepartmentCount() {
        String sql = "SELECT COUNT(id) as count, `department` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += " GROUP BY `department`";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getGroupByVerificationStatusCount() {
        String sql = "SELECT COUNT(id) as count, `verification_status` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += " GROUP BY `verification_status`";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getGroupByYearWiseCount() {
        String sql = "SELECT COUNT(id) as count, `year` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += " GROUP BY `year`";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getGroupByVerificationStatusAndIndexingCount() {
        String sql = "SELECT COUNT(id) as count, `verification_status`, `indexing` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += " GROUP BY `verification_status`,`indexing`";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getProferssorSubmissionCountList() {
        String sql = "SELECT `email_address` as `Email Address`, `name` as `Full Name`,  count(`email_address`) as `Submission Count` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += "  group by `email_address` order by `Submission Count` desc;";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }

    public List<Map<String, Object>> getProferssorSubmissionCountListIndexingWise() {
        String sql = "SELECT `email_address` as `Email Address`, `name` as `Full Name`, `indexing` as `Indexing`, count(`indexing`) as `Submission Count` FROM `research_details`";

        if (queryFilters != null)
            sql += queryFilters;

        sql += " group by `indexing`,`email_address` order by `email_address` asc;";

        List<Map<String, Object>> counts = jdbcTemplate.queryForList(sql);

        return counts;
    }


    @Transactional
    public void  saveFormattedRows(List<Map<String, Object>> rows) {
        // TODO complete
    }

}
