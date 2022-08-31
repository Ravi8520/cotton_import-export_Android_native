package com.ecotton.impex.api;

import com.ecotton.impex.models.AddCompanyModel;
import com.ecotton.impex.models.AddMemberModel;
import com.ecotton.impex.models.AddPlanModel;
import com.ecotton.impex.models.BrokerModel;
import com.ecotton.impex.models.BrokerReportModel;
import com.ecotton.impex.models.CancelPostModel;
import com.ecotton.impex.models.CompanyDetailModel;
import com.ecotton.impex.models.CompleteTabModel;
import com.ecotton.impex.models.ContractDetailModel;
import com.ecotton.impex.models.ContractProductModel;
import com.ecotton.impex.models.CountryModel;
import com.ecotton.impex.models.DistrictModel;
import com.ecotton.impex.models.DocumentUploadModel;
import com.ecotton.impex.models.EditProfileModel;
import com.ecotton.impex.models.InvitedDataModel;
import com.ecotton.impex.models.MakeDeal;
import com.ecotton.impex.models.MyContractModel;
import com.ecotton.impex.models.MyFavouriteModel;
import com.ecotton.impex.models.MyMembermodel;
import com.ecotton.impex.models.MyPostActiveModel;
import com.ecotton.impex.models.MyWalletModel;
import com.ecotton.impex.models.Nagoatiaationdetaillasttwo;
import com.ecotton.impex.models.NegotiationDetail;
import com.ecotton.impex.models.NegotiationList;
import com.ecotton.impex.models.NewsList;
import com.ecotton.impex.models.NotificatioListModel;
import com.ecotton.impex.models.Plan;
import com.ecotton.impex.models.PostDetail;
import com.ecotton.impex.models.PostDetailSpinerData;
import com.ecotton.impex.models.PostDetailsModel;
import com.ecotton.impex.models.PostToSellModel;
import com.ecotton.impex.models.PrivatSellNotificationModel;
import com.ecotton.impex.models.ProductAttributeModel;
import com.ecotton.impex.models.ProductModel;
import com.ecotton.impex.models.ProductValueModel;
import com.ecotton.impex.models.ProtModel;
import com.ecotton.impex.models.RequestModel;
import com.ecotton.impex.models.SearchBrokerModel;
import com.ecotton.impex.models.SearchCompanyModel;
import com.ecotton.impex.models.SearchSellerModel;
import com.ecotton.impex.models.SellertypeBuyerTypeModel;
import com.ecotton.impex.models.StateModel;
import com.ecotton.impex.models.StationModel;
import com.ecotton.impex.models.UploadCompanyDetailModdel;
import com.ecotton.impex.models.UserProfileModel;
import com.ecotton.impex.models.companylist.CompanyDirectory;
import com.ecotton.impex.models.companylist.CompanyListModel;
import com.ecotton.impex.models.dashboard.DashBoardModel;

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
    @POST("verify_email")
    Call<ResponseBody> VerifyEmail(
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

   
    @POST("country_list")
    Call<ResponseModel<List<CountryModel>>> country_list(
            @Header("Authorization") String Authorization

    );
    @FormUrlEncoded
  @POST("port_list")
    Call<ResponseModel<List<ProtModel>>> port_list(
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
    @POST("country_list_for_dashboard_seller_filter")
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

    @FormUrlEncoded
    @POST("post_report")
    Call<ResponseModel<BrokerReportModel>> post_report(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("contract_party_list")
    Call<ResponseModel<BrokerReportModel>> contract_party_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("broker_party_wise_contract_report")
    Call<ResponseModel<BrokerReportModel>> broker_party_wise_contract_report(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("party_wise_contract_report")
    Call<ResponseModel<BrokerReportModel>> party_wise_contract_report(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );
    @FormUrlEncoded
    @POST("contract_broker_list")
    Call<ResponseModel<List<ContractProductModel>>> contract_broker_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );
    @FormUrlEncoded
    @POST("contract_product_list")
    Call<ResponseModel<List<ContractProductModel>>> contract_product_list(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("broker_wise_contract_report")
    Call<ResponseModel<ContractProductModel>> broker_wise_contract_report(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );
    @FormUrlEncoded
    @POST("product_wise_contract_report")
    Call<ResponseModel<ContractProductModel>> product_wise_contract_report(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );
    @FormUrlEncoded
    @POST("unread_negotiation_notification")
    Call<ResponseBody> unreadNegotiationNotification(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("plan_list")
    Call<ResponseModel<List<Plan>>> getPlanList(
            @Header("Authorization") String Authorization,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("add_user_plan")
    Call<ResponseModel<AddPlanModel>> add_user_plan(
            @Header("Authorization") String Authorization,
            @Field("data") String data

    );

}