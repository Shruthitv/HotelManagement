package com.mindtree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.mindtree.model.BookedRoomDetails;
import com.mindtree.service.HotelService;

@Controller
public class HotelController {

	@Autowired
	@Qualifier(value = "hotelService")
	private HotelService hotelService;

	@RequestMapping(value = "/index")
	public String index(Model model) {
		return "index";
	}

	@RequestMapping(value = "/hotelDetails")
	public String selectCity(Model model) {
		model.addAttribute("listOfcity", this.hotelService.listOfCity());
		return "listTheHotel";
	}

	@RequestMapping(value = "/hotelsInCity", method = RequestMethod.GET)
	public @ResponseBody String displayHotelInCity(@RequestParam String city) {
		String json = new Gson().toJson(this.hotelService.listTheHotelByCity(city));
		return json;
	}

	@RequestMapping(value = "/bookARoom")
	public String bookARoom(@RequestParam("hotelId") int hotelId, Model model) {
		model.addAttribute("roomDetails", this.hotelService.getHotelDetailsById(hotelId));
		return "bookARoom";
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public void DisplayError(Model model) {
		model.addAttribute("Message", "Error has been Handled");
	}

	@RequestMapping(value = "/RoomBooked", method = RequestMethod.POST)
	public ModelAndView updateToBooked(@RequestParam("city") String city, @RequestParam("hname") String hotelName,
			@RequestParam("checkIn") String checkIn, @RequestParam("checkOut") String checkOut,
			@RequestParam("noOfRooms") int noOfRooms, ModelAndView modelAndView) {
		BookedRoomDetails bookedRoomDetails = new BookedRoomDetails();
		bookedRoomDetails.setCity(city);
		bookedRoomDetails.setHotelName(hotelName);
		bookedRoomDetails.setCheckIn(checkIn);
		bookedRoomDetails.setCheckOut(checkOut);
		bookedRoomDetails.setNoOfRooms(noOfRooms);

		String refNumber = this.hotelService.bookRoom(bookedRoomDetails);
		String message = "";
		if (!StringUtils.isEmpty(refNumber)) {
			message = " Booking done. Your refernce number is : " + refNumber
					+ " and total amount to be paid for your stay : Rs. "
					+ this.hotelService.amountTobePaid(bookedRoomDetails);
		}

		modelAndView.setViewName("index");
		modelAndView.addObject("Message", message);
		return modelAndView;
	}  

	@RequestMapping(value = "/listTheHotelForLowestPrice")
	public String selectCityForLowestPrice(Model model) {
		model.addAttribute("listOfcity", this.hotelService.listOfCity());
		return "listTheHotelForLowestPrice";
	}

	@RequestMapping(value = "/lowestPricedHotel", method = RequestMethod.GET)
	public @ResponseBody String getLowestPricedHotel(@RequestParam String city) {
		String json = new Gson().toJson(this.hotelService.getLowestPricedHotel(city));
		return json;
	}

}
