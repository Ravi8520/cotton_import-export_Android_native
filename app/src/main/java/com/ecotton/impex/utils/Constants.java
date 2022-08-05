package com.ecotton.impex.utils;

public class Constants {
    public static final String ACCEPTED = "Packing Order";
    public static final String PENDING = "Checking Order";
    public static final String INCOMPLETE = "Delivery Attempted";
    public static final String REJECTED = "Rejected";
    public static final String CANCELLED = "Cancelled";
    public static final String DELIVERED = "Delivered";
    public static final String ASSIGNED = "Ready For Pickup";
    public static final String PICKUP = "Out For Delivery";
    public static final String PAYMENT_PENDING = "Payment Pending";
    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 2003;
    public static final int REQUEST_CODE_IMAGE_CAMERA = 2004;

    public static final String UNPAID = "unpaid";
    public static final String PAID = "paid";

    public static final String HOME = "home";
    public static final String WORK = "work";
    public static final String HOSPITAL = "hospital";

    public static final String ORDER_TYPE_FULL_ORDER = "full_order";
    public static final String ORDER_TYPE_AS_PER_PRESCRIPTION = "as_per_prescription";
    public static final String ORDER_TYPE_SELECTED_ITEM = "selected_item";
    public static final String ORDER_TYPE_MANUAL_ORDER = "manual_order";

    public static final String ORDER_ONGOING = "0";
    public static final String ORDER_HISTORY = "1";

    public static String[] IMAGE_MIME_TYPES = {"image/jpg", "image/jpeg", "image/png"};
    public static String[] IMAGE_PDF_MIME_TYPES = {"image/jpg", "image/jpeg", "image/png", "application/pdf"};

    //Tutorial Guide
    public static final String SCREEN_UPLOAD_PRESCRIPTION = "upload_prescription_screen";
    public static final String SCREEN_ADD_ADDRESS = "add_address_screen";
    public static final String SCREEN_DELIVERY_ADDRESS = "delivery_address_screen";
    public static final String SCREEN_MY_ORDER = "my_order_screen";
    public static final String SCREEN_ORDER_REVIEW = "order_review_screen";
    public static final String SCREEN_MANUAL_ORDER = "order_manual_screen";
    public static final String SCREEN_SELECT_PHARMACY = "order_select_pharmacy_screen";
    public static final String SCREEN_TERMS_AND_CONDITION = "terms_and_condition_screen";

    public static final String AFTER_MEAL = "after_meal";
    public static final String BEFORE_MEAL = "before_meal";

    public static final String ONCE = "once";
    public static final String DAILY = "daily";
    public static final String WEEKLY = "weekly";

    public static final String SPECIFIC_TIME = "specific";
    public static final String INTERVAL_TIME = "interval";


    public static final String M_ID = "EGPubB79897653639919"; //Paytm Merchand Id we got it in paytm credentials
    public static final String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential

    public static final String WEBSITE = "WEBSTAGING";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    public static final String AUTH = "Bearer fhlJjaBaa8oAYoMV7V37M7LWzkhKBBUtVuPl7aGea5Jzg8c3uCbOWTY4hVxAal6VCWGOKrpM3Uz3bQK2pdUjr7MOvEK6dTqgveyv";

    public static final String PROPREITOR = "propreitor";
    public static final String PARTNER = "partner";

    public static final String USER_PHARMACY = "pharmacy";
    public static final String USER_SELLER = "seller";
    public static final String USER_DELIVERY_BOY = "delivery_boy";
    public static  String  USERTYPE = "";

    public static  String  EXPORTER = "seller";
    public static  String  IMPORTER = "buyer";
    //server status string
    public interface ServerOrderStatus{
        String UPCOMING = "new";
        String ACCEPTED = "accept";
        String READYFORPICKUP = "assign";
        String OUTFORDELIVERY = "pickup";
        String COMPLETED = "complete";
        String REJECTED = "reject";
        String CANCELLED = "cancel";
        String RETURNED = "incomplete";
    }

    //display status string
    public interface DisplayOrderStatus{
        String UPCOMING = "Upcoming";
        String ACCEPTED = "Accepted";
        String READYFORPICKUP = "Ready for pickup";
        String OUTFORDELIVERY = "Out for delivery";
        String COMPLETED = "Completed";
        String REJECTED = "Rejected";
        String CANCELLED = "Cancelled";
        String RETURNED = "Return";
    }

    //delivery type string
    public interface DeliveryType{
        String INTERNAL = "internal";
        String EXTERNAL = "external";
        String BOTH = "both";
    }


    //order type string
    public interface OrderType{
        String AS_PER_PRESCRIPTION = "as_per_prescription";
        String FULL_ORDER = "full_order";
        String SELECTED_ITEM = "selected_item";
        String MANUAL_ORDER = "manual_order";
    }
    public static class OREDER_STATUS {
        public static final String CREATED = "created";
        public static final String PICKUP = "pickup";
        public static final String COMPLETE = "complete";
        public static final String IN_COMPLETE = "inComplete";
        public static final String CANCEL = "cancel";



    }



}
