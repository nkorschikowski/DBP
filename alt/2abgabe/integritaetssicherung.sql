CREATE OR REPLACE FUNCTION update_rating()
  RETURNS TRIGGER AS $$
  BEGIN
    UPDATE produkte
    SET rating = (
      SELECT AVG(bewertung)
      FROM rezensionen
      WHERE produkt_nr = NEW.produkt_nr
    )
    WHERE produkt_nr = NEW.produkt_nr;
    RETURN NEW;
  END;
  $$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_rating_del()
  RETURNS TRIGGER AS $$
  BEGIN
    UPDATE produkte
    SET rating = (
      SELECT AVG(bewertung)
      FROM rezensionen
      WHERE produkt_nr = OLD.produkt_nr
    )
    WHERE produkt_nr = OLD.produkt_nr;
    RETURN OLD;
  END;
  $$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_rating_trigger AFTER INSERT OR UPDATE
  ON rezensionen
  FOR EACH ROW
  EXECUTE FUNCTION update_rating();

CREATE OR REPLACE TRIGGER update_rating_del_trigger AFTER DELETE
  ON rezensionen
  FOR EACH ROW
  EXECUTE FUNCTION update_rating_del();