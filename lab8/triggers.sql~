CREATE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION func_name()
RETURNS  trigger AS
$BODY$
BEGIN
new.part_number = nextval('part_number_seq');
return new;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER trig BEFORE INSERT
ON part_nyc FOR EACH ROW
EXECUTE PROCEDURE func_name();
