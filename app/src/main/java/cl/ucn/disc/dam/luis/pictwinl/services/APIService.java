package cl.ucn.disc.dam.luis.pictwinl.services;

import cl.ucn.disc.dam.luis.pictwinl.domain.Pic;
import cl.ucn.disc.dam.luis.pictwinl.models.TokenPic;
import cl.ucn.disc.dam.luis.pictwinl.models.TokenResponse;
import cl.ucn.disc.dam.luis.pictwinl.models.TokenResponsePic;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Luis on 15-12-2016.
 */
public interface APIService {
    @POST("json/createtwin")
    Call<TokenResponsePic>insertPicInfo(@Body TokenPic tokenPic);

    @POST("test")
    Call<TokenResponse>insertPicTest(@Body Pic pic);

}
