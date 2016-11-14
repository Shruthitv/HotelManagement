package com.mindtree.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RoomDetailsMapper implements RowMapper{

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		RoomDetails details = new RoomDetails();
		details.setCity(rs.getString("city"));
		details.setHotelName(rs.getString("hotelname"));
		details.setNoOfRoomsAvailable(rs.getString("noofroomsavailable"));
		return details;
	}

}
