package com.andevcon.hackathon.msft.api;

import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.office365.connectmicrosoftgraph.vo.MessageWrapper;
import com.microsoft.onenoteapi.service.OneNotePartsMap;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;
import com.microsoft.onenotevos.Section;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.mime.TypedString;

/**
 * Created by prtrived on 12/1/15.
 */
public class ApiClient {

    private static final String
            GET_PAGES_FROM_SECTION_URL = "/me/notes/sections/{sectionId}/pages",
            GET_SECTIONS_URL = "/me/notes/sections",
            GET_USERS_URL = "/me/photo/$value",
            GET_PAGE_CONTENT_BY_ID = "/me/notes/pages/{id}/content",
            POST_PAGE_INTO_SECTION = "/me/notes/sections/{sectionId}/pages",
            DELETE_PAGE_URL = "/me/notes/pages/{pageId}",
            GET_IMAGE_RESOURCE = "/me/notes/resources/{id}/content";

    static RestAdapter getTraveLogRestAdapter(String baseUrl) {
        return RestAdapterManager.getInstance().createRestAdapter(baseUrl);
    }

    public interface ApiService {

        @GET(GET_PAGES_FROM_SECTION_URL)
        void getPagesFromSections( @Path("sectionId") String sectionId,
                                   Callback<Envelope<Page>> callback);

        @Multipart
        @POST(POST_PAGE_INTO_SECTION)
        void postPageWithImages(@Path("sectionId") String sectionId,
                                 @PartMap OneNotePartsMap partMap,
                                 Callback<Envelope<Page>> callback);

        @Headers("Content-Type:text/html")
        @POST(POST_PAGE_INTO_SECTION)
        void postSimplePage(@Path("sectionId") String sectionId,
                            @Body TypedString content,
                            Callback<Page> callback);

        @GET(GET_SECTIONS_URL)
        void getSections(Callback<Envelope<Section>> callback);

        @GET(GET_PAGE_CONTENT_BY_ID)
        void getPageContentById(
                @Path("id") String id,
                Callback<Response> callback);

        @GET(GET_USERS_URL)
        void getUserPhoto(Callback<Response> responseCallback);

        @DELETE(DELETE_PAGE_URL)
        void deletePage(@Path("pageId") String pageId,
                Callback<Response> callback);

        @GET(GET_IMAGE_RESOURCE)
        void getPageImageResource(
                @Path("id") String id,
                Callback<Response> callback);

        @POST("/me/microsoft.graph.sendmail")
        void sendMail(
                @Header("Content-type") String contentTypeHeader,
                @Body MessageWrapper mail,
                Callback<Void> callback);
    }

    public interface General {
        @GET("/")
        void getSomething(Callback<Response> callback);
    }

    public static ApiService apiService = getTraveLogRestAdapter(Constants.MICROSOFT_GRAPH_API_ENDPOINT).create(ApiService.class);



    public static General getGenericApiService(String url) {
        return getTraveLogRestAdapter(url).create(General.class);

    }

}
