package online.eight.locmaster;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationBTSService {
    @GET("ajax/searchCell.php")
    Call<BTSAPIResponse> getLocation(
            @Query("mcc") String mcc,
            @Query("mnc") String mnc,
            @Query("lac") String lac,
            @Query("cell_id") String cellId
    );
}

