package com.cotton.importexport.api;

import com.cotton.importexport.models.AddCompanyModel;
import com.cotton.importexport.models.AddMemberModel;
import com.cotton.importexport.models.BrokerModel;
import com.cotton.importexport.models.CancelPostModel;
import com.cotton.importexport.models.CompanyDetailModel;
import com.cotton.importexport.models.CompleteTabModel;
import com.cotton.importexport.models.ContractDetailModel;
import com.cotton.importexport.models.DistrictModel;
import com.cotton.importexport.models.DocumentUploadModel;
import com.cotton.importexport.models.EditProfileModel;
import com.cotton.importexport.models.InvitedDataModel;
import com.cotton.importexport.models.MakeDeal;
import com.cotton.importexport.models.MyContractModel;
import com.cotton.importexport.models.MyFavouriteModel;
import com.cotton.importexport.models.MyMembermodel;
import com.cotton.importexport.models.MyPostActiveModel;
import com.cotton.importexport.models.MyWalletModel;
import com.cotton.importexport.models.Nagoatiaationdetaillasttwo;
import com.cotton.importexport.models.NegotiationDetail;
import com.cotton.importexport.models.NegotiationList;
import com.cotton.importexport.models.NewsList;
import com.cotton.importexport.models.NotificatioListModel;
import com.cotton.importexport.models.PostDetail;
import com.cotton.importexport.models.PostDetailSpinerData;
import com.cotton.importexport.models.PostDetailsModel;
import com.cotton.importexport.models.PostToSellModel;
import com.cotton.importexport.models.PrivatSellNotificationModel;
import com.cotton.importexport.models.ProductAttributeModel;
import com.cotton.importexport.models.ProductModel;
import com.cotton.importexport.models.ProductValueModel;
import com.cotton.importexport.models.RequestModel;
import com.cotton.importexport.models.SearchBrokerModel;
import com.cotton.importexport.models.SearchCompanyModel;
import com.cotton.importexport.models.SearchSellerModel;
import com.cotton.importexport.models.SellertypeBuyerTypeModel;
import com.cotton.importexport.models.StateModel;
import com.cotton.importexport.models.StationModel;
import com.cotton.importexport.models.UploadCompanyDetailModdel;
import com.cotton.importexport.models.UserProfileModel;
import com.cotton.importexport.models.companylist.CompanyDirectory;
import com.cotton.importexport.models.companylist.CompanyListModel;
import com.cotton.importexport.models.dashboard.DashBoardModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    //---------------------- News Module START----------------------

    @FormUrlEncoded
    @POST("news")
    Call<ResponseModel<List<NewsList>>> getNews(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("news_details")
    Call<ResponseModel<NewsList>> getNewsDetail(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    //---------------------- News Module END----------------------

    //---------------------- News Module START----------------------

    @FormUrlEncoded
    @POST("company_directory")
    Call<ResponseModel<List<CompanyDirectory>>> getBuyerSellerDirectory(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("add_to_favourites")
    Call<ResponseModel<CompanyDirectory>> add_to_favourites(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("remove_from_favourite")
    Call<ResponseModel<CompanyDirectory>> remove_from_favourite(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("my_favourites")
    Call<ResponseModel<List<MyFavouriteModel>>> my_favourites(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    //---------------------- News Module END----------------------

    //---------------------- Login Module START----------------------


    @FormUrlEncoded
    @POST("verify_mobile_number")
    Call<ResponseBody> VerifyMobileNo(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("verify_otp")
    Call<ResponseBody> VerifyOTP(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("reset_password")
    Call<ResponseBody> Reset_password(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResponseBody> Resend_otp(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ResponseBody> Forgot_password(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("registration")
    Call<ResponseBody> Registration(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> Login(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @POST("sellertype_buyertype_businesstype_registrationas")
    Call<ResponseModel<List<SellertypeBuyerTypeModel>>> sellertype_buyertype(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @POST("state_list")
    Call<ResponseModel<List<StateModel>>> state_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("get_invited_by_company_details")
    Call<ResponseModel<List<InvitedDataModel>>> get_invited_by_company_details(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("district_list")
    Call<ResponseModel<List<DistrictModel>>> district_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("station_list")
    Call<ResponseModel<List<StationModel>>> station_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @Multipart
    @POST("update_company")
    Call<ResponseModel<UploadCompanyDetailModdel>> Update_company(
            @Header("Authorization") String Authorization,
            @Part("data") RequestBody data,
            @Part MultipartBody.Part companystapm
    );

    @FormUrlEncoded
    @POST("change_password")
    Call<ResponseBody> Change_password(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseBody> LogOut(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @POST("my_company_list")
    Call<ResponseModel<List<CompanyListModel>>> Company_list(
            @Header("Authorization") String Authorization

    );

    @FormUrlEncoded
    @POST("select_company")
    Call<ResponseBody> Select_company(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("add_company")
    Call<ResponseModel<AddCompanyModel>> Addcompany(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @POST("product_list")
    Call<ResponseModel<List<ProductModel>>> Product_list(
            @Header("Authorization") String Authorization

    );

    @FormUrlEncoded
    @POST("product_attribute_list")
    Call<ResponseModel<List<ProductAttributeModel>>> Product_attribute_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("post_to_sell")
    Call<ResponseModel<PostToSellModel>> Post_to_sell(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("post_to_buy")
    Call<ResponseModel<PostToSellModel>> Post_to_buy(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("state_list_for_dashboard_buyer_filter")
    Call<ResponseModel<List<ProductModel>>> state_list_for_dashboard_buyer_filter(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("search_company")
    Call<ResponseModel<List<SearchCompanyModel>>> Search_company(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_to_buy")
    Call<ResponseModel<PrivatSellNotificationModel>> Notification_to_buy(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_to_seller")
    Call<ResponseModel<PrivatSellNotificationModel>> Notification_to_seller(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_post_seller_list")
    Call<ResponseModel<List<MyPostActiveModel>>> notification_post_seller_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_post_buy_list")
    Call<ResponseModel<List<MyPostActiveModel>>> notification_post_buy_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("cancel_post")
    Call<ResponseModel<CancelPostModel>> cancel_post(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    @FormUrlEncoded
    @POST("post_details")
    Call<ResponseModel<List<PostDetailsModel>>> post_details(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("completed_deal")
    Call<ResponseModel<List<CompleteTabModel>>> completed_deal(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("search_to_sell")
    Call<ResponseModel<List<SearchSellerModel>>> Search_to_sell(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("search_to_buy")
    Call<ResponseModel<List<SearchSellerModel>>> Search_to_buy(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("product_attribute_list_for_dashboard_filter")
    Call<ResponseModel<List<ProductValueModel>>> product_attribute_list_for_dashboard_filter(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("user_profile")
    Call<ResponseModel<UserProfileModel>> user_profile(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ResponseModel<EditProfileModel>> edit_profile(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("get_my_member_list")
    Call<ResponseModel<List<MyMembermodel>>> GetMemberList(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("request_list")
    Call<ResponseModel<List<RequestModel>>> request_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("accept_reject_member_request")
    Call<ResponseModel<RequestModel>> accept_reject_member_request(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("add_member")
    Call<ResponseModel<AddMemberModel>> Addmember(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("search_broker")
    Call<ResponseModel<List<SearchBrokerModel>>> search_broker(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("send_broker_request")
    Call<ResponseModel<SearchBrokerModel>> send_broker_request(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("get_company_details")
    Call<ResponseModel<CompanyDetailModel>> Get_company_details(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    @FormUrlEncoded
    @POST("my_contract")
    Call<ResponseModel<List<MyContractModel>>> my_contract(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("make_deal_otp_verify")
    Call<ResponseModel<List<MyContractModel>>> make_deal_otp_verify(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @Multipart
    @POST("update_transaction_tracking")
    Call<ResponseModel<DocumentUploadModel>> update_transaction_tracking(
            @Header("Authorization") String Authorization,
            @Part("data") RequestBody data,
            @Part MultipartBody.Part companystapm,
            @Part MultipartBody.Part transmit_deal,
            @Part MultipartBody.Part without_gst,
            @Part MultipartBody.Part gst_reciept,
            @Part MultipartBody.Part debit_note
    );

    @FormUrlEncoded
    @POST("contract_details")
    Call<ResponseModel<ContractDetailModel>> contract_details(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("transaction_history")
    Call<ResponseModel<MyWalletModel>> transaction_history(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("dashboard_buyer")
    Call<ResponseModel<List<DashBoardModel>>> Dashboard_buyer(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("broker_list")
    Call<ResponseModel<List<BrokerModel>>> broker_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("delete_broker")
    Call<ResponseModel<BrokerModel>> delete_broker(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("filter_buyer")
    Call<ResponseModel<List<DashBoardModel>>> filterBuyer(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

    @FormUrlEncoded
    @POST("post_details")
    Call<ResponseModel<List<PostDetail>>> getPostDetail(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    @FormUrlEncoded
    @POST("negotiation_detail")
    Call<ResponseModel<List<Nagoatiaationdetaillasttwo>>> getNegotiationDetail(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("transmit_payment_lab_list")
    Call<ResponseModel<List<PostDetailSpinerData>>> getTransmitPaymentLabList(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("broker_list_v1")
    Call<ResponseModel<List<BrokerModel>>> getBrokerList(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("negotiation_new_v2")
    Call<ResponseModel> addNegotiation(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("make_deal_new_v2")
    Call<ResponseModel<MakeDeal>> makeDeal(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("make_deal_otp_verify")
    Call<ResponseModel> makeDealOtpVerify(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("resend_deal_otp")
    Call<ResponseModel> resend_deal_otp(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("negotiation_list_new_v2")
    Call<ResponseModel<List<NegotiationList>>> negotiationList(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_to_seller_list")
    Call<ResponseModel<List<NotificatioListModel>>> notification_to_seller_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("notification_to_buy_list")
    Call<ResponseModel<List<NotificatioListModel>>> notification_to_buy_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("negotiation_detail_new_v2")
    Call<ResponseModel<NegotiationDetail>> negotiationDetail(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    // BUYER ///


    @FormUrlEncoded
    @POST("filter_seller")
    Call<ResponseModel<List<DashBoardModel>>> filteSeller(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("negotiation_list_buyer_new_v2")
    Call<ResponseModel<List<NegotiationList>>> negotiationListBuyer(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );


    @FormUrlEncoded
    @POST("lab_report_status")
    Call<ResponseModel> updateLabReportStatus(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );
}