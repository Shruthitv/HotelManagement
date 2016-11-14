package com.mindtree.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mindtree.model.BookedRoomDetails;
import com.mindtree.model.Hotel;
import com.mindtree.model.RoomDetails;
import com.mindtree.model.RoomDetailsMapper;

@Repository
public class HotelService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<String> listOfCity() {
		String query = "SELECT DISTINCT CITY FROM HOTELDETAILS WHERE NOOFROOMSAVAILABLE > 0";
		List<String> listOfcity = new ArrayList<String>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
		for (Map row : list) {
			listOfcity.add((String) row.get("city"));
		}
		return listOfcity;
	}

	public List<Hotel> listTheHotelByCity(String city) {

		String query = "SELECT * FROM HOTELDETAILS where CITY = ? ";
		List<Hotel> listOfHotels = new ArrayList<Hotel>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query, new Object[] {city});
		for (Map row : list) {
			Hotel hotel = new Hotel();
			hotel.setId((Integer) row.get("id"));
			hotel.setHotelName((String) row.get("hotelname"));
			hotel.setCity((String) row.get("city"));
			hotel.setNoOfRoomsAvailable((Integer) row.get("noofroomsavailable"));
			hotel.setPrice((Integer) row.get("price"));
			listOfHotels.add(hotel);
		}
		return listOfHotels;
	}

	public RoomDetails getHotelDetailsById(int hotelId) {
		RoomDetails details = new RoomDetails();
		String query = "select city, hotelname, noofroomsavailable from hoteldetails where id = ?";
		details = (RoomDetails) jdbcTemplate.queryForObject(query, new Object[] { hotelId }, new RoomDetailsMapper());
		return details;
	}

	public String bookRoom(BookedRoomDetails bookedRoomDetails) {
		int noOfRooms = bookedRoomDetails.getNoOfRooms();

		String qry = " SELECT DISTINCT ID FROM HOTELDETAILS WHERE HOTELNAME = ?";
		int id = jdbcTemplate.queryForObject(qry, new Object[] { bookedRoomDetails.getHotelName() }, Integer.class);

		String query = " SELECT MIN(ROOMNUMBER) FROM ROOMDETAILS WHERE BOOKINGNUMBER IS NULL AND ID = ?";
		int minRoom = jdbcTemplate.queryForObject(query, new Object[] { id }, Integer.class);

		query = " SELECT MAX(ROOMNUMBER) FROM ROOMDETAILS WHERE BOOKINGNUMBER IS NULL AND ID = ?";
		int maxRoom = jdbcTemplate.queryForObject(query, new Object[] { id }, Integer.class);

		qry = "";
		if (noOfRooms > 1) {
			noOfRooms = minRoom + (noOfRooms - 1);
			int min = minRoom;
			for (int i = min + 1; i <= noOfRooms && noOfRooms <= maxRoom; i++, min++) {
				qry += " OR ROOMNUMBER = " + i;
			}
		}

		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		String refNumber = sb.toString();

		query = "UPDATE ROOMDETAILS SET CHECKIN = (SELECT STR_TO_DATE(?,'%d-%m-%Y')) , CHECKOUT = (SELECT STR_TO_DATE(?,'%d-%m-%Y')), BOOKINGNUMBER = ? WHERE ID = ? AND (ROOMNUMBER = "
				+ minRoom + qry+")";
		
		int i = this.jdbcTemplate.update(query, bookedRoomDetails.getCheckIn(), bookedRoomDetails.getCheckOut(),
				refNumber, id);
		
		String refNum = "";
		if (i == bookedRoomDetails.getNoOfRooms()) {
			query = "UPDATE HOTELDETAILS SET NOOFROOMSAVAILABLE = NOOFROOMSAVAILABLE-? WHERE ID = ?";
			this.jdbcTemplate.update(query, bookedRoomDetails.getNoOfRooms(), id);
			refNum = refNumber;
		}
		return refNum;
	}

	public int amountTobePaid(BookedRoomDetails bookedRoomDetails) {
		String query = " SELECT (DATEDIFF((SELECT DATE_FORMAT(STR_TO_DATE(?, '%d-%m-%Y'),'%Y-%m-%d')),"
				+ " (SELECT DATE_FORMAT(STR_TO_DATE(?, '%d-%m-%Y'),'%Y-%m-%d')))) * "
				+ " (SELECT PRICE FROM HOTELDETAILS WHERE HOTELNAME = ?) AS AMOUNT ";

		int amountToBePaid = jdbcTemplate.queryForObject(query, new Object[] { bookedRoomDetails.getCheckOut(),
				bookedRoomDetails.getCheckIn(), bookedRoomDetails.getHotelName() }, Integer.class);

		return amountToBePaid;
	}

	public List<Hotel> getLowestPricedHotel(String city) {
		String query = "SELECT * FROM HOTELDETAILS WHERE CITY = ? ORDER BY PRICE LIMIT 5";
		List<Hotel> listOfHotels = new ArrayList<Hotel>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query, new Object[]{city});
		for (Map row : list) {
			Hotel hotel = new Hotel();
			hotel.setId((Integer) row.get("id"));
			hotel.setHotelName((String) row.get("hotelname"));
			hotel.setCity((String) row.get("city"));
			hotel.setNoOfRoomsAvailable((Integer) row.get("noofroomsavailable"));
			hotel.setPrice((Integer) row.get("price"));
			listOfHotels.add(hotel);
		}
		return listOfHotels;
	}
}
