use namops_drivers_portal;

create table Driver (
    lngDriver INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    strFirstName VARCHAR(256) NOT NULL,
    strSurname VARCHAR(256) NOT NULL
);


create table JobCardItem
(
	id int auto_increment,
	jobCardNo varchar(256) not null,
	containerNo varchar(256) not null,
	containerSize varchar(256) null,
	isFull boolean default false not null,
	customerRef varchar(256) not null,
	driverId int null,
	constraint JobCardItem_pk
		primary key (id),
	constraint JobCardItem_driver_lngDriver_fk
		foreign key (driverId) references driver (lngDriver)
);
create table tblJobLogistics
(
	id int auto_increment,
	driverId int not null,
	truckReg varchar(256) not null,
	firstTrailerReg varchar(256) not null,
	secondTrailerReg varchar(256) not null,
	designatePickUpDate datetime null comment 'Date this container is to be picked up according to the client.',
	startODM LONG null,
	startLocationName text null,
	startLocationGPS text null,
	ysnUseBison boolean default false null,
	ysnUseWeighBridge boolean default false null,
	dateWeightBridgeEmpty datetime null,
	emptyTruckWeight LONG null,
	dateWeightBridgeFull datetime null,
	fullTruckWeight LONG null,
	ysnScanContainer boolean default false null,
	containerScanDate datetime null,
	actualPickUpDate datetime null,
	pickUpLocationGPS text null,
	pickUpLocationName text null,
	pickUpODM LONG null,
	container1 text null,
	container2 text null,
	container3 text null,
	unknownContainer text null,
	weighBillNo varchar(256) null,
	container1JobCardId varchar(256) null,
	container2JobCardId varchar(256) null,
	container3JobCardId varchar(256) null,
	unknownContainerJobCardId varchar(256) null,
	dropOffODM LONG null,
	dropOffLocationGPS text null,
	dropOffLocationName text null,
	memNotes text null,
	tripStatus int null comment 'Indicates the current status of this trip',
	constraint tblJobLogistics_pk
		primary key (id),
	constraint tblJobLogistics_jobcarditem_id_fk
		foreign key (driverId) references jobcarditem (id)
			on update cascade on delete cascade
);

show tables;
select * from tblJobLogistics;





