package com.genebio.nextprot.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.genebio.nextprot.domain.Publication;

@Component("publicationDAO")
public class PublicationDAO {

	@Autowired
	private DataSource datasource;
	
	private static final String SQL_SELECT_PUBLICATION_BY_ID  = "SELECT resource_id, title, abstract_text, publication_date from nextprot.publications where resource_id = :resource_id";
	private static final ParameterizedRowMapper<Publication> PUBLICATION_MAPPER = new ParameterizedRowMapper<Publication>() {
        public Publication mapRow(ResultSet resultSet, int row) throws SQLException {

          //Need to use a mapper, but it is not so bad if we don't want to use reflection since the database may use different names
          Publication publication = new Publication();
      	  publication.setId(resultSet.getLong("resource_id"));
      	  publication.setTitle(resultSet.getString("title"));
      	  publication.setAbstractText(resultSet.getString("abstract_text"));
      	  publication.setPublicationDate(resultSet.getDate("publication_date"));
          return publication;
        }
      };

    //Spring advantages: No need to open / close connection or to worry about result set...
	public Publication getPublicationById(long id) {
		//We can use named parameters which are less error prone
		SqlParameterSource namedParameters = new MapSqlParameterSource("resource_id", id);
		return new NamedParameterJdbcTemplate(datasource).queryForObject(SQL_SELECT_PUBLICATION_BY_ID, namedParameters, PUBLICATION_MAPPER);
	}
}
