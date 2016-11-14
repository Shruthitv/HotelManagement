Database : hotel
Tables which i have created

create table hoteldetails
{
id int, (this should be unique)
hotelname varchar(50),
city varchar(50),
noofroomsavailable int,
price int
};

create table roomdetails
{
id int, (this should be one of the id's from hoteldetails)
roomnumber int, (this should be unique)
hotelname varchar(50), (the corresponding hotel name for id from hoteldetails)
checkin date,
checkout date,
bookingnumber varchar(50)
};


