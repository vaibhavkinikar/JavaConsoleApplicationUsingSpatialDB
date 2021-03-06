DELETE FROM USER_SDO_GEOM_METADATA 
   WHERE TABLE_NAME = 'BUILDING' AND COLUMN_NAME = 'LOCATION_POINTS';
   
DELETE FROM USER_SDO_GEOM_METADATA 
   WHERE TABLE_NAME = 'STUDENTS' AND COLUMN_NAME = 'STUDENT_POINTS';
   
DELETE FROM USER_SDO_GEOM_METADATA 
   WHERE TABLE_NAME = 'TRAMSTOPS' AND COLUMN_NAME = 'TRAM_POINTS';

DROP INDEX BUILDING_SIDX;
DROP INDEX STUDENTS_SIDX;
DROP INDEX TRAMSTOPS_SIDX;

DROP TABLE BUILDING;
DROP TABLE STUDENTS;
DROP TABLE TRAMSTOPS;

COMMIT;