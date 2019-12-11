import org.json.JSONArray;
import org.json.JSONObject;

public class Controllers {

    public static void main(String[] args) throws Exception {
        new Controllers().updateVehicleOut("123456");
    }

    public JSONObject updateVehicleIn(String plate) throws Exception {
        return new MySqlAccess().updateVehicleIn(plate);
    }

    public String updateVehicleOut(String plate) throws Exception {
        return new MySqlAccess().updateVehicleOut(plate);
    }

    public void blockParkingSlot(String slotId) throws Exception {
        new MySqlAccess().blockParkingSlot(slotId);
    }

    public void releaseParkingSlot(String slotId) throws Exception {
        new MySqlAccess().releaseParkingSlot(slotId);
    }

    public JSONArray checkNewReservations() throws Exception {
        JSONArray reservations = new MySqlAccess().readDataBase("SELECT * FROM demoprojectdb.reserved_slot where status = 'RESERVED'");
        for (int i = 0; i < reservations.length(); i++) {
            JSONObject reservation = reservations.getJSONObject(i);
            new MySqlAccess().reservationSeen(reservation.getInt("res_id"), reservation.getString("slotId"));
        }
        return reservations;
    }
}
