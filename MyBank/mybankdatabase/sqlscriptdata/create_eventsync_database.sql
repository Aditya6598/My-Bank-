DROP DATABASE IF EXISTS dbEventSync;
CREATE DATABASE IF NOT EXISTS dbEventSync;

USE dbEventSync;

SET GLOBAL group_concat_max_len = 1000000;

/* event table */
/* microservices have a common event receiving tables */
/* 
	eventdirection - -1 does not mean anything, 0 means its event waiting to be sent, 1 means event is sent, 2 means event is received
	all received events are updated with direction of 2, all events ready to be sent are marked with a direction of 0 
	eventstatus - 0 means event is not processed as yet, 1 means event is processed 
	event source - which service has created this event (0 means customer, 1 means account, 2 means money transfer)
	event destination - which service is suppose to receive the event (0 means customer, 1 means account, 2 means money transfer)
	
*/

CREATE TABLE tEvents (
_id INT NOT NULL AUTO_INCREMENT,
eventid INT NOT NULL ,
eventsource VARCHAR(400) default "",	
eventdestination VARCHAR(400) default "",	
eventdata json DEFAULT NULL,	
eventstatus INT default 0 ,
eventdirection INT default -1,
createdate timestamp default now(),
lastmodifieddate timestamp default now(),
createdby VARCHAR(255) default 'SYS',
lastmodifiedby VARCHAR(255) default 'SYS',
PRIMARY KEY (_id)
);

LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\tEventSync.csv'  
INTO TABLE tEvents    
FIELDS TERMINATED BY ','  
ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
(	
eventdata,	
eventstatus
);


